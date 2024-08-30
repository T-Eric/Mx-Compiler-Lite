package midend.asm;

import java.util.ArrayList;
import java.util.HashSet;
import midend.asm.asmassets.*;
import midend.asm.asmassets.asmId.AsmType;
import midend.asm.asmassets.asmId.RegName;
import midend.asm.asmassets.statements.*;
import midend.llvm_ir.irassets.irId;
import midend.llvm_ir.irassets.irType;
import midend.llvm_ir.irassets.irId.IdType;
import midend.llvm_ir.irassets.irType.IRType;
import midend.llvm_ir.irassets.statements.*;
import midend.llvm_ir.irassets.statements.instructions.*;
import midend.llvm_ir.irassets.statements.irIns.InsType;
import org.antlr.v4.runtime.misc.Pair;
public class asmBuilder {
  public asmWorld world;
  public irWorld ir = null;

  asmFunc curFunc = null;
  asmBlock curBlock = null;

  ArrayList<asmId> regs = new ArrayList<>();
  HashSet<irId> occured = new HashSet<>(); // 记录这一轮指令后要扫除的栈空间

  RegName sp = RegName.sp;
  RegName a0 = RegName.a0;
  RegName a1 = RegName.a1;
  RegName a2 = RegName.a2;
  RegName s1 = RegName.s1;

  public asmBuilder(irWorld world) {
    for (int i = 0; i < 32; ++i) {
      asmId reg = new asmId(asmId.RegName.values()[i]);
      regs.add(reg);
    }
    this.world = new asmWorld(world);
    this.ir = this.world.ir;
  }

  asmId getAsmId(irId ir) {
    assert asmId.idMap.containsKey(ir);
    assert ir.type != IdType.Constant;
    ir.refTime--;
    return asmId.idMap.get(ir);
  }

  public void visitWorld() {
    // ignore declares
    // global variables definitions
    for (var define : ir.defines) {
      assert define.type == InsType.GlobalVar;
      var def = (globalVarIns)define;
      asmIns ins = new asmIns(def, asmIns.OpType.Global);
      asmId id = new asmId(AsmType.Global, def.result);
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
    }
    // class offsets and methods get
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

      var ircst = cls.constructor;
      asmFunc cst = new asmFunc(ircst);
      world.functions.put(ircst.name, cst);
      curFunc = cst;
      for (var irarg : ircst.args) {
        asmId arg = new asmId(AsmType.Address, irarg);
        cst.stack.push_high(new Pair<asmId, Integer>(arg, 4));
        arg.size = 4;
        cst.args.add(arg);
      }
      curFunc = null;
      for (var md : cls.methods.values()) {
        asmFunc func = new asmFunc(md);
        world.functions.put(md.name, func);
        curFunc = func;
        for (var irarg : md.args) {
          asmId arg = new asmId(AsmType.Address, irarg);
          func.stack.push_high(new Pair<asmId, Integer>(arg, 4));
          arg.size = 4;
          if (irarg.valueType.dimension != 0)
            arg.pointer = s1;
          func.args.add(arg);
        }
        curFunc = null;
      }
      visitFunc(cls.constructor);
      for (var md : cls.methods.values()) {
        visitFunc(md);
      }
    }
    // funcdefs
    /* 设想：先声明所有func，取得他们的栈
     *
     */
    // func get
    for (var fn : ir.functions.values()) {
      if (fn.builtIn)
        continue;
      asmFunc func = new asmFunc(fn);
      world.functions.put(fn.name, func);
      curFunc = func;
      // 在这里事先放好各个参数，倒序存放方便后面直接使用
      for (var irarg : fn.args) {
        asmId arg = new asmId(AsmType.Address, irarg);
        func.stack.push_high(new Pair<asmId, Integer>(arg, 4));
        arg.size = 4;
        if (irarg.valueType.dimension != 0)
          arg.pointer = s1;
        // 函数构建完之后再setAddress
        func.args.add(arg);
      }
      curFunc = null;
    }
    for (var fn : ir.functions.values()) {
      visitFunc(fn);
    }
  }

  public void visitFunc(irFunc ir) {
    if (ir.builtIn)
      return;
    asmFunc func = world.functions.get(ir.name);
    curFunc = func;
    // 初步设想：正常翻译blocks，最后把计算好的stackSize做个头尾
    // 参数是外界传进来，目前认为是caller负责分配栈空间和保存
    // 不过我们分配栈空间时还是用自己的args去做，保证两者对应即可

    // 先建立ra的栈顶内存
    func.raAddr = new asmId(AsmType.Address, null);
    func.raAddr.size = 4;
    func.stack.push_high(new Pair<asmId, Integer>(func.raAddr, 4));

    for (var blk : ir.blocks) {
      asmBlock b = visitBlock(blk);
      b.funcName = ir.name;
      func.blocks.add(b);
    }
    occured.clear();
    // 添加addi和ra
    // TODO
    int allocSize = 0;
    for (; allocSize <= 65536; allocSize += 16)
      if (allocSize >= func.stack.total)
        break;
    asmBlock first = func.blocks.get(0),
             last = func.blocks.get(func.blocks.size() - 1);

    for (int i = 2000; i < allocSize; i += 2000) {
      asmIns stackAlloc = new asmIns(null, asmIns.OpType.addi);
      stackAlloc.setArithImm(asmIns.OpType.addi, regi(sp), new asmId(-2000),
                             regi(sp));
      first.pre.add(stackAlloc);
    }
    asmIns stackAlloc = new asmIns(null, asmIns.OpType.addi);
    stackAlloc.setArithImm(asmIns.OpType.addi, regi(sp),
                           new asmId(-(allocSize % 2000)), regi(sp));
    first.pre.add(stackAlloc);
    // 添加sw ra和lw ra
    func.raAddr.setAddress(allocSize - 4, sp);
    asmIns raSave = new asmIns(null, asmIns.OpType.sw);
    raSave.setS(func.raAddr, regi(RegName.ra));
    first.pre.add(raSave);

    // 此时为各个参数赋值
    int offset = -8;
    for (var arg : func.args) {
      arg.setAddress(allocSize + offset, sp);
      offset -= 4;
    }

    asmIns originRet = last.instructions.get(last.instructions.size() - 1);
    last.instructions.remove(last.instructions.size() - 1);
    asmIns raLoad = new asmIns(null, asmIns.OpType.lw);
    raLoad.setL(func.raAddr, regi(RegName.ra));

    last.instructions.add(raLoad);
    for (int i = 2000; i < allocSize; i += 2000) {
      asmIns stackReturn = new asmIns(null, asmIns.OpType.addi);
      stackReturn.setArithImm(asmIns.OpType.addi, regi(sp), new asmId(2000),
                              regi(sp));
      last.instructions.add(stackReturn);
    }
    asmIns stackReturn = new asmIns(null, asmIns.OpType.addi);
    stackReturn.setArithImm(asmIns.OpType.addi, regi(sp),
                            new asmId(allocSize % 2000), regi(sp));
    last.instructions.add(stackReturn);
    last.instructions.add(originRet);
    curFunc = null;
  }

  public asmBlock visitBlock(irBlock ir) {
    asmBlock block = new asmBlock(ir, true);
    curBlock = block;
    // 初步设想：最简单的逻辑是直接翻译。应该可以出正确的事就是了
    for (var ins : ir.instructions)
      visitIns(ins);
    visitIns(ir.terminal);
    ArrayList<irId> occurHere = new ArrayList<>();
    for (var id : occured)
      if (id.refTime == 0 && id.type == IdType.Local) {
        curFunc.stack.recycle(asmId.idMap.get(id));
        occurHere.add(id);
      }
    for (var id : occurHere)
      occured.remove(id);
    curBlock = null;
    return block;
  }

  public void visitIns(irIns irins) {
    // 每个irIns可能生成许多个asmIns的集合
    switch (irins.type) {
    case Alloca: { // 分配指针
      allocaIns alloca = (allocaIns)irins;
      // 类似于堆上分发，不过是栈上连续空间
      asmId head = new asmId(AsmType.Address, alloca.result);
      head.setAddress(curFunc.stack.push_force(head, 1), sp);
      head.size = 4;
      alloca.result.refTime--;
      occured.add(alloca.result);
      int sum =
          alloca.allocaQuantity == null ? 1 : alloca.allocaQuantity.constValue;
      curFunc.stack.push_force(head, sum);
      head.hasBlocks += sum;
      // head=sp+4
      asmIns li = new asmIns(null, asmIns.OpType.li);
      li.setLi(new asmId(head.offset + 4), regi(a0));
      curBlock.instructions.add(li);
      asmIns add = new asmIns(null, asmIns.OpType.add);
      add.setFull(regi(a0), regi(a0), regi(sp));
      curBlock.instructions.add(add);
      genStore(head, 4);
      head.pointer = s1;
      return;
    }
    case Binary: {
      // 都不需要担心堆上问题
      binaryIns binary = (binaryIns)irins;
      asmId res = newId(binary.result, sp);
      // 处理常数
      // boolean needBothReg =
      //     binary.op.ordinal() >= binaryIns.OpType.Sub.ordinal() &&
      //     binary.op.ordinal() <= binaryIns.OpType.Srem.ordinal();
      asmId rs1 = null, rs2 = null;
      irId lhs = binary.lhs, rhs = binary.rhs;

      if (lhs.type != IdType.Constant && rhs.type != IdType.Constant) {
        rs1 = getAsmId(lhs);
        rs2 = getAsmId(rhs);
        genLoadAt(rs1, a1, 4);
        genLoadAt(rs2, a2, 4);
      } else if (lhs.type == IdType.Constant && rhs.type != IdType.Constant) {
        rs2 = getAsmId(rhs);
        genConstAt(lhs, a1);
        genLoadAt(rs2, a2, 4);
      } else if (lhs.type != IdType.Constant && rhs.type == IdType.Constant) {
        rs1 = getAsmId(lhs);
        genLoadAt(rs1, a1, 4);
        genConstAt(rhs, a2);
      } else if (lhs.type == IdType.Constant && rhs.type == IdType.Constant) {
        genConstAt(lhs, a1);
        genConstAt(rhs, a2);
      }

      asmIns.OpType op =
          asmIns.OpType
              .values()[asmIns.OpType.add.ordinal() + binary.op.ordinal()];
      asmIns ins = new asmIns(binary, op);
      ins.rd = regi(a0);
      ins.rs1 = regi(a1);
      ins.rs2 = regi(a2);

      // TODO 这个i可能超界，还是不冒这个险了
      curBlock.instructions.add(ins);
      genStore(res, 4);
      return;
    }
    case Bitcast: {
      bitcastIns cast = (bitcastIns)irins;
      asmId res = newId(cast.result, sp);
      asmId src = getAsmId(cast.castPtr);
      RegName r = src.pointer;
      src.pointer = null;
      genLoad(src, 4);
      genStore(res, 4);
      src.pointer = r;
      res.pointer = r;
      res.pointToHeap = cast.result.valueType.dimension > 0;
      // 比如i8*到i32**的转换，i32**显然是pointToHeap的
      return;
    }
    case Br: {
      brIns br = (brIns)irins;
      // 跳转到label
      if (br.cond == null) {
        // j label即可
        asmIns jmp = new asmIns(irins, asmIns.OpType.j);
        asmId label = new asmId(AsmType.Label, br.trueLabel);
        label.setLabel(curFunc.ir.name);
        jmp.setJ(label);
        curBlock.instructions.add(jmp);
      } else {
        // 加载cond到a0
        if (br.cond.type == IdType.Constant)
          genConst(br.cond);
        else
          genLoad(getAsmId(br.cond), 4);

        asmIns bnez = new asmIns(irins, asmIns.OpType.bnez);
        asmId go = new asmId(AsmType.Label, br.trueLabel);
        go.setLabel(curFunc.ir.name);
        bnez.setBz(regi(a0), go);
        curBlock.instructions.add(bnez);

        asmIns jmp = new asmIns(irins, asmIns.OpType.j);
        asmId label = new asmId(AsmType.Label, br.falseLabel);
        label.setLabel(curFunc.ir.name);
        jmp.setJ(label);
        curBlock.instructions.add(jmp);
      }
      return;
    }
    case Call: {
      // 现阶段先把所有的参数全部放在栈中
      callIns call = (callIns)irins;
      // args要么是已有%，要么是constant，需要先去找一找
      if (call.builtin) {
        // 必须按照a0,a1,a2传入，但是现阶段没必要保存这些值？
        int ord = RegName.a0.ordinal();
        for (var irarg : call.args) {
          if (asmId.idMap.containsKey(irarg)) {
            // %
            asmId id = getAsmId(irarg);

            var r = id.pointer;
            id.pointer = null;
            genLoadAt(id, RegName.values()[ord], 4); // TODO 不知道要不要lb
            id.pointer = r;
          } else {
            genConstAt(irarg, RegName.values()[ord]);
          }
          ++ord;
        }
      } else {
        // 全部栈传入，从-8(sp)开始传，因为-4(sp)是ra位
        int offset = -8;
        for (var irarg : call.args) {
          if (asmId.idMap.containsKey(irarg)) {
            asmId arg = getAsmId(irarg);
            RegName r = arg.pointer;
            arg.pointer = null;
            genLoad(arg, 4); // 将本函数要传入的值Load进a0
            arg.pointer = r;
          } else {
            // const
            genConst(irarg);
          }
          genStore(new asmId(offset, sp), 4);
          offset -= 4;
        }
      }
      asmIns asmCall = new asmIns(call, asmIns.OpType.call);
      asmId label = new asmId(AsmType.Label, null);
      label.info = call.callName;
      asmCall.setCall(label);
      curBlock.instructions.add(asmCall);
      if (call.result.valueType.type != IRType.Void) {
        // 返回值在a0里
        asmId res = newId(call.result, sp);
        genStore(res, 4);
        if (call.builtin && call.result.valueType.dimension > 0) {
          res.pointer = s1;
          // res.pointToHeap = true;
        }
      }
      return;
    }
    case Getelementptr: {
      getelementptrIns get = (getelementptrIns)irins;
      if (get.indices.size() == 1) {
        // 有可能分配到堆上内容哦？不如说一定会呢
        // 似乎只对new出的数组有效
        // 但愿string s="true"是常量，不会被length

        // lw a0, 24(sp)
        asmId baseId = getAsmId(get.objectPtr);

        var r = baseId.pointer;
        baseId.pointer = null;
        genLoad(baseId, 4);
        baseId.pointer = r;
        /*
         * 这一步，a0相当于malloc返回值
         */
        // addi a0 offset, a0
        /*
         * 这一步之后，a0是那个元素的地址
         */
        int offset = 0;
        assert (get.result.valueType.theClass == null);

        if (get.indices.get(0).type == IdType.Constant) {
          offset = get.result.valueType.getDeref().sizeof() *
                   get.indices.get(0).constValue;
          asmIns addi = new asmIns(null, asmIns.OpType.addi);
          addi.setArithImm(asmIns.OpType.addi, regi(a0), new asmId(offset),
                           regi(a0));
          curBlock.instructions.add(addi);
        } else {
          // 是一指针
          // li a2, sizeof
          // lw a1, index
          // mul a1 a1, a2
          // add a0 a0, a1
          asmIns li = new asmIns(null, asmIns.OpType.li);
          li.setLi(new asmId(get.result.valueType.getDeref().sizeof()),
                   regi(a2));
          curBlock.instructions.add(li);

          asmId indexId = getAsmId(get.indices.get(0));
          genLoadAt(indexId, a1, 4);
          // TODO 不知道是不是堆上的，故不知道要不要在这里sizeof
          asmIns mul = new asmIns(null, asmIns.OpType.mul);
          mul.setFull(regi(a1), regi(a1), regi(RegName.a2));
          curBlock.instructions.add(mul);

          asmIns addIns = new asmIns(null, asmIns.OpType.add);
          addIns.setFull(regi(a0), regi(a0), regi(a1));
          curBlock.instructions.add(addIns);
        }
        // 到这一步，a0存储了指向那个元素的地址（也即和objectPtr是一个级别）
        asmId destptr = newId(get.result, sp); // 基底是sp

        // sw a0 resultPtr
        destptr.space = get.result.valueType.getDeref()
                            .sizeof(); // TODO 是dest还是destptr的？
        genStore(destptr, 4);
        if (!baseId.alloca) {   // TODO 史山
          destptr.pointer = s1; // 下次可以直接0(s1)做地址
          // destptr.pointToHeap = true;
          destptr.pointToHeap =
              get.objectPtr.valueType.getDeref().dimension > 0;
          // 做的时候倒是不用动用s1，之后再说
        }
      } else {
        if (get.objectPtr.valueType.getDeref().arrayLength != -1) {
          // lui a0, %hi; addi a0, a0, %lo; genstore(dest, dest.space)
          // 有一次似乎没用？只要一次就好了
          genLoad(getAsmId(get.objectPtr), 4);
          asmId dest = newId(get.result, sp); // space=4
          genStore(dest, dest.space);
        } else {
          // TODO 最难点
          /*
           * 第一个i32 0指的是堆上lw s1,12(sp)；lw a0, 0(s1)；跟一个index时一致
           * 第二个i32 i指的是根据asmClass推导的指针偏移量，即addi
           * 现阶段假设所有的类都能对齐
           */
          assert get.indices.size() == 2;
          assert get.objectPtr.valueType.theClass != null;
          irClass irc = get.objectPtr.valueType.theClass;
          // lw a0,12(sp)；
          asmId baseId = getAsmId(get.objectPtr);
          var r = baseId.pointer;
          baseId.pointer = null;
          genLoad(baseId, 4);
          baseId.pointer = r;

          // addi a0, a0, offset
          assert get.indices.get(1).type == IdType.Constant;
          asmClass cls = world.classes.get(irc.name);
          int offset = cls.offsets.get(get.indices.get(1).constValue);

          asmIns addi = new asmIns(null, asmIns.OpType.addi);
          addi.setArithImm(asmIns.OpType.addi, regi(a0), new asmId(offset),
                           regi(a0));
          curBlock.instructions.add(addi);

          // sw a0 resultPtr
          // 如果result也是指针，两级指针会重合
          asmId destptr = newId(get.result, sp);
          destptr.space = get.result.valueType.getDeref().sizeof();
          genStore(destptr, destptr.space);
          if (!baseId.alloca) {
            destptr.pointer = s1;
            destptr.pointToHeap = get.result.valueType.getDeref().dimension > 0;
            // destptr.classPointToHeap = get.result.valueType.dimension > 0;
          }
        }
      }
      return;
    }
    case Icmp: {
      icmpIns icmp = (icmpIns)irins;
      // 同样，不需要考虑堆问题，不需要考虑i问题
      // 比较方式为做一减法
      asmId res = newId(icmp.result, sp);
      // 处理常数
      asmId rs1 = null, rs2 = null;
      irId lhs = icmp.lhs, rhs = icmp.rhs;

      if (lhs.type != IdType.Constant && rhs.type != IdType.Constant) {
        if (rhs.type == IdType.Null) {
          rs1=getAsmId(lhs);
          var r=rs1.pointer;
          rs1.pointer=null;
          genLoadAt(rs1, a1, 4);
          rs1.pointer=r;
          asmIns li=new asmIns(null, asmIns.OpType.li);
          li.setLi(new asmId(0),regi(a2));
          curBlock.instructions.add(li);
        } else {
          rs1 = getAsmId(lhs);
          rs2 = getAsmId(rhs);
          genLoadAt(rs1, a1, 4);
          genLoadAt(rs2, a2, 4);
        }
      } else if (lhs.type == IdType.Constant && rhs.type != IdType.Constant) {
        rs2 = getAsmId(rhs);
        genConstAt(lhs, a1);
        genLoadAt(rs2, a2, 4);
      } else if (lhs.type != IdType.Constant && rhs.type == IdType.Constant) {
        rs1 = getAsmId(lhs);
        genLoadAt(rs1, a1, 4);
        genConstAt(rhs, a2);
      } else if (lhs.type == IdType.Constant && rhs.type == IdType.Constant) {
        genConstAt(lhs, a1);
        genConstAt(rhs, a2);
      }

      // 先做一减法，值放在a0
      asmIns comp = new asmIns(null, asmIns.OpType.sub);
      comp.rd = regi(a0);
      comp.rs1 = regi(a1);
      comp.rs2 = regi(a2);
      curBlock.instructions.add(comp);

      if (icmp.op == icmpIns.OpType.Sge || icmp.op == icmpIns.OpType.Sle) {
        // seqz a1,a0
        // sltz||sgtz a2,a0
        // or a0 a1,a2
        asmIns seqz = new asmIns(null, asmIns.OpType.seqz),
               stz = new asmIns(null, icmp.op == icmpIns.OpType.Sge
                                          ? asmIns.OpType.sgtz
                                          : asmIns.OpType.sltz),
               orins = new asmIns(null, asmIns.OpType.or);
        seqz.setSz(regi(a1), regi(a0));
        stz.setSz(regi(a2), regi(a0));
        orins.setFull(regi(a0), regi(a1), regi(a2));
        curBlock.instructions.add(seqz);
        curBlock.instructions.add(stz);
        curBlock.instructions.add(orins);
      } else {
        // seqz||snez||sltz||sgtz a0, a0
        asmIns.OpType op;
        switch (icmp.op) {
        case Eq:
          op = asmIns.OpType.seqz;
          break;
        case Ne:
          op = asmIns.OpType.snez;
          break;
        case Sgt:
          op = asmIns.OpType.sgtz;
          break;
        case Slt:
          op = asmIns.OpType.sltz;
          break;
        default:
          throw new RuntimeException();
        }
        asmIns ins = new asmIns(null, op);
        ins.setSz(regi(a0), regi(a0));
        curBlock.instructions.add(ins);
      }

      genStore(res, 4);
      return;
    }
    case Load: {
      loadIns load = (loadIns)irins;
      asmId res = asmId.idMap.containsKey(load.result) ? getAsmId(load.result)
                                                       : newId(load.result, sp);
      asmId addr = getAsmId(load.loadAddr);
      genLoad(addr, addr.space);
      genStore(res, 4);
      if (addr.pointToHeap)
        res.pointer = s1;
      return;
    }
    case Phi:
      // nan
      break;
    case Ret: {
      retIns ret = (retIns)irins;
      var insType = ret.result.valueType;
      asmIns re = new asmIns(null, asmIns.OpType.ret);
      if (insType.type != IRType.Void) {
        // 返回值加载进a0
        asmId retVal = getAsmId(ret.retValue);
        var r = retVal.pointer;
        retVal.pointer = null;
        genLoad(retVal, retVal.space); // retVal.ir.valueType.sizeof()
        retVal.pointer = r;
      }
      curBlock.instructions.add(re);
      return;
    }
    case Select:
      // nan
      break;
    case Store: { // store语句不会引入新虚拟寄存器
      storeIns store = (storeIns)irins;
      // storeIns的result保存要存储的事物类型
      asmId addr = getAsmId(store.storeAddr);
      // storeAddr控制是否堆指针，storeValue没有这个权利

      if (store.storeValue.type == IdType.Constant) {
        // li a0, imm; sw/sb a0, addr
        genConst(store.storeValue);
        genStore(addr, addr.space);
      } else {
        // lw/lb a0, val; sw/sb a0, addr
        asmId val = getAsmId(store.storeValue);
        if (val == null) {
          asmIns li = new asmIns(null, asmIns.OpType.li);
          li.setLi(new asmId(0), regi(a0));
          curBlock.instructions.add(li);
        } else {
          RegName r = val.pointer;
          val.pointer = null;
          genLoad(val, addr.space);
          val.pointer = r;
        }

        genStore(addr, addr.space);

        addr.pointToHeap = (val != null && val.pointer != null);
      }
      return;
    }
    default:
      throw new RuntimeException();
    }
  }

  //#region tools

  // load address to reg, just use a0
  public void genLoad(asmId address, int size) {
    if (address.asmType == AsmType.Global) {
      asmId hi = new asmId(AsmType.Global, null),
            lo = new asmId(AsmType.Global, null);
      hi.setGlobalPart(address, false, a0);
      lo.setGlobalPart(address, true, a0);

      asmIns lui = new asmIns(null, asmIns.OpType.lui);
      lui.setLi(hi, regi(a0));
      curBlock.instructions.add(lui);

      if (address.isString) {
        asmIns addi = new asmIns(null, asmIns.OpType.addi);
        addi.setArithImm(asmIns.OpType.addi, regi(a0), lo, regi(a0));
        curBlock.instructions.add(addi);
      } else {
        asmIns load = new asmIns(null, asmIns.OpType.lw);
        load.setL(lo, regi(a0));
        curBlock.instructions.add(load);
      }
    } else {
      asmIns.OpType op = size == 4 ? asmIns.OpType.lw : asmIns.OpType.lb;
      if (address.pointer != null) {
        asmIns ins = new asmIns(null, asmIns.OpType.lw);
        ins.setL(address, regi(address.pointer));
        curBlock.instructions.add(ins);
        asmId deref = address.getDeref();
        asmIns extra = new asmIns(null, op);
        extra.setL(deref, regi(a0));
        curBlock.instructions.add(extra);
      } else {
        asmIns ins = new asmIns(null, op);
        ins.setL(address, regi(a0));
        curBlock.instructions.add(ins);
      }
    }
  }

  public void genLoadAt(asmId address, RegName reg, int size) {
    if (address.asmType == AsmType.Global) {
      asmId hi = new asmId(AsmType.Global, null),
            lo = new asmId(AsmType.Global, null);
      hi.setGlobalPart(address, false, reg);
      lo.setGlobalPart(address, true, reg);

      asmIns lui = new asmIns(null, asmIns.OpType.lui);
      lui.setLi(hi, regi(reg));
      curBlock.instructions.add(lui);

      if (address.isString) {
        asmIns addi = new asmIns(null, asmIns.OpType.addi);
        addi.setArithImm(asmIns.OpType.addi, regi(a0), lo, regi(a0));
        curBlock.instructions.add(addi);
      } else {
        asmIns load = new asmIns(null, asmIns.OpType.lw);
        load.setL(lo, regi(a0));
        curBlock.instructions.add(load);
      }
    } else {
      asmIns.OpType op = size == 4 ? asmIns.OpType.lw : asmIns.OpType.lb;
      if (address.pointer != null) {
        asmIns ins = new asmIns(null, asmIns.OpType.lw);
        ins.setL(address, regi(address.pointer));
        curBlock.instructions.add(ins);
        asmId deref = address.getDeref();
        asmIns extra = new asmIns(null, op);
        extra.setL(deref, regi(reg));
        curBlock.instructions.add(extra);
      } else {
        asmIns ins = new asmIns(null, op);
        ins.setL(address, regi(reg));
        curBlock.instructions.add(ins);
      }
    }
  }

  // just use a0
  public void genStore(asmId address, int size) {
    if (address.asmType == AsmType.Global) {
      asmId hi = new asmId(AsmType.Global, null),
            lo = new asmId(AsmType.Global, null);
      hi.setGlobalPart(address, false, a1);
      lo.setGlobalPart(address, true, a1);

      asmIns lui = new asmIns(null, asmIns.OpType.lui);
      lui.setLi(hi, regi(a1));
      curBlock.instructions.add(lui);

      asmIns.OpType op =
          address.space == 4 ? asmIns.OpType.sw : asmIns.OpType.sb;
      if (address.pointer != null) {
        asmIns extra = new asmIns(null, asmIns.OpType.lw);
        extra.setL(address, regi(address.pointer));
        curBlock.instructions.add(extra);
        asmId deref = address.getDeref();
        asmIns ins = new asmIns(null, op);
        ins.setS(deref, regi(a0));
        curBlock.instructions.add(ins);
      } else {
        asmIns store = new asmIns(null, op);
        store.setS(lo, regi(a0));
        curBlock.instructions.add(store);
      }

    } else {
      asmIns.OpType op = size == 4 ? asmIns.OpType.sw : asmIns.OpType.sb;
      if (address.pointer != null) {
        // 由于存到堆里，故不要下面多的那一步
        asmIns extra = new asmIns(null, asmIns.OpType.lw);
        extra.setL(address, regi(address.pointer));
        curBlock.instructions.add(extra);
        asmId deref = address.getDeref();
        asmIns ins = new asmIns(null, op);
        ins.setS(deref, regi(a0));
        curBlock.instructions.add(ins);
      } else {
        asmIns ins = new asmIns(null, op);
        if (size != 4)
          if (address.reg == sp) {
            asmIns setter = new asmIns(null, asmIns.OpType.sw);
            setter.setS(address, regi(RegName.zero));
            curBlock.instructions.add(setter);
          }
        ins.setS(address, regi(a0));
        curBlock.instructions.add(ins);
      }
    }
  }

  public void genStoreFrom(asmId address, RegName reg, int size) {
    if (address.asmType == AsmType.Global) {
      asmId hi = new asmId(AsmType.Global, null),
            lo = new asmId(AsmType.Global, null);
      hi.setGlobalPart(address, false, a0);
      lo.setGlobalPart(address, true, a0);

      asmIns lui = new asmIns(null, asmIns.OpType.lui);
      lui.setLi(hi, regi(a0));
      curBlock.instructions.add(lui);

      asmIns.OpType op =
          address.space == 4 ? asmIns.OpType.sw : asmIns.OpType.sb;
      if (address.pointer != null) {
        asmIns extra = new asmIns(null, asmIns.OpType.lw);
        extra.setL(address, regi(address.pointer));
        curBlock.instructions.add(extra);
        asmId deref = address.getDeref();
        asmIns ins = new asmIns(null, op);
        ins.setS(deref, regi(reg));
        curBlock.instructions.add(ins);
      } else {
        asmIns store = new asmIns(null, op);
        store.setS(lo, regi(reg));
        curBlock.instructions.add(store);
      }
    } else {
      asmIns.OpType op = size == 4 ? asmIns.OpType.sw : asmIns.OpType.sb;
      if (address.pointer != null) {
        // 由于存到堆里，故不要下面多的那一步
        asmIns extra = new asmIns(null, asmIns.OpType.lw);
        extra.setL(address, regi(address.pointer));
        curBlock.instructions.add(extra);
        asmId deref = address.getDeref();
        asmIns ins = new asmIns(null, op);
        ins.setS(deref, regi(reg));
        curBlock.instructions.add(ins);
      } else {
        asmIns ins = new asmIns(null, op);
        if (size != 4)
          if (address.reg == sp) {
            asmIns setter = new asmIns(null, asmIns.OpType.sw);
            setter.setS(address, regi(RegName.zero));
            curBlock.instructions.add(setter);
          }
        ins.setS(address, regi(reg));
        curBlock.instructions.add(ins);
      }
    }
  }

  public asmId regi(RegName r) { return regs.get(r.ordinal()); }

  // generate a1 stored const: li a0, imm
  public void genConst(irId ir) {
    assert ir.type == IdType.Constant;
    if (ir.constValue > (1 << 15)) {
      int high = ir.constValue >> 12, low = ir.constValue & ((1 << 12) - 1);
      if (low > 2047) {
        low -= 4096;
        high++;
      }
      asmIns lui = new asmIns(null, asmIns.OpType.lui);
      lui.setLi(new asmId(high), regi(a0));
      asmIns addi = new asmIns(null, asmIns.OpType.addi);
      addi.setArithImm(asmIns.OpType.addi, regi(a0), new asmId(low), regi(a0));
      curBlock.instructions.add(lui);
      curBlock.instructions.add(addi);
    } else {
      asmIns ins = new asmIns(null, asmIns.OpType.li);
      ins.setLi(new asmId(ir.constValue), regi(a0));
      curBlock.instructions.add(ins);
    }
  }

  // li reg, imm
  public void genConstAt(irId ir, RegName reg) {
    // lui逻辑左移12位，addi加12位
    assert ir.type == IdType.Constant;
    if (ir.constValue > (1 << 15)) {
      int high = ir.constValue >> 12, low = ir.constValue & ((1 << 12) - 1);
      if (low > 2047) {
        low -= 4096;
        high++;
      }
      asmIns lui = new asmIns(null, asmIns.OpType.lui);
      lui.setLi(new asmId(high), regi(reg));
      asmIns addi = new asmIns(null, asmIns.OpType.addi);
      addi.setArithImm(asmIns.OpType.addi, regi(reg), new asmId(low),
                       regi(reg));
      curBlock.instructions.add(lui);
      curBlock.instructions.add(addi);
    } else {
      asmIns ins = new asmIns(null, asmIns.OpType.li);
      ins.setLi(new asmId(ir.constValue), regi(reg));
      curBlock.instructions.add(ins);
    }
  }

  public asmId newId(irId ir, RegName base) {
    asmId ret = new asmId(AsmType.Address, ir);
    ret.setAddress(curFunc.stack.push_low(ret),
                   base); // ir.valueType.sizeof()
    ret.size = 4;
    if (ir != null) {
      ir.refTime--;
      occured.add(ir);
    }
    return ret;
  }

  //#endregion
}
