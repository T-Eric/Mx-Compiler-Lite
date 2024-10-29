package midend.llvm_ir;

import ast.*;
import ast.ExprNodes.*;
import ast.ExprNodes.ExprNode.ExprType;
import ast.ExprNodes.ExprNode.OpType;
import ast.StmtNodes.*;
import ast.StmtNodes.StmtNode.StmtType;
import ast.atomNode.AtomType;
import ast.programPartNode.PartType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import midend.llvm_ir.irassets.irId;
import midend.llvm_ir.irassets.irId.IdType;
import midend.llvm_ir.irassets.irType;
import midend.llvm_ir.irassets.irType.IRType;
import midend.llvm_ir.irassets.statements.*;
import midend.llvm_ir.irassets.statements.instructions.*;
import org.antlr.v4.runtime.misc.Pair;
import utility.Type.ASTType;
import utility.error.internalError;
import utility.position;

public class irBuilder implements ASTVisitor {
  public irWorld world;
  public programNode program;

  public Stack<HashMap<String, irId>> scopeStack =
      new Stack<>(); // store variables of a scope
  public Map<String, String> builtinFuncNames;
  public Stack<irBlock> condStack = new Stack<>();
  public Stack<irBlock> gotoStack = new Stack<>(); // for while

  irBlock curBlock = null;
  irFunc curFunc = null;
  irClass curClass = null;

  boolean hasDeclared = false;
  int constStringCount = 0;

  String ClassPrefix = "class.", GlobalPrefix = "global.";

  public irBuilder(programNode program) {
    this.program = program;
    world = new irWorld();
    scopeStack.push(new HashMap<>()); // globalscope
    init();
  }

  @Override
  public void visit(programNode it) {
    for (var pt : it.parts) {
      if (pt.partType == PartType.ClassDef) {
        var cls = new irClass();
        cls.builtIn = pt.classDef.builtin;
        cls.name = ClassPrefix + pt.classDef.name;
        world.classes.put(cls.name, cls);
      }
    }
    for (var pt : it.parts)
      if (pt.partType == PartType.ClassDef || pt.partType == PartType.FuncDef)
        pt.accept(this);
    hasDeclared = true;
    it.parts.forEach(pt -> pt.accept(this));
  }

  @Override
  public void visit(programPartNode it) {
    switch (it.partType) {
    case ClassDef:
      it.classDef.accept(this);
      break;
    case FuncDef:
      it.funcDef.accept(this);
      break;
    case VarDef:
      it.varDef.accept(this);
    }
  }

  @Override
  public void visit(funcDefNode it) {
    boolean isMethod = curClass != null;

    if (hasDeclared) {
      // skip builtin
      if (it.builtin)
        return;
      irFunc func = null;
      if (isMethod)
        func = it.type == null ? curClass.constructor
                               : curClass.methods.get(it.name);
      else
        func = world.functions.get(it.name);
      curFunc = func;
      // set new scope
      HashMap<String, irId> scp = new HashMap<>();
      /// if ismethod remember adding this
      /// add args into scope using alloca and store
      /// use alloca to avoid modifying original values
      irBlock blk = new irBlock();
      func.blocks.add(blk);
      curBlock = blk;
      if (isMethod)
        scp.put("this", func.args.get(0));
      // this ptr is 'reference', but others are temporary variables
      if (it.argList != null) {
        for (int cnt = 0; cnt < it.argList.types.size(); ++cnt) {
          var arg = func.args.get(isMethod ? cnt + 1 : cnt);
          allocaIns ai = new allocaIns(arg.valueType);
          storeIns si = new storeIns(arg.valueType);
          si.storeAddr = ai.result;
          si.storeValue = arg;
          curBlock.instructions.add(ai);
          curBlock.instructions.add(si);
          scp.put(it.argList.identifiers.get(cnt), ai.result);
        }
      }
      scopeStack.push(scp);
      // in main call void _init()
      if (func.name == "main") {
        callIns ci = new callIns(new irType(IRType.Void));
        ci.callName = builtinFuncNames.get("_init");
        ci.args = new ArrayList<>();
        curBlock.instructions.add(ci);
      }
      // build return block
      var retBlock = new irBlock();
      var retInst = new retIns(func.retType);
      func.retBlock = retBlock;
      // 直接返回局部变量或参数都可行，但是全局变量则不行
      if (func.retType.type != IRType.Void) {
        func.retValPtr = genAlloca(func.retType);
        loadIns loadRetValueIns = new loadIns(func.retType);
        loadRetValueIns.loadAddr = func.retValPtr;
        retBlock.instructions.add(loadRetValueIns);
        retInst.retValue = loadRetValueIns.result;
      } else {
        retInst.retValue = new irId(func.retType);
      }
      retBlock.terminal = retInst;
      // build suite
      it.suite.accept(this);
      // jump to return block
      genBr(retBlock, true);
      func.blocks.add(retBlock);
      scopeStack.pop();
    } else {
      var func = new irFunc();
      func.builtIn = it.builtin;
      func.retType = new irType(it.type, world);

      if (func.builtIn)
        func.name = builtinFuncNames.get(it.name);
      else if (isMethod)
        func.name = curClass.name + "." + it.name;
      else if (it.name.equals("main"))
        func.name = "main";
      else
        func.name = GlobalPrefix + it.name;

      // declare args
      if (isMethod) {
        func.theClass = curClass;
        func.args.add(new irId(new irType(curClass)));
      }
      if (it.argList != null) {
        for (var at : it.argList.types)
          func.args.add(new irId(new irType(at, world)));
      }

      if (isMethod)
        curClass.methods.put(it.name, func);
      else
        world.functions.put(it.name, func);
    }
  }

  @Override
  public void visit(argListNode it) {
    // no need
  }

  @Override
  public void visit(varDefNode it) {
    // get the scope(class, function)
    if (scopeStack.size() == 1) {
      // global value
      curFunc = world.functions.get(builtinFuncNames.get("_init"));
      curBlock = curFunc.blocks.get(curFunc.blocks.size() - 1); // ! toRevise
    }
    // declare variables
    irType varType = new irType(it.type, world);
    for (var vt : it.varTerms) {
      vt.valueType = varType;
      vt.accept(this);
    }
  }

  @Override
  public void visit(varTermNode it) {
    irIns ins = null;
    if (scopeStack.size() == 1) {
      // global value
      ins = new globalVarIns();
      ins.result = new irId(it.valueType.getPtr(), GlobalPrefix + it.name);
      // add to world and scopeStack
      world.defines.add(ins);
      scopeStack.get(0).put(it.name, ins.result);
    } else {
      // member value
      ins = new allocaIns(it.valueType);
      curBlock.instructions.add(ins);
      // optimize: default zero
      if (it.valueType.type == IRType.I32 && !it.valueType.isArray())
        genPtrAssign(genI32Const(0), ins.result);
      scopeStack.peek().put(it.name, ins.result);
    }
    // assignment
    if (it.assignExpr != null) {
      it.assignExpr.accept(this);
      // t18出现了赋值为null时assignExpr.value的类型错误（原来是未定义的
      // 要修改成这个class的类型
      if (it.assignExpr.value.type == IdType.Null)
        it.assignExpr.value.valueType = ins.result.valueType.getDeref();
      genPtrAssign(it.assignExpr.value, ins.result);
    }
  }

  @Override
  public void visit(classDefNode it) {
    var cls = world.classes.get(ClassPrefix + it.name);

    if (hasDeclared) {
      // define the constructor and methods
      curClass = cls;
      for (var md : it.methodDefs)
        md.accept(this);
      curClass = null;
      if (cls.constructor.blocks.isEmpty()) {
        // constructor not implemented because that it's empty
        var block = new irBlock();
        block.terminal = new retIns(new irType(IRType.Void));
        cls.constructor.blocks.add(block);
      }
    } else {
      // declare member variables
      int cnt = 0;
      for (var vd : it.varDefs) {
        var type = new irType(vd.type, world);
        for (var vt : vd.varTerms) {
          cls.vars.put(vt.name, new Pair<>(cnt++, type));
        }
      }

      // whether itself has constructor
      curClass = cls;
      funcDefNode maybe_constructor = null;
      for (var md : it.methodDefs) {
        if (md.type != null)
          md.accept(this);
        else
          maybe_constructor = md;
      }

      // declare constructor
      cls.constructor = new irFunc();
      cls.constructor.name = ClassPrefix + it.name + "." + it.name;
      cls.constructor.theClass = cls;
      cls.constructor.retType = new irType(IRType.Void);
      cls.constructor.args.add(new irId(new irType(cls))); // this ptr
      if (maybe_constructor != null && maybe_constructor.argList != null) {
        // add extra args into constructor
        for (var at : maybe_constructor.argList.types) {
          cls.constructor.args.add(new irId(new irType(at, world)));
        }
      }
      curClass = null;
      // we only declare! now isn't the time to really define it
    }
  }

  @Override
  public void visit(suiteNode it) {
    scopeStack.push(new HashMap<>());
    for (var stmt : it.stmts)
      stmt.accept(this);
    scopeStack.pop();
  }

  @Override
  public void visit(typeNode it) {
    // no need, already has constructor from typeNode to irType
  }

  @Override
  public void visit(exprListNode it) {
    // no need
  }

  @Override
  public void visit(arrayBlockNode it) {
    // no need
  }

  @Override
  public void visit(arrayConstNode it) {
    // malloc_size=sizeof(int)+(sizeof(int))
    var i32Type = new irType(IRType.I32);
    var i8ptrType = new irType(IRType.I8);
    i8ptrType.dimension = 1;
    if (it.varType.dimension == 0) {
      it.value = new irId(IdType.Null);
      return;
    }
    irType basicType = null;
    switch (it.varType.type) {
    case Bool:
      basicType = new irType(IRType.I1);
      break;
    case String:
      basicType = new irType(IRType.I8);
      basicType.dimension = 1;
      break;
    case Int:
      basicType = new irType(IRType.I32);
      break;
    default:
      throw new internalError("WTH", new position(0, 0));
    }
    // if (it.isFirst) {
    //   irType ptrType = new irType(basicType);
    //   ptrType.dimension += it.varType.dimension; // %a=alloca ptr
    //   genAlloca(ptrType);
    // }
    if (it.varType.dimension == 1) {
      //分类讨论
      int generalSize = 0;
      for (var at : it.atoms) {
        at.accept(this);
        generalSize += at.value.valueType.sizeof();
      }
      var arrSize = it.atoms.size();
      irType thisType =
          it.atoms.get(0).value.valueType.getPtr(); // 本数组的类型

      var call = new callIns(i8ptrType);
      call.callName = builtinFuncNames.get("_new");
      call.builtin = true;
      call.args.add(genI32Const(generalSize + 4));
      curBlock.instructions.add(call);

      var cast = new bitcastIns(i32Type.getPtr());
      cast.castPtr = call.result;
      curBlock.instructions.add(cast);

      var store = new storeIns(i32Type);
      store.storeAddr = cast.result;
      store.storeValue = genI32Const(arrSize);
      curBlock.instructions.add(store);

      var getIns = new getelementptrIns();
      getIns.result = new irId(cast.result.valueType);
      getIns.objectPtr = cast.result;
      getIns.indices.add(genI32Const(1));
      curBlock.instructions.add(getIns);

      var castToNew = new bitcastIns(thisType);
      castToNew.castPtr = getIns.result;
      curBlock.instructions.add(castToNew);
      it.value = castToNew.result;

      // 赋值，store
      int i = 0;
      for (var at : it.atoms) {
        var getEntry = new getelementptrIns();
        getEntry.result = new irId(it.value.valueType);
        getEntry.objectPtr = it.value;
        getEntry.indices.add(genI32Const(i++));
        curBlock.instructions.add(getEntry);

        var storeToEntry = new storeIns(thisType.getDeref());
        storeToEntry.storeAddr = getEntry.result;
        storeToEntry.storeValue = at.value;
        curBlock.instructions.add(storeToEntry);
      }
    } else {
      // 指针的大小都是4
      irType thisType = new irType(basicType);
      thisType.dimension += it.varType.dimension;

      var call = new callIns(thisType);
      call.callName = builtinFuncNames.get("_new");
      call.builtin = true;
      call.args.add(genI32Const(4 * it.arrayConst.size() + 4));
      curBlock.instructions.add(call);

      var cast = new bitcastIns(i32Type.getPtr());
      cast.castPtr = call.result;
      curBlock.instructions.add(cast);

      var store = new storeIns(i32Type);
      store.storeAddr = call.result;
      store.storeValue = genI32Const(it.arrayConst.size());
      curBlock.instructions.add(store);

      var getIns = new getelementptrIns();
      getIns.result = new irId(cast.result.valueType);
      getIns.objectPtr = cast.result;
      getIns.indices.add(genI32Const(1));
      curBlock.instructions.add(getIns);

      var castToNew = new bitcastIns(thisType);
      castToNew.castPtr = getIns.result;
      curBlock.instructions.add(castToNew);
      it.value = castToNew.result;

      int i = 0;
      for (var ac : it.arrayConst) {
        ac.accept(this);
        if (ac.varType.type != ASTType.Null) {
          // 赋值
          var getEntry = new getelementptrIns();
          getIns.result = new irId(it.value.valueType);
          getEntry.objectPtr = it.value;
          getEntry.indices.add(genI32Const(i));
          curBlock.instructions.add(getEntry);

          var storeToEntry = new storeIns(thisType);
          storeToEntry.storeAddr = getEntry.result;
          storeToEntry.storeValue = ac.value;
          curBlock.instructions.add(storeToEntry);

          i++;
        }
      }
    }
    // if (it.isFirst) {
    //   // load
    //   it.value = getPtrVal(it.value);
    // }
  }

  @Override
  public void visit(atomNode it) {
    irId atomId = null;
    switch (it.atomType) {
    case This:
      atomId = curFunc.args.get(0);
      break;
    case Identifier:
      var id = it.identifier;

      for (int i = scopeStack.size() - 1; i >= 0 && atomId == null; --i)
        atomId = scopeStack.get(i).get(id);
      var cls = curFunc.theClass;
      if (atomId == null && cls != null) {
        // class member
        var member = cls.vars.get(id);
        var ins = new getelementptrIns();
        ins.objectPtr = curFunc.args.get(0);
        ins.indices.add(genI32Const(0));
        ins.indices.add(genI32Const(member.a));
        ins.result = new irId(member.b.getPtr());
        curBlock.instructions.add(ins);
        atomId = ins.result;
      }
      if (atomId == null)
        atomId = scopeStack.peek().get(id);
      assert (atomId != null);
      break;
    case StringConst:
      // 提前声明在顶端
      var strName = ".strconst." + constStringCount++;
      var len = it.stringConst.length();
      var str = it.stringConst.replaceAll("\\\\n", "\\\\0A")
                    .replaceAll("\\\\t", "\\\\09")
                    .replaceAll("\\\\\\\"", "\\\\22");
      var temp = str.replaceAll("\\\\\\\\", "123");
      var realLen = str.length() - 2 * (str.length() - len) -
                    (temp.length() - str.length());
      var globalDef = new globalVarIns();
      globalDef.globalVarInfo = str + "\\00";
      globalDef.result =
          new irId(new irType(IRType.I8, realLen + 1).getPtr(), strName);
      world.defines.add(globalDef);
      scopeStack.peek().put(strName, globalDef.result);

      var strType = new irType(IRType.I8);
      strType.dimension = 1; // i8*
      var ins = new getelementptrIns();
      ins.objectPtr = globalDef.result;
      ins.result = new irId(strType);
      ins.indices.add(genI32Const(0));
      ins.indices.add(genI32Const(0));
      curBlock.instructions.add(ins);
      atomId = ins.result;
      break;
    case DecimalInt:
      atomId = genI32Const(it.decimalInt);
      break;
    case Bool:
      atomId = new irId(new irType(IRType.I1), it.bool ? 1 : 0);
      break;
    case Null:
      atomId = new irId(IdType.Null);
      break;
    }
    it.value = atomId;
  }

  //#endregion

  //#region statement
  // assume already set curBlock
  // if,while,for,these introduces additional blocks
  // we implement like expanding current block,

  @Override
  public void visit(suiteStmtNode it) {
    it.suite.accept(this);
  }

  @Override
  public void visit(ifStmtNode it) {
    /*
     * 块都像链表，对于 b1,[b2],b3块，
     * 将其变为 b1,{cond,true,false,goto},b3块，
     * 其中cond中的语句可以直接接在b2后面，即设cond=curBlock
     * 而后进行扩展，记得在true和false块后添加去往b3的语句
     */
    // turn the terminal instruction to the goto block
    var formerBr = curBlock.terminal;
    // use curBlock as the original block
    irBlock gotoBlock = new irBlock();
    irBlock trueBlock = new irBlock();
    irBlock falseBlock = it.falseStmt == null ? gotoBlock : new irBlock();
    it.ifCondExpr.accept(this);
    var condId = it.ifCondExpr.value;
    genBr(condId, trueBlock, falseBlock);
    // get true and false blocks
    curBlock = trueBlock;
    curFunc.blocks.add(trueBlock);
    genBr(gotoBlock, false);

    if (it.trueStmt.type != StmtType.Suite)
      scopeStack.push(new HashMap<>());
    it.trueStmt.accept(this);
    if (it.trueStmt.type != StmtType.Suite)
      scopeStack.pop();

    if (it.falseStmt != null) {
      curBlock = falseBlock;
      curFunc.blocks.add(falseBlock);
      genBr(gotoBlock, false);

      if (it.falseStmt.type != StmtType.Suite)
        scopeStack.push(new HashMap<>());
      it.falseStmt.accept(this);
      if (it.falseStmt.type != StmtType.Suite)
        scopeStack.pop();
    }
    // get next block
    curBlock = gotoBlock;
    gotoBlock.terminal = formerBr;
    curFunc.blocks.add(gotoBlock);
  }

  @Override
  public void visit(whileStmtNode it) {
    /*
     * b1,{cond,todo,next}
     */
    var formerBr = curBlock.terminal;
    // create new cond and todo block
    var condBlock = new irBlock();
    var todoBlock = new irBlock();
    var gotoBlock = new irBlock();

    // goto cond block
    genBr(condBlock, true);
    curFunc.blocks.add(condBlock);
    it.condExpr.accept(this);
    var condId = it.condExpr.value;
    genBr(condId, todoBlock, gotoBlock);
    // get todo block: add cond and goto to stack
    curFunc.blocks.add(todoBlock);
    curBlock = todoBlock;
    genBr(condBlock, false);
    // must first genBr so that continue and break can revise it
    condStack.push(condBlock);
    gotoStack.push(gotoBlock);
    if (it.bodyStmt.type != StmtType.Suite)
      scopeStack.push(new HashMap<>());
    it.bodyStmt.accept(this);
    if (it.bodyStmt.type != StmtType.Suite)
      scopeStack.pop();
    condStack.pop();
    gotoStack.pop();
    // impl goto block
    curFunc.blocks.add(gotoBlock);
    curBlock = gotoBlock;
    gotoBlock.terminal = formerBr;
  }

  @Override
  public void visit(forStmtNode it) {
    var formerBr = curBlock.terminal;
    // added initialization compared to while
    var condBlock = new irBlock();
    var todoBlock = new irBlock();
    var stepBlock = new irBlock();
    var gotoBlock = new irBlock();
    // when condition it starts a new scope
    scopeStack.push(new HashMap<>());
    // init
    if (it.initWithVarDef != null) {
      if (it.initWithVarDef)
        it.initVarDef.accept(this);
      else
        it.initExpr.accept(this);
    }
    // cond
    genBr(condBlock, true);
    curFunc.blocks.add(condBlock);
    if (it.condExpr == null) {
      genBr(todoBlock, false);
    } else {
      it.condExpr.accept(this);
      var condId = it.condExpr.value;
      genBr(condId, todoBlock, gotoBlock);
    }
    // body
    curFunc.blocks.add(todoBlock);
    curBlock = todoBlock;
    genBr(stepBlock, false);
    condStack.push(stepBlock);
    gotoStack.push(gotoBlock);
    it.forBodyStmt.accept(this);
    condStack.pop();
    gotoStack.pop();
    // step
    curFunc.blocks.add(stepBlock);
    curBlock = stepBlock;
    genBr(condBlock, false);
    if (it.stepExpr != null)
      it.stepExpr.accept(this);
    // goto
    scopeStack.pop();
    curFunc.blocks.add(gotoBlock);
    curBlock = gotoBlock;
    gotoBlock.terminal = formerBr;
  }

  @Override
  public void visit(continueStmtNode it) {
    // change current br
    genBr(condStack.peek(), false);
  }

  @Override
  public void visit(breakStmtNode it) {
    genBr(gotoStack.peek(), false);
  }

  @Override
  public void visit(returnStmtNode it) {
    // store the return value to the retValPtr
    if (curFunc.retType.type != IRType.Void) {
      it.retExpr.accept(this);
      var retValue = it.retExpr.value;
      // originally: return b;
      // turn into: store i32 retValue, retValPtr; ret retValPtr;
      var st = new storeIns(curFunc.retType);
      // 不能retValue.valueType，因为curFunc知道null所属class类型但return语句不知道
      st.storeAddr = curFunc.retValPtr;
      st.storeValue = retValue;
      curBlock.instructions.add(st);
    }
    // goto retBlock
    genBr(curFunc.retBlock, false);
  }

  @Override
  public void visit(singleExprStmtNode it) {
    it.expr.accept(this);
  }

  @Override
  public void visit(varDefStmtNode it) {
    it.varDef.accept(this);
  }

  @Override
  public void visit(emptyStmtNode it) {
    // haha
  }

  //#endregion

  //#region expression

  @Override
  public void visit(parenExprNode it) {
    it.expr.isLvalue = it.isLvalue;
    it.expr.accept(this);
    it.value = it.expr.value;
  }

  @Override
  public void visit(atomExprNode it) {
    it.atom.accept(this);
    it.value = it.atom.value;
    if (!it.isLvalue && it.atom.atomType == AtomType.Identifier)
      it.value = getPtrVal(it.value);
  }

  @Override
  public void visit(memberExprNode it) {
    it.expr.accept(this);
    var objectPtr = it.expr.value; // %6
    /*
     * %6 = getelementptr %struct.B, ptr %3, i32 0, i32 1
     * %7 = getelementptr %struct.A, ptr %6, i32 0, i32 0
     */
    var ins = new getelementptrIns();
    ins.objectPtr = objectPtr;
    ins.indices.add(genI32Const(0)); // i32 0, not sure why, see above
    var v = objectPtr.valueType.theClass.vars.get(it.id);
    ins.indices.add(genI32Const(v.a));
    ins.result = new irId(v.b.getPtr()); // %7
    curBlock.instructions.add(ins);

    /*
     * 在解析类似A.func()时，会先按照funcNode解析
     * 所以对于这个调用是函数还是可以将其交给funcNode处理？
     * 认为来到这一步是默认其为member valuable调用
     */
    it.value = it.isLvalue ? ins.result : getPtrVal(ins.result);
  }

  @Override
  public void visit(arrayExprNode it) {
    /* code: return a+b[2][3];(in class A)
     * %6 = getelementptr inbounds nuw %struct.A, ptr %3, i32 0, i32 1
     * %7 = getelementptr inbounds [3 x [4 x i32]], ptr %6, i32 0, i32 2
     * %8 = getelementptr inbounds [4 x i32], ptr %7, i32 0, i32 3
     * %9 = load i32, ptr %8
     */
    // arrayPtr.valueType, arrayPtr
    // 决定分步生成getelementptr，不知为何indices开头一定要带一个i32,0
    // TODO
    it.nameExpr.isLvalue = true; // get its pointer
    it.nameExpr.accept(this);
    var arrayPtr = it.nameExpr.value; // %6

    // boolean nameIsNotLvalue = false;
    // if (it.nameExpr.exprType == ExprType.Paren &&
    //     ((parenExprNode)(it.nameExpr)).expr.exprType == ExprType.New)
    //   nameIsNotLvalue = true;
    for (var ar : it.arr) {
      //逐层往下取
      // if (nameIsNotLvalue) {
      //   nameIsNotLvalue = false;
      // } else
      arrayPtr = getPtrVal(arrayPtr);
      ar.expr.accept(this);
      var index = ar.expr.value;
      var ins = new getelementptrIns();
      ins.result = new irId(arrayPtr.valueType);
      ins.objectPtr = arrayPtr; // %6
      // ins.indices.add(genI32Const(0)); // i32 0, 不知道为什么，看着办先
      ins.indices.add(index);
      curBlock.instructions.add(ins);
      arrayPtr = ins.result;
    }
    /*
     * 对于连续的getel潮，比如cls.b[2][3](in class B)
     * 会选择getel取值到底，然后用load往外存取得的指针
     * load这一步交给谁做？load是在isLvalue=false时取出它的值
     * 是交给isLvalue?ptr:getPtrVal(ptr)做的
     * 所以中间只需要连续生成getel即可
     */
    if (!it.isLvalue && (arrayPtr.valueType.dimension == 0 ||
                         (arrayPtr.valueType.dimension == 1 &&
                          arrayPtr.valueType.type == IRType.I8))) {
      it.value = arrayPtr;
      return;
    }
    it.value = it.isLvalue ? arrayPtr : getPtrVal(arrayPtr);
  }

  @Override
  public void visit(funcExprNode it) {
    irFunc func = null;
    String funcName = it.funcExpr.exprType == ExprType.Member
                          ? ((memberExprNode)it.funcExpr).id
                          : ((atomExprNode)it.funcExpr).atom.identifier;
    irId objectPtr = null;
    var curClass = curFunc.theClass;

    if (it.funcExpr.exprType == ExprType.Member) {
      var objectVal = ((memberExprNode)it.funcExpr).expr;
      objectVal.accept(this);
      objectPtr = objectVal.value;
      if (objectPtr.valueType.isArray()) {
        if (funcName.equals("size") || funcName.equals("length")) {
          // 数组的长度存储在前头
          assert (it.args == null);
          var cast = new bitcastIns((new irType(IRType.I32)).getPtr()); // i32*
          cast.castPtr = objectPtr;
          curBlock.instructions.add(cast);
          var getel = new getelementptrIns();
          getel.result = new irId(cast.result.valueType);
          getel.objectPtr = cast.result;
          getel.indices.add(genI32Const(-1));
          curBlock.instructions.add(getel);
          it.value = getPtrVal(getel.result);
          return;
        } else
          func =
              world.classes.get(ClassPrefix + "_string").methods.get(funcName);
      } else
        func = objectPtr.valueType.theClass.methods.get(funcName);
    } else if (curClass != null && curClass.methods.get(funcName) != null) {
      func = curClass.methods.get(funcName);
      objectPtr = curFunc.args.get(0);
    } else {
      func = world.functions.get(funcName);
    }

    var ins = new callIns(func.retType);
    ins.func = func;
    ins.builtin = func.builtIn;
    ins.callName = func.name;
    if (objectPtr != null)
      ins.args.add(objectPtr);
    if (it.args != null)
      for (var expr : it.args.expressions) {
        expr.accept(this);
        ins.args.add(expr.value);
      }
    curBlock.instructions.add(ins);
    it.value = ins.result;
  }

  @Override
  public void visit(newExprNode it) {
    // 使用malloc后必须用bitcastIns转换成对应类型的空间，就像malloc前面要加上(指针类型)一样
    var strType = new irType(IRType.I8);
    strType.dimension = 1;
    var i32Type = new irType(IRType.I32);
    var newType = new irType(it.rightType, world);
    var arrayDim = newType.dimension;
    if (newType.type == IRType.Class || newType.type == IRType.I8)
      arrayDim--;

    if (arrayDim > 0) {
      if (it.arrayConst == null) {
        irId arrayPtr = null;
        irId midResult = null;
        // TODO
        if (it.isLvalue)
          midResult = genAlloca(newType);
        if (arrayDim == 1) {
          it.rightType.blocks.get(0).expr.accept(this);
          var arrSize = it.rightType.blocks.get(0).expr.value;
          var mulIns = new binaryIns(i32Type);
          mulIns.op = binaryIns.OpType.Mul;
          mulIns.lhs = genI32Const(newType.getDeref().sizeof());
          mulIns.rhs = arrSize;
          curBlock.instructions.add(mulIns);
          var addIns = new binaryIns(i32Type);
          addIns.op = binaryIns.OpType.Add;
          addIns.lhs = mulIns.result;
          addIns.rhs = genI32Const(4);
          curBlock.instructions.add(addIns);

          var call = new callIns(strType);
          call.callName = builtinFuncNames.get("_new");
          call.builtin = true;
          call.args.add(addIns.result);
          curBlock.instructions.add(call);

          var cast = new bitcastIns(i32Type.getPtr());
          cast.castPtr = call.result;
          curBlock.instructions.add(cast);

          var store = new storeIns(i32Type);
          store.storeAddr = cast.result;
          store.storeValue = arrSize;
          curBlock.instructions.add(store);

          var getIns = new getelementptrIns();
          getIns.result = new irId(cast.result.valueType);
          getIns.objectPtr = cast.result;
          getIns.indices.add(genI32Const(1));
          curBlock.instructions.add(getIns);

          var castToNew = new bitcastIns(newType);
          castToNew.castPtr = getIns.result;
          curBlock.instructions.add(castToNew);

          arrayPtr = castToNew.result;
        } else {
          // 多维数组，逐层malloc空间
          var call = new callIns(strType);
          call.callName = builtinFuncNames.get("_new_array");
          call.builtin = true;
          call.args.add(genI32Const(newType.sizeof()));
          call.args.add(genI32Const(arrayDim));
          // _new_array函数：传入单个元素大小、维数、该数组指针（头部已经存入大小）
          var alloca = new allocaIns(i32Type);
          alloca.allocaQuantity = genI32Const(arrayDim);
          curBlock.instructions.add(alloca);

          var ptr = alloca.result;
          var storePtr = ptr;
          int i;
          for (i = 0; i < arrayDim; ++i) {
            if (i != 0) {
              var getIns = new getelementptrIns();
              getIns.result = new irId(ptr.valueType);
              getIns.objectPtr = ptr;
              getIns.indices.add(genI32Const(i));
              curBlock.instructions.add(getIns);
              storePtr = getIns.result;
            }
            var store = new storeIns(i32Type);
            store.storeAddr = storePtr;

            var index = it.rightType.blocks.get(i).expr;
            if (index == null) {
              // 一路做下去，直到没有定义数组大小
              store.storeValue = genI32Const(0);
              curBlock.instructions.add(store);
              break;
            } else {
              index.accept(this);
              store.storeValue = index.value;
            }
            curBlock.instructions.add(store);
          }
          call.args.add(ptr);
          curBlock.instructions.add(call);
          // 转化回去
          var cast = new bitcastIns(newType);
          cast.castPtr = call.result;
          curBlock.instructions.add(cast);
          arrayPtr = cast.result;
        }
        if (it.isLvalue) {
          genPtrAssign(arrayPtr, midResult);
          it.value = midResult;
        } else
          it.value = arrayPtr;
      } else {
        it.arrayConst.isFirst = true;
        it.arrayConst.accept(this);
        it.value = it.arrayConst.value;
      }

    } else if (newType.type == IRType.Class) {
      irType classType = newType.getDeref();

      var call = new callIns(strType);
      call.callName = builtinFuncNames.get("_new");
      call.builtin = true;
      call.args.add(genI32Const(classType.sizeof()));
      curBlock.instructions.add(call);

      var cast = new bitcastIns(newType);
      cast.castPtr = call.result;
      curBlock.instructions.add(cast);

      var callConstructor = new callIns(new irType(IRType.Void));
      callConstructor.callName = classType.theClass.constructor.name;
      callConstructor.args.add(cast.result);
      curBlock.instructions.add(callConstructor);

      it.value = cast.result;
    }
  }

  @Override
  public void visit(selfExprNode it) {
    // suffix ++ --
    // %4 = original a pointer
    /* %5 = load i32, ptr %4, align 4, !dbg !71
     * %6 = add nsw i32 %5, 1, !dbg !71
     * store i32 %6, ptr %4, align 4, !dbg !71
     * ...
     * %11 = add nsw i32 %5, %10, !dbg !73
     */
    it.expr.isLvalue = true;
    it.expr.accept(this);
    var origPtr = it.expr.value;     // %4
    var midPtr = getPtrVal(origPtr); //%5
    var calcIns = new binaryIns(midPtr.valueType);
    calcIns.lhs = midPtr;
    calcIns.rhs = new irId(midPtr.valueType, 1);
    calcIns.op =
        it.op == OpType.Inc ? binaryIns.OpType.Add : binaryIns.OpType.Sub;
    curBlock.instructions.add(calcIns);
    genPtrAssign(calcIns.result, origPtr);
    it.value = midPtr;
  }

  @Override
  public void visit(unaryExprNode it) {
    // Inc,Dec
    if (it.op == OpType.Inc || it.op == OpType.Dec) {
      // alike, but
      /* %5 = load i32, ptr %4, align 4, !dbg !71
       * %6 = add nsw i32 %5, 1, !dbg !71
       * store i32 %6, ptr %4, align 4, !dbg !71
       * ...
       * %11 = add nsw i32 %6, %10, !dbg !73
       */
      it.rhsExpr.isLvalue = true;
      it.rhsExpr.accept(this);
      var origPtr = it.rhsExpr.value;  // %4
      var midPtr = getPtrVal(origPtr); // %5
      var calcIns = new binaryIns(midPtr.valueType);
      calcIns.lhs = midPtr;
      calcIns.rhs = new irId(midPtr.valueType, 1);
      calcIns.op =
          it.op == OpType.Inc ? binaryIns.OpType.Add : binaryIns.OpType.Sub;
      curBlock.instructions.add(calcIns);
      genPtrAssign(calcIns.result, origPtr);
      it.value = it.isLvalue ? origPtr : calcIns.result;
    } else {
      // Not,Tilde,Add,Sub
      it.rhsExpr.accept(this);
      var origPtr = it.rhsExpr.value;
      var calcIns = new binaryIns(origPtr.valueType);
      switch (it.op) {
      case Not:
        // !a=a^(-1)
        // assert (origPtr.valueType.type == IRType.I1);
        calcIns.lhs = origPtr;
        calcIns.rhs = new irId(origPtr.valueType, 1);
        calcIns.op = binaryIns.OpType.Xor;
        break;
      case Tilde:
        calcIns.lhs = origPtr;
        calcIns.rhs = new irId(origPtr.valueType, -1);
        calcIns.op = binaryIns.OpType.Xor;
        break;
      case Add:
        it.value = origPtr;
        return;
      case Sub:
        calcIns.lhs = new irId(origPtr.valueType, 0);
        calcIns.rhs = origPtr;
        calcIns.op = binaryIns.OpType.Sub;
        break;
      default:
        throw new internalError("WTH", new position(0, 0));
      }
      curBlock.instructions.add(calcIns);
      it.value = calcIns.result; // cannot be lvalue
    }
  }

  @Override
  public void visit(binaryExprNode it) {
    if (it.op == OpType.AndAnd || it.op == OpType.OrOr) {
      // 短路求值：从左到右求值，假如这个值已经是正确值则不再往下计算
      // 设计：把最大bool表达式做成只有And和Or的二叉树，后序遍历
      // 去除phi：用alloca和store
      boolean isAnd = it.op == OpType.AndAnd;
      var finalBlock = new irBlock();
      var nextBlock = new irBlock();
      var formerBr = curBlock.terminal;

      irType tp = new irType(IRType.I1);
      // var phi = new phiIns(tp);
      var alloca = new allocaIns(tp);
      curBlock.instructions.add(alloca);

      var exprs = new LinkedList<ExprNode>();
      ExprNode exprNode = it;
      while (exprNode.exprType == ExprType.Binary &&
             exprNode.op == (isAnd ? OpType.AndAnd : OpType.OrOr)) {
        exprs.addFirst(((binaryExprNode)exprNode).rhsExpr);
        exprNode = ((binaryExprNode)exprNode).lhsExpr;
      }
      exprs.addFirst(exprNode);

      for (int i = 0, len = exprs.size(); i < len; ++i) {
        if (i == 0) {
          exprs.get(i).accept(this);
          genBr(exprs.get(i).value, isAnd ? nextBlock : finalBlock,
                isAnd ? finalBlock : nextBlock);
          // phi.args.add(new Pair<irId, irId>(new irId(tp, isAnd ? 0 : 1),
          //                                   curBlock.label));
          genPtrAssign(new irId(tp, isAnd ? 0 : 1), alloca.result);
        } else if (i < len - 1) {
          curFunc.blocks.add(nextBlock);
          curBlock = nextBlock;
          nextBlock = new irBlock();
          exprs.get(i).accept(this);
          genBr(exprs.get(i).value, isAnd ? nextBlock : finalBlock,
                isAnd ? finalBlock : nextBlock);
          // phi.args.add(new Pair<irId, irId>(new irId(tp, isAnd ? 0 : 1),
          //                                   curBlock.label));
          genPtrAssign(new irId(tp, isAnd ? 0 : 1), alloca.result);
        } else {
          curFunc.blocks.add(nextBlock);
          curBlock = nextBlock;
          exprs.get(i).accept(this);
          // phi.args.add(
          //     new Pair<irId, irId>(exprs.get(i).value, curBlock.label));
          genPtrAssign(exprs.get(i).value, alloca.result);
          genBr(finalBlock, false);
        }
      }
      curFunc.blocks.add(finalBlock);
      curBlock = finalBlock;
      // curBlock.instructions.add(phi);
      curBlock.terminal = formerBr;
      // it.value = phi.result;
      it.value = getPtrVal(alloca.result);
      return;
    } else {
      it.lhsExpr.accept(this);
      it.rhsExpr.accept(this);
      var lhsValue = it.lhsExpr.value;
      var rhsValue = it.rhsExpr.value;
      // 考虑Eq和Ne在null的处理
      if (it.op == OpType.Eq || it.op == OpType.Ne) {
        if (lhsValue.type == IdType.Null || rhsValue.type == IdType.Null) {
          if (lhsValue.type == rhsValue.type) {
            it.value =
                new irId(new irType(IRType.I1), it.op == OpType.Eq ? 1 : 0);
            return;
          } else if (lhsValue.type == IdType.Null)
            lhsValue.valueType = rhsValue.valueType;
        }
      }

      if (lhsValue.valueType.isString()) {
        irType retType;
        if (it.op == OpType.Add)
          retType = new irType(IRType.I8).getPtr(); // return a
        else
          retType = new irType(IRType.I1);
        var call = new callIns(retType);
        call.builtin = true;
        switch (it.op) {
        case Add:
          call.callName = builtinFuncNames.get("_string_add");
          break;
        case Eq:
          call.callName = builtinFuncNames.get("_string_eq");
          break;
        case Ne:
          call.callName = builtinFuncNames.get("_string_ne");
          break;
        case Ge:
          call.callName = builtinFuncNames.get("_string_ge");
          break;
        case Geq:
          call.callName = builtinFuncNames.get("_string_geq");
          break;
        case Le:
          call.callName = builtinFuncNames.get("_string_le");
          break;
        case Leq:
          call.callName = builtinFuncNames.get("_string_leq");
          break;
        default:
          throw new internalError("WTH", new position(0, 0));
        }
        call.args.add(lhsValue);
        call.args.add(rhsValue);
        curBlock.instructions.add(call);

        it.value = call.result;
        return;
      } else {
        irType retType;
        if (it.op == OpType.Eq || it.op == OpType.Ne || it.op == OpType.Ge ||
            it.op == OpType.Geq || it.op == OpType.Le || it.op == OpType.Leq) {
          retType = new irType(IRType.I1);
          icmpIns.OpType op;
          switch (it.op) {
          case Eq:
            op = icmpIns.OpType.Eq;
            break;
          case Ne:
            op = icmpIns.OpType.Ne;
            break;
          case Ge:
            op = icmpIns.OpType.Sgt;
            break;
          case Geq:
            op = icmpIns.OpType.Sge;
            break;
          case Le:
            op = icmpIns.OpType.Slt;
            break;
          case Leq:
            op = icmpIns.OpType.Sle;
            break;
          default:
            throw new internalError("WTH", new position(0, 0));
          }
          icmpIns calc = new icmpIns(op, retType);
          calc.lhs = lhsValue;
          calc.rhs = rhsValue;
          curBlock.instructions.add(calc);
          it.value = calc.result;
        } else {
          retType = lhsValue.valueType;
          binaryIns.OpType op;
          switch (it.op) {
          case Div:
            op = binaryIns.OpType.Sdiv;
            break;
          case Mod:
            op = binaryIns.OpType.Srem;
            break;
          case Shr:
            op = binaryIns.OpType.Ashr;
            break;
          default:
            op = binaryIns.OpType.valueOf(it.op.toString());
          }
          binaryIns calc = new binaryIns(retType);
          calc.op = op;
          calc.lhs = lhsValue;
          calc.rhs = rhsValue;
          curBlock.instructions.add(calc);
          it.value = calc.result;
        }
        return;
      }
    }
  }

  @Override
  public void visit(ternaryExprNode it) {
    // code:
    /* int c=1,d=2;
     * bool v=true;
     * return v?c:d;
     */
    // gen: c=%3,d=%4,v=%5 as local variables, use phi
    /*
     * %4 = load i8, ptr %3
     * %5 = trunc i8 %4 to i1
     * // maybe turn it to %5 = load i1, ptr %3
     *
     * %6 = select i1 %5, i32* %1, i32* %2
     *
     * %7 = load i32, ptr %6
     */
    // 发现不能用select语句糊弄，还是乖乖做吧
    var formerBr = curBlock.terminal;
    it.condExpr.accept(this);
    irBlock trueBlock = new irBlock(), falseBlock = new irBlock(),
            gotoBlock = new irBlock(), originBlock = curBlock;
    genBr(it.condExpr.value, trueBlock, falseBlock);

    curFunc.blocks.add(trueBlock);
    curBlock = trueBlock;
    it.lhsExpr.accept(this);
    if (it.lhsExpr.value.type == IdType.Null)
      it.lhsExpr.value.valueType = new irType(IRType.I32);
    genBr(gotoBlock, false);
    irBlock trueTailBlock = curBlock;

    curFunc.blocks.add(falseBlock);
    curBlock = falseBlock;
    it.rhsExpr.accept(this);
    if (it.rhsExpr.value.type == IdType.Null)
      it.rhsExpr.value.valueType = new irType(IRType.I32);
    genBr(gotoBlock, false);
    irBlock falseTailBlock = curBlock;

    curFunc.blocks.add(gotoBlock);
    curBlock = gotoBlock;
    // 如果并没有返回值，那么不应当产生phi
    if (it.lhsExpr.value.valueType.type == IRType.Void) {
      curBlock.terminal = formerBr;
      it.value = new irId(new irType(IRType.Void));
    } else {
      // var phi = new phiIns();
      var alloca = new allocaIns(it.lhsExpr.value.valueType);
      originBlock.instructions.add(alloca);

      // default zero for i32er
      if (it.lhsExpr.value.valueType.type == IRType.I32 &&
          !it.lhsExpr.value.valueType.isArray()) {
        var ins = new storeIns(new irType(IRType.I32));
        ins.storeValue = genI32Const(0);
        ins.storeAddr = alloca.result;
        originBlock.instructions.add(ins);
      }

      // phi.args.add(new Pair<irId, irId>(it.lhsExpr.value, trueBlock.label));
      // phi.args.add(new Pair<irId, irId>(it.rhsExpr.value, falseBlock.label));
      curBlock = trueTailBlock;
      genPtrAssign(it.lhsExpr.value, alloca.result);
      curBlock = falseTailBlock;
      genPtrAssign(it.rhsExpr.value, alloca.result);
      // phi.result = new irId(it.lhsExpr.value.valueType);
      // curBlock.instructions.add(phi);
      curBlock = gotoBlock;
      curBlock.terminal = formerBr;
      it.value = getPtrVal(alloca.result);
    }
  }

  @Override
  public void visit(formatStrExprNode it) {
    /*
     * 策略：交替【直接生成StringConst Atom】和 call toString
     */
    irType strType = new irType(IRType.I8);
    strType.dimension = 1;
    ArrayList<irId> stringConsts = new ArrayList<>();
    ArrayList<irId> exprStrings = new ArrayList<>();
    for (var expr : it.innerExprs) {
      expr.accept(this);
      switch (expr.value.valueType.type) {
      case I1:
        var call = new callIns(strType);
        call.builtin = true;
        call.callName = builtinFuncNames.get("boolToString");
        call.args.add(expr.value);
        curBlock.instructions.add(call);
        exprStrings.add(call.result);
        break;
      case I32:
        var cal = new callIns(strType);
        cal.builtin = true;
        cal.callName = builtinFuncNames.get("toString");
        cal.args.add(expr.value);
        curBlock.instructions.add(cal);
        exprStrings.add(cal.result);
        break;
      case I8:
        // 一定已经是i8*
        exprStrings.add(expr.value);
        break;
      default:
        throw new internalError("WTF", new position(0, 0));
      }
    }
    // String部分，直接开头生成常量
    for (var sc : it.strings) {
      atomNode scNode = new atomNode(new position(0, 0));
      scNode.atomType = AtomType.StringConst;
      scNode.stringConst = sc;
      scNode.accept(this);
      stringConsts.add(scNode.value);
    }
    // 加法全加了
    var curStr = stringConsts.get(0);
    for (int i = 0; i < exprStrings.size(); ++i) {
      var call1 = new callIns(strType);
      call1.callName = builtinFuncNames.get("_string_add");
      call1.builtin = true;
      call1.args.add(curStr);
      call1.args.add(exprStrings.get(i));
      curBlock.instructions.add(call1);
      curStr = call1.result;

      var call2 = new callIns(strType);
      call2.callName = builtinFuncNames.get("_string_add");
      call2.builtin = true;
      call2.args.add(curStr);
      call2.args.add(stringConsts.get(i + 1));
      curBlock.instructions.add(call2);
      curStr = call2.result;
    }
    it.value = curStr;
  }

  @Override
  public void visit(arrayConstExprNode it) {
    it.arrayConst.isFirst = true;
    it.arrayConst.accept(this);
    it.value = it.arrayConst.value;
  }

  @Override
  public void visit(assignExprNode it) {
    it.lhsExpr.isLvalue = true; // wish lhsExpr give a pointer rather than value
    it.lhsExpr.accept(this);
    it.rhsExpr.accept(this);
    irId lValue = it.lhsExpr.value, rValue = it.rhsExpr.value;

    if (rValue.type == IdType.Null)
      rValue.valueType = lValue.valueType.getDeref();
    genPtrAssign(rValue, lValue);
    it.value = lValue; // continuous assignment
  }

  //#endregion

  //#region tools

  void init() {
    // give built-in names
    builtinFuncNames = Map.ofEntries(
        Map.entry("size", "_array_size"), Map.entry("print", "_print"),
        Map.entry("println", "_println"), Map.entry("printInt", "_printInt"),
        Map.entry("printlnInt", "_printlnInt"),
        Map.entry("getString", "_getString"), Map.entry("getInt", "_getInt"),
        Map.entry("toString", "_toString"),
        Map.entry("length", "_string_length"),
        Map.entry("substring", "_string_substring"),
        Map.entry("parseInt", "_string_parseInt"),
        Map.entry("ord", "_string_ord"),
        Map.entry(
            "_string_add",
            "_string_add"), // 注意这里应该是两个不同的键，但你的代码中出现了重复的键
        Map.entry("_string_eq", "_string_eq"), // 同上，这里应该是两个不同的键
        Map.entry("_string_ne", "_string_ne"), // 同上
        Map.entry("_string_leq", "_string_leq"),
        Map.entry("_string_le", "_string_le"),
        Map.entry("_string_geq", "_string_geq"),
        Map.entry("_string_ge", "_string_ge"), Map.entry("_init", "_init"),
        Map.entry("_new", "malloc"), Map.entry("_new_array", "_new_array"),
        Map.entry("boolToString", "_boolToString"));

    genDeclare(
        String.format("declare i8* @%s(i32)", builtinFuncNames.get("_new")));
    genDeclare(String.format("declare i8* @%s(i32, i32, i32*)",
                             builtinFuncNames.get("_new_array")));

    genDeclare(
        String.format("declare void @%s(i8*)", builtinFuncNames.get("print")));

    genDeclare(String.format("declare void @%s(i8*)",
                             builtinFuncNames.get("println")));

    genDeclare(String.format("declare void @%s(i32)",
                             builtinFuncNames.get("printInt")));

    genDeclare(String.format("declare void @%s(i32)",
                             builtinFuncNames.get("printlnInt")));

    genDeclare(
        String.format("declare i8* @%s()", builtinFuncNames.get("getString")));

    genDeclare(
        String.format("declare i32 @%s()", builtinFuncNames.get("getInt")));

    genDeclare(String.format("declare i8* @%s(i32)",
                             builtinFuncNames.get("toString")));

    genDeclare(String.format("declare i8* @%s(i8*, i8*)",
                             builtinFuncNames.get("_string_add")));

    genDeclare(String.format("declare i1 @%s(i8*, i8*)",
                             builtinFuncNames.get("_string_eq")));

    genDeclare(String.format("declare i1 @%s(i8*, i8*)",
                             builtinFuncNames.get("_string_ne")));

    genDeclare(String.format("declare i1 @%s(i8*, i8*)",
                             builtinFuncNames.get("_string_le")));

    genDeclare(String.format("declare i1 @%s(i8*, i8*)",
                             builtinFuncNames.get("_string_leq")));

    genDeclare(String.format("declare i1 @%s(i8*, i8*)",
                             builtinFuncNames.get("_string_ge")));

    genDeclare(String.format("declare i1 @%s(i8*, i8*)",
                             builtinFuncNames.get("_string_geq")));

    genDeclare(String.format("declare i8* @%s(i8*, i32, i32)",
                             builtinFuncNames.get("substring")));

    genDeclare(String.format("declare i32 @%s(i8*)",
                             builtinFuncNames.get("parseInt")));

    genDeclare(String.format("declare i32 @%s(i8*, i32)",
                             builtinFuncNames.get("ord")));

    genDeclare(String.format("declare i8* @%s(i1)",
                             builtinFuncNames.get("boolToString")));

    irFunc initFunc = new irFunc();
    initFunc.retType = new irType(IRType.Void);
    initFunc.name = "_init";
    var initBlock = new irBlock();
    initBlock.terminal = new retIns(initFunc.retType);
    initFunc.blocks.add(initBlock);
    world.functions.put(initFunc.name, initFunc);
  }

  irId genI8Const(int x) { return new irId(new irType(IRType.I8), x); }

  irId genI32Const(int x) { return new irId(new irType(IRType.I32), x); }

  void genDeclare(String decInfo) {
    var dec = new declareIns();
    dec.decInfo = decInfo;
    world.declares.add(dec);
  }

  void genDefine(String globalVarInfo) {
    var def = new globalVarIns();
    def.globalVarInfo = globalVarInfo;
    world.defines.add(def);
  }

  // assign right to left which is a ptr to memory
  void genPtrAssign(irId value, irId ptr) {
    var ins = new storeIns(value.valueType);
    ins.storeValue = value;
    ins.storeAddr = ptr;
    curBlock.instructions.add(ins);
  }

  irId genAlloca(irType type) {
    var ins = new allocaIns(type);
    curBlock.instructions.add(ins);

    // optim:为使得有初值不至于乱，为所有i32alloca强制赋值0
    if (type.dimension == 0 && type.type == IRType.I32 && !type.isArray())
      //赋予一个
      genPtrAssign(genI32Const(0), ins.result);

    return ins.result;
  }

  void genBr(irId cond, irBlock trueBlock, irBlock falseBlock) {
    var ins = new brIns();
    ins.cond = cond;
    ins.trueLabel = trueBlock.label;
    ins.falseLabel = falseBlock.label;
    curBlock.terminal = ins;
  }

  void genBr(irBlock block, boolean gotoBlock) {
    var ins = new brIns();
    ins.trueLabel = block.label;
    curBlock.terminal = ins;

    if (gotoBlock)
      curBlock = block; // as an absolutely goto statement
  }

  // %5 = load i32, ptr %4, receive %4, return %5
  irId getPtrVal(irId ptr) {
    var ins = new loadIns(ptr.valueType.getDeref());
    ins.loadAddr = ptr;
    curBlock.instructions.add(ins);
    return ins.result;
  }

  //#endregion
}
