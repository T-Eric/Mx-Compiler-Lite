package midend.neo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import midend.llvm_ir.irassets.irId;
import midend.llvm_ir.irassets.irId.IdType;
import midend.llvm_ir.irassets.irType.IRType;
import midend.llvm_ir.irassets.statements.*;
import midend.llvm_ir.irassets.statements.instructions.*;
import midend.llvm_ir.irassets.statements.irIns.InsType;
import midend.neo.asmassets.*;
import midend.neo.asmassets.asmId.AsmType;
import midend.neo.asmassets.asmId.RegName;
import midend.neo.asmassets.asmIns.OpType;

public class neoBuilder {
  public asmWorld world;
  public irWorld ir = null;

  asmFunc curFunc = null;
  asmBlock curBlock = null;

  ArrayList<asmId> regList = new ArrayList<>();
  // HashMap<String, Integer> builtInUseCount = new HashMap<>();

  HashMap<asmId, asmId> globalHiMap = new HashMap<>();
  HashMap<asmId, asmId> globalLoMap = new HashMap<>();

  RegName sp = RegName.sp;
  RegName a0 = RegName.a0;
  RegName t0 = RegName.t0;
  RegName t1 = RegName.t1;
  RegName t2 = RegName.t2;
  RegName t3 = RegName.t3;

  RegName[] tempRegs = {RegName.t0, RegName.t1, RegName.t2, RegName.t3,
                        RegName.t4, RegName.t5, RegName.t6};
  RegName[] argRegs = {RegName.a0, RegName.a1, RegName.a2, RegName.a3,
                       RegName.a4, RegName.a5, RegName.a6, RegName.a7};
  RegName[] useRegs = {RegName.s0, RegName.s1, RegName.s2,  RegName.s3,
                       RegName.s4, RegName.s5, RegName.s6,  RegName.s7,
                       RegName.s8, RegName.s9, RegName.s10, RegName.s11};

  public neoBuilder(irWorld world) {
    for (int i = 0; i < 32; ++i)
      regList.add(new asmId(RegName.values()[i]));
    this.world = new asmWorld(world);
    this.ir = this.world.ir;
  }

  public void visitWorld() {
    // genBuiltInArgCount();
    // global variable definitions
    for (var define : ir.defines) {
      assert define.type == InsType.GlobalVar;
      var def = (globalVarIns)define;
      asmIns ins = new asmIns(def, asmIns.OpType.Global);
      asmId id = new asmId(AsmType.Global, def.result);
      //这个id起到唯一标识global的作用，使用时会提取其info使用hi和lo
      id.space = def.result.valueType.getDeref().sizeof();
      if (id.space == 1)
        id.space = 4;
      ins.globleId = id;
      if (def.result.valueType.arrayLength != -1) { // String
        id.isString = true;
        id.info = ".L" + id.info;
        id.globalInfo = def.globalVarInfo;
        id.globalInfo = id.globalInfo.replaceAll("\\\\0A", "\\\\n")
                            .replaceAll("\\\\09", "\\\\t")
                            .replaceAll("\\\\22", "\\\\\\\"")
                            .replaceAll("\\\\00", "");
        String tmp = id.globalInfo.replaceAll("\\\\", "");
        id.actualSize = tmp.length() + 1;
        world.rodataBlock.instructions.add(ins);
      } else {
        world.dataBlock.instructions.add(ins);
      }
      // 生成所有global的hi和lo的asmId，以便后续调用
      asmId hi = new asmId(AsmType.Global, null),
            lo = new asmId(AsmType.Global, null);
      hi.setGlobalPart(id, false);
      lo.setGlobalPart(id, true);
      lo.reg = t3;
      globalHiMap.put(id, hi);
      globalLoMap.put(id, lo);
    }

    // class definition (mainly the value positions)
    for (var cls : ir.classes.values()) {
      if (cls.builtIn)
        continue;
      asmClass ac = new asmClass();
      ac.theClass = cls;
      int curOffset = 0;
      for (var pa : cls.vars.values()) {
        ac.offsets.add(curOffset);
        curOffset += pa.b.sizeof();
      }
      world.classes.put(cls.name, ac);
    }
    // init finished
    // collect all functions
    LinkedList<irFunc> funcs = new LinkedList<>();
    for (var cls : ir.classes.values()) {
      if (cls.builtIn)
        continue;

      funcs.add(cls.constructor);
      for (var md : cls.methods.values())
        funcs.add(md);
    }
    // global functions
    for (var fn : ir.functions.values()) {
      if (!fn.builtIn)
        funcs.add(fn);
    }

    for (var fn : funcs)
      prepareFunc(fn);
    for (var fn : funcs)
      prepareStack(fn);
    for (var fn : funcs)
      visitFunc(fn);
  }

  void prepareFunc(irFunc ir) {
    asmFunc func = new asmFunc(ir);
    world.functions.put(ir.name, func);
    if (ir.blocks.size() == 1 && ir.retBlock.instructions.size() == 0 &&
        ir.retBlock.terminal.result.valueType.type == IRType.Void)
      func.delable = true;
    func.builtIn = false;
    func.rac.allocReg();
  }

  void prepareStack(irFunc ir) {
    var func = world.functions.get(ir.name);
    var rac = func.rac;
    var stk = func.stack;
    for (var id : rac.stackVars)
      stk.pushLow(id);
    for (var arg : rac.spilledArgs)
      stk.pushHigh(arg);
    stk.pushSave(RegName.ra);
    for (var re : rac.SaveRegsUsed)
      stk.pushSave(re);
    stk.offSet();
  }

  void visitFunc(irFunc ir) {
    if (ir.builtIn)
      return;
    var func = world.functions.get(ir.name);
    curFunc = func;
    var stk = func.stack;
    // 先把函数内容块做好再追加别的
    for (var block : ir.blocks)
      visitBlock(block);

    asmBlock first = func.blocks.get(0),
             last = func.blocks.get(func.blocks.size() - 1);
    // 将传入的参数移动到对应的s寄存器中
    for (int i = 0; i < ir.args.size(); ++i) {
      if (i > 7)
        break;
      var arg = toAsmId(ir.args.get(i));
      assert arg.asmType == AsmType.Register;
      var move = new asmIns(OpType.mv);
      move.setMv(regi(argRegs[i]), regi(arg.reg));
      first.instructions.addFirst(move);
    }

    // 分配sp，存储ra和其他callee save寄存器值
    for (var cs : stk.saveMap.keySet()) {
      asmId saveId = new asmId(stk.savePos(cs), sp);
      var saveIns = new asmIns(OpType.sw);
      saveIns.setS(regi(cs), saveId);
      first.instructions.addFirst(saveIns);
      var loadIns = new asmIns(OpType.lw);
      loadIns.setL(saveId, regi(cs));
      last.instructions.addLast(loadIns);
    }

    var spIns = new asmIns(OpType.addi);
    spIns.setArithImm(regi(sp), genImm(-stk.total), regi(sp));
    first.instructions.addFirst(spIns);

    var spins = new asmIns(OpType.addi);
    spins.setArithImm(regi(sp), genImm(stk.total), regi(sp));
    last.instructions.addLast(spins);
    last.instructions.addLast(func.ret);

    curFunc = null;
  }

  void visitBlock(irBlock ir) {
    // explicitLabel似乎总是true
    asmBlock block = new asmBlock(ir, true);
    block.funcName = curFunc.ir.name;
    curFunc.blocks.add(block);
    curBlock = block;

    for (var ins : ir.instructions)
      visitIns(ins);
    visitIns(ir.terminal);

    curBlock = null;
  }

  void visitIns(irIns irins) {
    switch (irins.type) {
      //#region alloca
    case Alloca: {
      // 已经在栈上分配了指针及其空间，为指针赋值
      var id = toAsmId(irins.result);
      var li = new asmIns(OpType.li);
      li.setLi(genImm(id.offset + 4), regi(t0));
      curBlock.instructions.add(li);
      var add = new asmIns(irins, OpType.add);
      add.setFull(regi(t0), regi(t0), regi(sp));
      curBlock.instructions.add(add);
      storeOp(id, t0);
      return;
    }
    //#region bin
    case Binary: {
      binaryIns binary = (binaryIns)irins;
      var result = toAsmId(binary.result);
      irId lhs = binary.lhs, rhs = binary.rhs;
      asmId rs1 = null, rs2 = null;
      if (lhs.type == IdType.Constant) {
        genConstAt(lhs, t1);
        rs1 = regi(t1);
      } else
        rs1 = regi(loadOp(toAsmId(lhs), t1));

      if (rhs.type == IdType.Constant) {
        genConstAt(rhs, t2);
        rs2 = regi(t2);
      } else
        rs2 = regi(loadOp(toAsmId(rhs), t2));
      var op = OpType.values()[OpType.add.ordinal() + binary.op.ordinal()];
      var ins = new asmIns(irins, op);
      var resultReg = getDest(result, t0);
      ins.setFull(regi(resultReg), rs1, rs2);
      curBlock.instructions.add(ins);
      storeOp(result, resultReg);
      return;
    }
    //#region cast
    case Bitcast: {
      bitcastIns cast = (bitcastIns)irins;
      var dest = toAsmId(cast.result);
      var src = toAsmId(cast.castPtr);
      if (dest.asmType == AsmType.Register && src.asmType == AsmType.Register)
        moveReg(src.reg, dest.reg);
      else
        storeOp(dest, loadOp(src, getDest(dest, t0)));
      return;
    }
    //#region br
    case Br: {
      brIns br = (brIns)irins;
      if (br.cond == null) {
        var jmp = new asmIns(irins, OpType.j);
        asmId label = new asmId(AsmType.Label, br.trueLabel);
        label.setLabel(curFunc.ir.name);
        jmp.setJ(label);
        curBlock.instructions.add(jmp);
      } else {
        RegName condReg;
        if (br.cond.type == IdType.Constant) {
          condReg = t0;
          genConstAt(br.cond, t0);
        } else {
          condReg = loadOp(toAsmId(br.cond), t0);
        }

        var bnez = new asmIns(irins, OpType.bnez);
        var go = new asmId(AsmType.Label, br.trueLabel);
        go.setLabel(curFunc.ir.name);
        bnez.setBz(regi(condReg), go);
        curBlock.instructions.add(bnez);

        var jmp = new asmIns(OpType.j);
        var then = new asmId(AsmType.Label, br.falseLabel);
        then.setLabel(curFunc.ir.name);
        jmp.setJ(then);
        curBlock.instructions.add(jmp);
      }
      return;
    }
    //#region call
    case Call: {
      callIns call = (callIns)irins;
      // 大胆尝试：如果要call的函数是void型且只有一条ret
      // void语句，直接忽略这个call
      var fn = world.functions.get(call.callName);
      if (fn != null && fn.delable)
        return;

      boolean isVoid = call.result.valueType.type == IRType.Void;
      // 如果是builtin，保存对应数量
      // 否则保存函数的callerSaveRegsUsed与inActiveIds持有寄存器的交
      // HashSet<RegName> reserve = null;
      // if (call.builtin) {
      //   int count = builtInUseCount.get(call.callName);
      //   reserve = new HashSet<>();
      //   if (call.result.valueType.type != IRType.Void)
      //     reserve.add(a0); // 有返回值则必须考虑存a0
      //   for (int i = 0; i < count; ++i) {
      //     reserve.add(RegName.values()[a0.ordinal() + i]);
      //   }
      // } else {
      //   reserve = new HashSet<>();
      //   for (var aid : call.inActiveIds) {
      //     var id = toAsmId(aid);
      //     if (id.asmType == AsmType.Register)
      //       reserve.add(id.reg);
      //   }
      //   // TODO 需要考察与allocaIns的适配性
      //   reserve.retainAll(fn.rac.callerSaveRegsUsed);
      //   // reserve=fn.rac.callerSaveRegsUsed;
      // }
      // // 保存之，忽略结果寄存器
      // HashMap<RegName, asmId> reserveId = new HashMap<>();
      // for (var reg : reserve) {
      //   if (resultAT && resultATreg == reg)
      //     continue;
      //   var resId = new asmId(curFunc.stack.reservePos(reg), sp);
      //   reserveId.put(reg, resId);
      //   storeOp(resId, reg);
      // }

      // load values to a-regs and that stack
      int offset = -4; // ra跟saveRegs坐一桌了，不必为它考虑
      for (int i = 0; i < call.args.size(); ++i) {
        var irarg = call.args.get(i);
        if (i < 8) {
          // a0~a7存值
          RegName dest = argRegs[i];
          if (irarg.type == IdType.Null) {
            moveReg(RegName.zero, dest);
          } else if (irarg.type == IdType.Constant) {
            genConstAt(irarg, dest);
          } else {
            var arg = toAsmId(irarg);
            if (arg.asmType == AsmType.Register)
              moveReg(arg.reg, dest);
            else
              loadOp(arg, dest);
          }
        } else {
          asmId arg = null;
          if (irarg.type == IdType.Null) {
            moveReg(RegName.zero, t0);
            arg = new asmId(t0);
          } else if (irarg.type == IdType.Constant) {
            // 立即数，赋值到t0
            genConstAt(irarg, t0);
            arg = new asmId(t0);
          } else {
            arg = toAsmId(irarg);
          }
          var address = new asmId(offset, sp);
          storeOp(address, loadOp(arg, t0));
          offset -= 4;
        }
      }

      // call
      var calling = new asmIns(call, OpType.call);
      var label = new asmId(AsmType.Label, null);
      label.info = call.callName;
      calling.setCall(label);
      curBlock.instructions.add(calling);

      if (!isVoid) {
        var result = toAsmId(call.result);
        // 保存值
        if (result.asmType == AsmType.Register)
          moveReg(a0, result.reg);
        else
          storeOp(result, a0);
      }

      return;
    }
    //#region gep
    case Getelementptr: {
      getelementptrIns gep = (getelementptrIns)irins;
      if (gep.indices.size() == 1) {
        var baseId = toAsmId(gep.objectPtr);
        var baseReg = loadOp(baseId, t0); // 载入t0
        var result = toAsmId(gep.result);
        var resultReg = getDest(result, t0);
        int offset = 0;

        // 获得偏移量
        if (gep.indices.get(0).type == IdType.Constant) {
          // add base base,const
          offset = gep.result.valueType.getDeref().sizeof() *
                   gep.indices.get(0).constValue;
          var addi = new asmIns(irins, OpType.addi);
          addi.setArithImm(regi(baseReg), genImm(offset), regi(resultReg));
          curBlock.instructions.add(addi);
        } else {
          // li t2, sizeof
          // lw t1, index
          // mul t1 t1,t2
          // add base base,t1
          var li = new asmIns(irins, OpType.li);
          li.setLi(genImm(gep.result.valueType.getDeref().sizeof()), regi(t2));
          curBlock.instructions.add(li);

          var indexId = toAsmId(gep.indices.get(0));
          var mul = new asmIns(OpType.mul);
          mul.setFull(regi(t1), regi(loadOp(indexId, t1)), regi(t2));
          curBlock.instructions.add(mul);

          var add = new asmIns(irins, OpType.add);
          add.setFull(regi(resultReg), regi(t1), regi(baseReg));
          curBlock.instructions.add(add);
        }
        // value存在baseReg中
        result.space = gep.result.valueType.getDeref().sizeof();
        storeOp(result, resultReg);
      } else {
        if (gep.objectPtr.valueType.getDeref().arrayLength != -1) {
          // String
          var re = loadOp(toAsmId(gep.objectPtr), t0);
          var result = toAsmId(gep.result);
          var resultReg = getDest(result, t0);
          moveReg(re, resultReg);
          storeOp(result, resultReg);
        } else {
          assert gep.objectPtr.valueType.theClass != null;
          irClass irc = gep.objectPtr.valueType.theClass;
          var baseReg = loadOp(toAsmId(gep.objectPtr), t0);
          var result = toAsmId(gep.result);
          var resultReg = getDest(result, t0);

          asmClass cls = world.classes.get(irc.name);
          int offset = cls.offsets.get(gep.indices.get(1).constValue);
          var addi = new asmIns(irins, OpType.addi);
          addi.setArithImm(regi(baseReg), genImm(offset), regi(resultReg));
          curBlock.instructions.add(addi);
          storeOp(result, resultReg);
          result.space = gep.result.valueType.getDeref().sizeof();
        }
      }
      return;
    }
    //#region icmp
    case Icmp: {
      icmpIns icmp = (icmpIns)irins;
      var result = toAsmId(icmp.result);
      asmId rs1 = null, rs2 = null;
      irId lhs = icmp.lhs, rhs = icmp.rhs;

      if (lhs.type == IdType.Constant) {
        // 立即数，赋值到t0
        genConstAt(lhs, t1);
        rs1 = regi(t1);
      } else
        rs1 = regi(loadOp(toAsmId(lhs), t1));

      if (rhs.type == IdType.Constant) {
        genConstAt(rhs, t2);
        rs2 = regi(t2);
      } else if (rhs.type == IdType.Null) {
        moveReg(RegName.zero, t2);
        rs2 = regi(t2);
      } else
        rs2 = regi(loadOp(toAsmId(rhs), t2));

      var sub = new asmIns(OpType.sub);
      sub.setFull(regi(t0), rs1, rs2);
      curBlock.instructions.add(sub);

      var resultReg = getDest(result, t0);
      if (icmp.op == icmpIns.OpType.Sge || icmp.op == icmpIns.OpType.Sle) {
        // seqz t1,t0
        // sltz||sgtz t2,t0
        // or t0,t1,t2
        asmIns seqz = new asmIns(OpType.seqz),
               stz = new asmIns(icmp.op == icmpIns.OpType.Sge ? OpType.sgtz
                                                              : OpType.sltz),
               orins = new asmIns(irins, OpType.or);
        seqz.setSz(regi(t1), regi(t0));
        stz.setSz(regi(t2), regi(t0));
        orins.setFull(regi(resultReg), regi(t1), regi(t2));
        curBlock.instructions.add(seqz);
        curBlock.instructions.add(stz);
        curBlock.instructions.add(orins);
      } else {
        // seqz||snez||sltz||sgtz t0, t0
        OpType op;
        switch (icmp.op) {
        case Eq:
          op = OpType.seqz;
          break;
        case Ne:
          op = OpType.snez;
          break;
        case Sgt:
          op = OpType.sgtz;
          break;
        case Slt:
          op = OpType.sltz;
          break;
        default:
          throw new RuntimeException();
        }
        var ins = new asmIns(irins, op);
        ins.setSz(regi(resultReg), regi(t0));
        curBlock.instructions.add(ins);
      }
      storeOp(result, resultReg);
      return;
    }
    //#region load
    case Load: {
      loadIns load = (loadIns)irins;
      var result = toAsmId(load.result);
      var address = toAsmId(load.loadAddr);
      if (address.asmType == AsmType.Global) {
        var resultReg = getDest(result, t1);
        loadOp(address, resultReg);
        storeOp(result, resultReg);
      } else {
        var addReg = loadOp(address, t0);
        var resultReg = getDest(result, t1);
        var midId = new asmId(0, addReg);
        var lw = new asmIns(irins, OpType.lw);
        lw.setL(midId, regi(resultReg));
        curBlock.instructions.add(lw);
        storeOp(result, resultReg);
      }
      return;
    }
    case Move: {
      moveIns move = (moveIns)irins;
      var dest = toAsmId(move.result);
      asmId src = null;

      // 可能出现赋予常数或null的情形
      if (move.src.type == IdType.Constant) {
        genConstAt(move.src, t0);
        src = regi(t0);
      } else if (move.src.type == IdType.Null) {
        moveReg(RegName.zero, t0);
        src = regi(t0);
      } else
        src = toAsmId(move.src);

      if (dest.asmType == AsmType.Register && src.asmType == AsmType.Register)
        moveReg(src.reg, dest.reg);
      else
        storeOp(dest, loadOp(src, getDest(dest, t0)));
      return;
    }
    //#region ret
    case Ret: {
      retIns ret = (retIns)irins;
      //暂存ret，后面要收尾时插入
      var retType = ret.result.valueType;
      var re = new asmIns(irins, OpType.ret);
      if (retType.type != IRType.Void) {
        if (ret.retValue.type == IdType.Constant) {
          genConstAt(ret.retValue, a0);
        } else if (ret.retValue.type == IdType.Null) {
          var li = new asmIns(OpType.li);
          li.setLi(genImm(0), regi(a0));
          curBlock.instructions.add(li);
        } else
          moveReg(loadOp(toAsmId(ret.retValue), a0), a0);
      }
      curFunc.ret = re;
      return;
    }
    //#region store
    case Store: {
      storeIns store = (storeIns)irins;
      var result = toAsmId(store.storeAddr);

      //将地址加载进t0中
      var midId = result.asmType == AsmType.Global
                      ? result
                      : new asmId(0, loadOp(result, t0));
      RegName valueReg = null;
      if (store.storeValue.type == IdType.Constant) {
        genConstAt(store.storeValue, t1);
        valueReg = t1;
      } else if (store.storeValue.type == IdType.Null) {
        valueReg = RegName.zero;
      } else {
        valueReg = loadOp(toAsmId(store.storeValue), t1);
      }
      storeOp(midId, valueReg);
      return;
    }

    default:
      throw new RuntimeException();
    }
  }

  //#region tools

  RegName loadOp(asmId id, RegName re) {
    // 把id对应存储点的值存入re寄存器，如果是寄存器值就直接返回寄存器
    // 先load再用，所以直接插入block
    if (id.asmType == AsmType.Register)
      return id.reg;
    if (id.asmType == AsmType.Address) {
      String loadInfo = id.ir == null
                            ? ""
                            : String.format("load id %s to reg %s",
                                            id.ir.toString(), re.toString());
      var load = new asmIns(new declareIns(loadInfo), OpType.lw);
      load.setL(id, regi(re));
      curBlock.instructions.add(load);
      return re;
    }
    if (id.asmType == AsmType.Global) {
      String loadInfo = String.format("load global %s to reg %s",
                                      id.ir.toString(), re.toString());
      var lui = new asmIns(new declareIns(loadInfo), OpType.lui);
      lui.setLi(globalHiMap.get(id), regi(t3));
      curBlock.instructions.add(lui);

      if (id.isString) {
        var addi = new asmIns(OpType.addi);
        addi.setArithImm(regi(t3), globalLoMap.get(id), regi(re));
        curBlock.instructions.add(addi);
        // 加载一个地址，通过 mv a0, re 投放给函数
      } else {
        var load = new asmIns(OpType.lw);
        load.setL(globalLoMap.get(id), regi(re));
        curBlock.instructions.add(load);
        // 加载一个值，re中就存了这个值
      }
      return re;
    }
    throw new RuntimeException();
  }

  RegName getDest(asmId id, RegName re) {
    // 为指令得到一个合适的dest寄存器
    if (id.asmType == AsmType.Register)
      return id.reg;
    return re;
  }

  void storeOp(asmId id, RegName re) {
    // 将re中的值存进id对应的存储点，如果是寄存器值就直接参与计算
    // 先得再store，先用getDest顶替，后续补插入，故不返回值
    if (id.asmType == AsmType.Register)
      return;
    if (id.asmType == AsmType.Address) {
      String storeInfo = id.ir == null
                             ? ""
                             : String.format("store reg %s to id %s",
                                             re.toString(), id.ir.toString());
      var store = new asmIns(new declareIns(storeInfo), OpType.sw);
      store.setS(regi(re), id);
      curBlock.instructions.add(store);
      return;
    }
    if (id.asmType == AsmType.Global) {
      String storeInfo = String.format("store reg %s to global %s",
                                       re.toString(), id.ir.toString());
      var lui = new asmIns(new declareIns(storeInfo), OpType.lui);
      lui.setLi(globalHiMap.get(id), regi(t3));
      curBlock.instructions.add(lui);
      // 需要相信不会把“”的字符串常量进行更改
      var store = new asmIns(OpType.sw);
      store.setS(regi(re), globalLoMap.get(id));
      curBlock.instructions.add(store);
      // 之前的pointer是为了一站式解决指针类变量
      // 现在只用在load和store的irIns处做翻译即可
      return;
    }
  }

  void moveReg(RegName src, RegName dest) {
    if (src == dest)
      return;
    var move = new asmIns(OpType.mv);
    move.setMv(regi(src), regi(dest));
    curBlock.instructions.add(move);
  }

  asmId regi(RegName re) { return regList.get(re.ordinal()); }

  asmId genImm(int n) { return new asmId(n); }

  void genConstAt(irId ir, RegName re) {
    if (ir.type == IdType.Null)
      ir.constValue = 0;
    if (ir.constValue == 0) {
      moveReg(RegName.zero, re);
      return;
    }
    if (ir.constValue > (1 << 15)) {
      int high = ir.constValue >> 12, low = ir.constValue & ((1 << 12) - 1);
      if (low > 2047) {
        low -= 4096;
        high++;
      }
      asmIns lui = new asmIns(null, asmIns.OpType.lui);
      lui.setLi(new asmId(high), regi(re));
      asmIns addi = new asmIns(null, asmIns.OpType.addi);
      addi.setArithImm(regi(re), new asmId(low), regi(re));
      curBlock.instructions.add(lui);
      curBlock.instructions.add(addi);
    } else {
      asmIns ins = new asmIns(null, asmIns.OpType.li);
      ins.setLi(new asmId(ir.constValue), regi(re));
      curBlock.instructions.add(ins);
    }
  }

  asmId toAsmId(irId ir) {
    if (curFunc.rac.idMap.containsKey(ir))
      return curFunc.rac.idMap.get(ir);
    return asmId.idMap.get(ir);
  }

  boolean isCallerSaved(RegName re) {
    int reg = re.ordinal();
    return reg >= 10 && reg <= 17 || reg >= 29 && reg <= 31;
  }

  // void genBuiltInArgCount() {
  //   builtInUseCount = new HashMap<>() {
  //     {
  //       // put("malloc", 7);// 发现a系列全都被污染成0xdeadbeef
  //       // put("_new_array", 3);
  //       // put("_print", 2);
  //       // put("_println", 2);
  //       // put("_printInt", 2);
  //       // put("_printlnInt", 2);
  //       // put("_getString", 2);
  //       // put("_getInt", 2);
  //       // put("_toString", 3);
  //       // put("_string_add", 2);
  //       // put("_string_eq", 2);
  //       // put("_string_ne", 2);
  //       // put("_string_le", 2);
  //       // put("_string_leq", 2);
  //       // put("_string_ge", 2);
  //       // put("_string_geq", 2);
  //       // put("_string_substring", 3);
  //       // put("_string_parseInt", 3);
  //       // put("_string_ord", 2);
  //       // put("_boolToString", 3);
  //       put("malloc", 7); // 发现a系列全都被污染成0xdeadbeef
  //       put("_new_array", 7);
  //       put("_print", 7);
  //       put("_println", 7);
  //       put("_printInt", 7);
  //       put("_printlnInt", 7);
  //       put("_getString", 7);
  //       put("_getInt", 7);
  //       put("_toString", 7);
  //       put("_string_add", 7);
  //       put("_string_eq", 7);
  //       put("_string_ne", 7);
  //       put("_string_le", 7);
  //       put("_string_leq", 7);
  //       put("_string_ge", 7);
  //       put("_string_geq", 7);
  //       put("_string_substring", 7);
  //       put("_string_parseInt", 7);
  //       put("_string_ord", 7);
  //       put("_boolToString", 7);
  //     }
  //   };
  // }

  //#endregion
}
