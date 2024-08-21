package midend;

import ast.*;
import ast.ExprNodes.*;
import ast.StmtNodes.*;
import ast.programPartNode.PartType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import midend.irassets.irId;
import midend.irassets.irType;
import midend.irassets.irType.IRType;
import midend.irassets.statements.instructions.allocaIns;
import midend.irassets.statements.instructions.brIns;
import midend.irassets.statements.instructions.callIns;
import midend.irassets.statements.instructions.declareIns;
import midend.irassets.statements.instructions.globalVarIns;
import midend.irassets.statements.instructions.loadIns;
import midend.irassets.statements.instructions.retIns;
import midend.irassets.statements.instructions.storeIns;
import midend.irassets.statements.irBlock;
import midend.irassets.statements.irClass;
import midend.irassets.statements.irFunc;
import midend.irassets.statements.irIns;
import midend.irassets.statements.irWorld;
import org.antlr.v4.runtime.misc.Pair;

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
    boolean isMethod = curClass == null;
    // use outer [curBlock] to distinguish the block

    if (hasDeclared) {
      // skip builtin
      if (it.builtin)
        return;
      irFunc func = null;
      if (isMethod)
        func = it.type == null ? curClass.constructor
                               : curClass.methods.get(it.name);
      else
        func = world.functions.get(GlobalPrefix + it.name);
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
      // build suite
      it.suite.accept(this);
      // build return block
      var retBlock = new irBlock();
      var retInst = new retIns(func.retType);
      func.retBlock = retBlock;
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
        func.name = curClass.name + it.name;
      else if (it.name == "main")
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
        world.functions.put(func.name, func);
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
    // ! the name of variables has suffixes according to
    // scopeheight-1
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
      scopeStack.peek().put(it.name, ins.result);
    }
    // assignment
    if (it.assignExpr != null) {
      it.assignExpr.accept(this);
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
      cls.constructor.name = ClassPrefix + it.name;
      cls.constructor.theClass = cls;
      cls.constructor.retType = new irType(IRType.Void);
      cls.constructor.args.add(new irId(new irType(cls))); // this ptr
      if (maybe_constructor.argList != null) {
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
    // no need
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
    // no need
  }

  @Override
  public void visit(atomNode it) {
    // no need
  }

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
    irBlock trueBlock = new irBlock();
    irBlock falseBlock = it.falseStmt == null ? null : new irBlock();
    irBlock gotoBlock = new irBlock();

    it.ifCondExpr.accept(this);
    var condId = it.ifCondExpr.value;
    genBr(condId, trueBlock, falseBlock);
    // get true and false blocks
    curBlock = trueBlock;
    curFunc.blocks.add(trueBlock);
    genBr(gotoBlock, false);
    it.trueStmt.accept(this);
    if (it.falseStmt != null) {
      curBlock = falseBlock;
      curFunc.blocks.add(falseBlock);
      genBr(gotoBlock, false);
      it.falseStmt.accept(this);
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
    genBr(condBlock,
          false); // must first genBr so that continue and break can revise it
    condStack.push(condBlock);
    gotoStack.push(gotoBlock);
    it.bodyStmt.accept(this);
    condStack.pop();
    gotoStack.pop();
    // impl goto blick
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
      genBr(todoBlock, true);
    } else {
      it.condExpr.accept(this);
      var condId = it.condExpr.value;
      genBr(condId, todoBlock, gotoBlock);
    }
    // step+body
    curFunc.blocks.add(todoBlock);
    curBlock = todoBlock;
    genBr(condBlock, false);
    condStack.push(condBlock);
    gotoStack.push(gotoBlock);
    it.forBodyStmt.accept(this);
    condStack.pop();
    gotoStack.pop();
    // goto
    curFunc.blocks.add(gotoBlock);
    curBlock = gotoBlock;
    gotoBlock.terminal = formerBr;
    scopeStack.pop();
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
      var st = new storeIns(retValue.valueType);
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

  // endregion

  //#region expression

  @Override
  public void visit(parenExprNode it) {
    it.expr.accept(this);
    it.value = it.expr.value;
  }

  @Override
  public void visit(atomExprNode it) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(memberExprNode it) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(arrayExprNode it) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(funcExprNode it) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(newExprNode it) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(selfExprNode it) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(unaryExprNode it) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(binaryExprNode it) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(ternaryExprNode it) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(formatStrExprNode it) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(arrayConstExprNode it) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(assignExprNode it) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
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
        Map.entry("_string_lt", "_string_lt"),
        Map.entry("_string_le", "_string_le"),
        Map.entry("_string_gt", "_string_gt"),
        Map.entry("_string_ge", "_string_ge"), Map.entry("_init", "_init"),
        Map.entry("_new", "malloc"), Map.entry("_new_array", "_new_array"));
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

  // load a value from pointer
  irId getPtrVal(irId ptr) {
    var ins = new loadIns(ptr.valueType.getDeref());
    ins.loadAddr = ptr;
    curBlock.instructions.add(ins);
    return ins.result;
  }

  //#endregion
}
