package frontend;

import ast.*;
import ast.ExprNodes.*;
import ast.ExprNodes.ExprNode.OpType;
import ast.StmtNodes.*;
import ast.arrayConstNode.ArrayConstType;
import ast.atomNode.AtomType;
import ast.programPartNode.PartType;
import parser.MxBaseVisitor;
import parser.MxParser;
import utility.Type;
import utility.Type.ASTType;
import utility.error.semanticError;
import utility.position;
import utility.scope.*;

public class ASTBuilder extends MxBaseVisitor<ASTNode> {
  public globalScope mainScope;
  public ASTBuilder(globalScope mainScope) { this.mainScope = mainScope; }

  //#region basics

  @Override
  public ASTNode visitProgram(MxParser.ProgramContext ctx) {
    programNode node = new programNode(new position(ctx));
    ctx.programPart().forEach(
        part -> node.parts.add((programPartNode)visit(part)));
    generateBuiltin(node);
    return node;
  }

  @Override
  public ASTNode visitProgramPart(MxParser.ProgramPartContext ctx) {
    programPartNode node = new programPartNode(new position(ctx));
    if (ctx.classDef() != null) {
      node.partType = PartType.ClassDef;
      node.classDef = (classDefNode)visit(ctx.classDef());
    } else if (ctx.funcDef() != null) {
      node.partType = PartType.FuncDef;
      node.funcDef = (funcDefNode)visit(ctx.funcDef());
    } else if (ctx.varDef() != null) {
      node.partType = PartType.VarDef;
      node.varDef = (varDefNode)visit(ctx.varDef());
    } else
      throw new semanticError("Unexpected error when building programPart!",
                              node.pos);
    return node;
  }

  @Override
  public ASTNode visitFuncDef(MxParser.FuncDefContext ctx) {
    funcDefNode node = new funcDefNode(new position(ctx));
    node.name = ctx.Identifier().getText();
    if (ctx.type() != null)
      node.type = (typeNode)visit(ctx.type());
    if (ctx.argList() != null)
      node.argList = (argListNode)visit(ctx.argList());
    node.suite = (suiteNode)visit(ctx.suite());
    return node;
  }

  @Override
  public ASTNode visitArgList(MxParser.ArgListContext ctx) {
    argListNode node = new argListNode(new position(ctx));
    for (var t : ctx.type())
      node.types.add((typeNode)visit(t));
    for (var id : ctx.Identifier())
      node.identifiers.add(id.getText());
    return node;
  }

  @Override
  public ASTNode visitVarDef(MxParser.VarDefContext ctx) {
    varDefNode node = new varDefNode(new position(ctx));
    node.type = (typeNode)visit(ctx.type());
    for (var vt : ctx.varTerm())
      node.varTerms.add((varTermNode)visit(vt));
    return node;
  }

  @Override
  public ASTNode visitVarTerm(MxParser.VarTermContext ctx) {
    varTermNode node = new varTermNode(new position(ctx));
    node.name = ctx.Identifier().getText();
    if (ctx.expression() != null)
      node.assignExpr = (ExprNode)visit(ctx.expression());
    if (ctx.exprList() != null)
      node.constructArgs = (exprListNode)visit(ctx.exprList());
    return node;
  }

  @Override
  public ASTNode visitClassDef(MxParser.ClassDefContext ctx) {
    classDefNode node = new classDefNode(new position(ctx));
    node.name = ctx.Identifier().getText();
    for (var f : ctx.funcDef())
      node.methodDefs.add((funcDefNode)visit(f));
    for (var v : ctx.varDef())
      node.varDefs.add((varDefNode)visit(v));
    return node;
  }

  @Override
  public ASTNode visitSuite(MxParser.SuiteContext ctx) {
    suiteNode node = new suiteNode(new position(ctx));
    for (var st : ctx.statement()) {
      node.stmts.add((StmtNode)visit(st));
    }
    return node;
  }

  @Override
  public ASTNode visitType(MxParser.TypeContext ctx) {
    typeNode node = new typeNode(new position(ctx));
    Type type;
    if (ctx.Void() != null)
      type = new Type(Type.ASTType.Void, false);
    else if (ctx.Bool() != null)
      type = new Type(Type.ASTType.Bool, false);
    else if (ctx.Int() != null)
      type = new Type(Type.ASTType.Int, false);
    else if (ctx.String() != null)
      type = new Type(Type.ASTType.String, false);
    else if (ctx.Identifier() != null)
      type = new Type(ctx.Identifier().getText());
    else
      throw new semanticError("Unexpected error when detecting type!",
                              node.pos);

    type.dimension = ctx.arrayBlock().size();
    for (var ab : ctx.arrayBlock())
      node.blocks.add((arrayBlockNode)visit(ab));
    node.type = type;
    return node;
  }

  @Override
  public ASTNode visitExprList(MxParser.ExprListContext ctx) {
    exprListNode node = new exprListNode(new position(ctx));
    for (var e : ctx.expression())
      node.expressions.add((ExprNode)visit(e));
    return node;
  }

  @Override
  public ASTNode visitArrayBlock(MxParser.ArrayBlockContext ctx) {
    arrayBlockNode node = new arrayBlockNode(new position(ctx));
    if (ctx.expression() != null)
      node.expr = (ExprNode)visit(ctx.expression());
    return node;
  }

  @Override
  public ASTNode visitArrayConst(MxParser.ArrayConstContext ctx) {
    arrayConstNode node = new arrayConstNode(new position(ctx));
    if (!ctx.atom().isEmpty()) {
      for (var a : ctx.atom()) {
        var at = (atomNode)visit(a);
        if (at.atomType == AtomType.Identifier ||
            at.atomType == AtomType.This || at.atomType == AtomType.Null)
          throw new semanticError("Unsupported arrayConst element type",
                                  at.pos);
        if (node.varType == null) {
          if (at.atomType == AtomType.Bool)
            node.varType = new Type(ASTType.Bool, false);
          else if (at.atomType == AtomType.DecimalInt)
            node.varType = new Type(ASTType.Int, false);
          else
            node.varType = new Type(ASTType.String, false);
        } else {
          if (node.varType.type == ASTType.Bool &&
                  at.atomType != AtomType.Bool ||
              node.varType.type == ASTType.Int &&
                  at.atomType != AtomType.DecimalInt ||
              node.varType.type == ASTType.String &&
                  at.atomType != AtomType.StringConst)
            throw new semanticError("Multi-type in one-dimension arrayConst",
                                    node.pos);
        }
        node.atoms.add(at);
      }
      node.varType.dimension = 1;
    } else if (!ctx.arrayConst().isEmpty()) {
      Type sonType = null;
      for (var a : ctx.arrayConst()) {
        arrayConstNode ac = (arrayConstNode)visit(a);
        if (ac.varType.type == ASTType.Null)
          node.eachType.add(ArrayConstType.Empty);
        else
          node.eachType.add(ArrayConstType.ArrayConst);

        // 1. all same type or empty
        // 2. all same dimension or empty
        if (ac.varType.type == ASTType.Null) {
          if (sonType == null) {
            sonType = new Type(ASTType.Null, false);
          }
          if (ac.varType.dimension == 0)
            sonType.dimension = Math.max(2, sonType.dimension);
          else
            sonType.dimension = Math.max(ac.varType.dimension + 1,
                                         sonType.dimension); //{{{}},{}}
        } else {
          if (sonType == null) {
            sonType = new Type(ac.varType.type, false);
            sonType.dimension = ac.varType.dimension + 1;
          }
          // check dimension
          // 1. if first-time non-{}, then turn to it
          else if (sonType.type == ASTType.Null) {
            sonType.type = ac.varType.type;
            if (sonType.dimension <= ac.varType.dimension)
              sonType.dimension = ac.varType.dimension + 1;
          } else { // 2. if not same type or not same dimension, throw
            if (sonType.type != ac.varType.type)
              throw new semanticError("ArrayConst has different type elements",
                                      ac.pos);
            if (sonType.dimension != ac.varType.dimension + 1)
              throw new semanticError(
                  "ArrayConst element has multi-dimension beyonds zero",
                  ac.pos);
          }
        }
      }
      node.varType = sonType;
      // {{},{}}'s dimension is 2
    } else {
      node.varType = new Type(ASTType.Null, false);
      node.varType.dimension = 0; // need more thoughts
    }
    return node;
  }

  @Override
  public ASTNode visitAtom(MxParser.AtomContext ctx) {
    atomNode node = new atomNode(new position(ctx));
    if (ctx.This() != null) {
      node.atomType = atomNode.AtomType.This;
    } else if (ctx.Identifier() != null) {
      node.atomType = atomNode.AtomType.Identifier;
      node.identifier = ctx.Identifier().getText();
    } else if (ctx.StringConst() != null) {
      node.atomType = atomNode.AtomType.StringConst;
      node.stringConst = ctx.StringConst().getText().substring(
          1, ctx.StringConst().getText().length() - 1);
    } else if (ctx.DecimalInt() != null) {
      node.atomType = atomNode.AtomType.DecimalInt;
      node.decimalInt = Integer.parseInt(ctx.DecimalInt().getText());
    } else if (ctx.True() != null) {
      node.atomType = atomNode.AtomType.Bool;
      node.bool = true;
    } else if (ctx.False() != null) {
      node.atomType = atomNode.AtomType.Bool;
      node.bool = false;
    } else if (ctx.Null() != null) {
      node.atomType = atomNode.AtomType.Null;
    } else
      throw new semanticError("Unexpected error when detecting atom type!",
                              node.pos);
    return node;
  }

  //#endregion

  //#region statements

  @Override
  public ASTNode visitSuiteStmt(MxParser.SuiteStmtContext ctx) {
    suiteStmtNode node = new suiteStmtNode(new position(ctx));
    node.suite = (suiteNode)visit(ctx.suite());
    return node;
  }

  @Override
  public ASTNode visitIfStmt(MxParser.IfStmtContext ctx) {
    ifStmtNode node = new ifStmtNode(new position(ctx));
    node.ifCondExpr = (ExprNode)visit(ctx.expression());
    node.trueStmt = (StmtNode)visit(ctx.trueStmt);
    if (ctx.falseStmt != null)
      node.falseStmt = (StmtNode)visit(ctx.falseStmt);
    return node;
  }

  @Override
  public ASTNode visitWhileStmt(MxParser.WhileStmtContext ctx) {
    whileStmtNode node = new whileStmtNode(new position(ctx));
    node.condExpr = (ExprNode)visit(ctx.expression());
    node.bodyStmt = (StmtNode)visit(ctx.statement());
    return node;
  }

  @Override
  public ASTNode visitForStmt(MxParser.ForStmtContext ctx) {
    forStmtNode node = new forStmtNode(new position(ctx));
    if (ctx.initExpr != null) {
      node.initWithVarDef = false;
      node.initExpr = (ExprNode)visit(ctx.initExpr);
    } else if (ctx.varDef() != null) {
      node.initWithVarDef = true;
      node.initVarDef = (varDefNode)visit(ctx.varDef());
    }
    if (ctx.condExpr != null)
      node.condExpr = (ExprNode)visit(ctx.condExpr);
    if (ctx.stepExpr != null)
      node.stepExpr = (ExprNode)visit(ctx.stepExpr);
    node.forBodyStmt = (StmtNode)visit(ctx.statement());
    return node;
  }

  @Override
  public ASTNode visitContinueStmt(MxParser.ContinueStmtContext ctx) {
    return new continueStmtNode(new position(ctx));
  }

  @Override
  public ASTNode visitBreakStmt(MxParser.BreakStmtContext ctx) {
    return new breakStmtNode(new position(ctx));
  }

  @Override
  public ASTNode visitReturnStmt(MxParser.ReturnStmtContext ctx) {
    returnStmtNode node = new returnStmtNode(new position(ctx));
    if (ctx.expression() != null)
      node.retExpr = (ExprNode)visit(ctx.expression());
    return node;
  }

  @Override
  public ASTNode visitSingleExprStmt(MxParser.SingleExprStmtContext ctx) {
    singleExprStmtNode node = new singleExprStmtNode(new position(ctx));
    node.expr = (ExprNode)visit(ctx.expression());
    return node;
  }

  @Override
  public ASTNode visitVarDefStmt(MxParser.VarDefStmtContext ctx) {
    varDefStmtNode node = new varDefStmtNode(new position(ctx));
    node.varDef = (varDefNode)visit(ctx.varDef());
    return node;
  }

  @Override
  public ASTNode visitEmptyStmt(MxParser.EmptyStmtContext ctx) {
    return new emptyStmtNode(new position(ctx));
  }

  //#endregion

  //#region expressions

  @Override
  public ASTNode visitParenExpr(MxParser.ParenExprContext ctx) {
    parenExprNode node = new parenExprNode(new position(ctx));
    node.expr = (ExprNode)visit(ctx.expression());
    return node;
  }

  @Override
  public ASTNode visitAtomExpr(MxParser.AtomExprContext ctx) {
    atomExprNode node = new atomExprNode(new position(ctx));
    node.atom = (atomNode)visit(ctx.atom());
    return node;
  }

  @Override
  public ASTNode visitMemberExpr(MxParser.MemberExprContext ctx) {
    memberExprNode node = new memberExprNode(new position(ctx));
    node.expr = (ExprNode)visit(ctx.expression());
    if (ctx.Identifier() != null)
      node.id = ctx.Identifier().getText();
    else
      node.id = "_this";
    return node;
  }

  @Override
  public ASTNode visitArrayExpr(MxParser.ArrayExprContext ctx) {
    arrayExprNode node = new arrayExprNode(new position(ctx));
    node.nameExpr = (ExprNode)visit(ctx.expression());
    assert (!ctx.arrayBlock().isEmpty());
    for (var a : ctx.arrayBlock())
      node.arr.add((arrayBlockNode)visit(a));
    return node;
  }

  @Override
  public ASTNode visitFuncExpr(MxParser.FuncExprContext ctx) {
    funcExprNode node = new funcExprNode(new position(ctx));
    node.funcExpr = (ExprNode)visit(ctx.expression());
    if (ctx.exprList() != null)
      node.args = (exprListNode)visit(ctx.exprList());
    return node;
  }

  @Override
  public ASTNode visitNewExpr(MxParser.NewExprContext ctx) {
    newExprNode node = new newExprNode(new position(ctx));
    node.rightType = (typeNode)visit(ctx.type());
    if (ctx.arrayConst() != null)
      node.arrayConst = (arrayConstNode)visit(ctx.arrayConst());
    // check in SemanticChecker
    return node;
  }

  @Override
  public ASTNode visitSelfExpr(MxParser.SelfExprContext ctx) {
    selfExprNode node = new selfExprNode(new position(ctx));
    node.expr = (ExprNode)visit(ctx.expression());
    if (ctx.Inc() != null)
      node.op = OpType.Inc;
    else if (ctx.Dec() != null)
      node.op = OpType.Dec;
    else
      throw new semanticError(
          "Unexpected error when parsing self expression's operand!", node.pos);
    return node;
  }

  @Override
  public ASTNode visitUnaryExpr(MxParser.UnaryExprContext ctx) {
    unaryExprNode node = new unaryExprNode(new position(ctx));
    if (ctx.Inc() != null)
      node.op = OpType.Inc;
    else if (ctx.Dec() != null)
      node.op = OpType.Dec;
    else if (ctx.Not() != null)
      node.op = OpType.Not;
    else if (ctx.Tilde() != null)
      node.op = OpType.Tilde;
    else if (ctx.Add() != null)
      node.op = OpType.Add;
    else if (ctx.Sub() != null)
      node.op = OpType.Sub;
    else
      throw new semanticError(
          "Unexpected error when parsing unary expression's operand!",
          node.pos);
    node.rhsExpr = (ExprNode)visit(ctx.expression());
    return node;
  }

  @Override
  public ASTNode visitBinaryExpr(MxParser.BinaryExprContext ctx) {
    binaryExprNode node = new binaryExprNode(new position(ctx));
    if (ctx.Mul() != null)
      node.op = OpType.Mul;
    else if (ctx.Div() != null)
      node.op = OpType.Div;
    else if (ctx.Mod() != null)
      node.op = OpType.Mod;
    else if (ctx.Add() != null)
      node.op = OpType.Add;
    else if (ctx.Sub() != null)
      node.op = OpType.Sub;
    else if (ctx.Shl() != null)
      node.op = OpType.Shl;
    else if (ctx.Shr() != null)
      node.op = OpType.Shr;
    else if (ctx.Ge() != null)
      node.op = OpType.Ge;
    else if (ctx.Geq() != null)
      node.op = OpType.Geq;
    else if (ctx.Le() != null)
      node.op = OpType.Le;
    else if (ctx.Leq() != null)
      node.op = OpType.Leq;
    else if (ctx.Eq() != null)
      node.op = OpType.Eq;
    else if (ctx.Ne() != null)
      node.op = OpType.Ne;
    else if (ctx.And() != null)
      node.op = OpType.And;
    else if (ctx.Or() != null)
      node.op = OpType.Or;
    else if (ctx.Xor() != null)
      node.op = OpType.Xor;
    else if (ctx.AndAnd() != null)
      node.op = OpType.AndAnd;
    else if (ctx.OrOr() != null)
      node.op = OpType.OrOr;
    else
      throw new semanticError(
          "Unexpected error when parsing binary expression's operand!",
          node.pos);
    node.lhsExpr = (ExprNode)visit(ctx.left);
    node.rhsExpr = (ExprNode)visit(ctx.right);
    return node;
  }

  @Override
  public ASTNode visitTernaryExpr(MxParser.TernaryExprContext ctx) {
    ternaryExprNode node = new ternaryExprNode(new position(ctx));
    node.condExpr = (ExprNode)visit(ctx.cond);
    node.lhsExpr = (ExprNode)visit(ctx.left);
    node.rhsExpr = (ExprNode)visit(ctx.right);
    return node;
  }

  @Override
  public ASTNode visitFormatStrExpr(MxParser.FormatStrExprContext ctx) {
    formatStrExprNode node = new formatStrExprNode(new position(ctx));
    if (ctx.FStringAtom() != null) {
      node.strings.add(ctx.FStringAtom().getText().substring(
          2, ctx.FStringAtom().getText().length() - 1));
    } else {
      //(head expr)[(body expr)(body expr)...]?(tail)
      if (ctx.FStringHead() == null || ctx.FStringTail() == null ||
          ctx.expression().isEmpty())
        throw new semanticError("Incomplete structure of format string!",
                                node.pos);
      for (var ie : ctx.expression())
        node.innerExprs.add((ExprNode)visit(ie));
      node.strings.add(ctx.FStringHead().getText().substring(
          2, ctx.FStringHead().getText().length() - 1));
      for (var ms : ctx.FStringBody())
        node.strings.add(ms.getText().substring(1, ms.getText().length() - 1));
      node.strings.add(ctx.FStringTail().getText().substring(
          1, ctx.FStringTail().getText().length() - 1));
    }
    return node;
  }

  @Override
  public ASTNode visitArrayConstExpr(MxParser.ArrayConstExprContext ctx) {
    arrayConstExprNode node = new arrayConstExprNode(new position(ctx));
    node.arrayConst = (arrayConstNode)visit(ctx.arrayConst());
    return node;
  }

  @Override
  public ASTNode visitAssignExpr(MxParser.AssignExprContext ctx) {
    assignExprNode node = new assignExprNode(new position(ctx));
    node.lhsExpr = (ExprNode)visit(ctx.lvalue);
    node.rhsExpr = (ExprNode)visit(ctx.rvalue);
    return node;
  }

  //#endregion

  //#region builtins

  /*
   * int <arraytype_identifier>.size();
   * void print(string str);
   * void println(string str);
   * void printInt(int n);
   * void printlnInt(int n);
   * string getString();
   * int getInt();
   * string toString(int i);
   * int <StringIdentifier>.length();
   * string <StringIdentifier>.substring(int left, int right);
   * int <StringIdentifier>.parseInt();
   * int <StringIdentifier>.ord(int pos);
   */

  public void generateBuiltin(programNode root) {
    Type voidType = new Type(ASTType.Void, false);
    // Type boolType = new Type(ASTType.Bool, false);
    Type intType = new Type(ASTType.Int, false);
    Type stringType = new Type(ASTType.String, false);

    classDefNode arrayClass = new classDefNode(null);
    arrayClass.name = "_array"; // must not duplicate
    arrayClass.builtin = true;
    arrayClass.methodDefs.add(generateFunc(intType, "size"));
    root.parts.add(generatePart(arrayClass));

    classDefNode stringClass = new classDefNode(null);
    stringClass.name = "_string"; // must not duplicate
    stringClass.builtin = true;
    stringClass.methodDefs.add(generateFunc(intType, "length"));
    stringClass.methodDefs.add(generateFunc(stringType, "substring", intType,
                                            "left", intType, "right"));
    stringClass.methodDefs.add(generateFunc(intType, "parseInt"));
    stringClass.methodDefs.add(generateFunc(intType, "ord", intType, "pos"));
    root.parts.add(generatePart(stringClass));

    root.parts.add(
        generatePart(generateFunc(voidType, "print", stringType, "str")));
    root.parts.add(
        generatePart(generateFunc(voidType, "println", stringType, "str")));
    root.parts.add(
        generatePart(generateFunc(voidType, "printInt", intType, "n")));
    root.parts.add(
        generatePart(generateFunc(voidType, "printlnInt", intType, "n")));
    root.parts.add(generatePart(generateFunc(stringType, "getString")));
    root.parts.add(generatePart(generateFunc(intType, "getInt")));
    root.parts.add(
        generatePart(generateFunc(stringType, "toString", intType, "i")));

    root.parts.add(generatePart(generateFunc(
        stringType, "_string_add", stringType, "lhs", stringType, "rhs")));
    root.parts.add(generatePart(generateFunc(
        stringType, "_string_eq", stringType, "lhs", stringType, "rhs")));
    root.parts.add(generatePart(generateFunc(
        stringType, "_string_ne", stringType, "lhs", stringType, "rhs")));
    root.parts.add(generatePart(generateFunc(
        stringType, "_string_lt", stringType, "lhs", stringType, "rhs")));
    root.parts.add(generatePart(generateFunc(
        stringType, "_string_le", stringType, "lhs", stringType, "rhs")));
    root.parts.add(generatePart(generateFunc(
        stringType, "_string_gt", stringType, "lhs", stringType, "rhs")));
    root.parts.add(generatePart(generateFunc(
        stringType, "_string_ge", stringType, "lhs", stringType, "rhs")));
  }

  programPartNode generatePart(classDefNode node) {
    programPartNode part = new programPartNode(null);
    part.partType = PartType.ClassDef;
    part.classDef = node;
    return part;
  }

  programPartNode generatePart(funcDefNode node) {
    programPartNode part = new programPartNode(null);
    part.partType = PartType.FuncDef;
    part.funcDef = node;
    return part;
  }

  funcDefNode generateFunc(Type retType, String name) {
    funcDefNode node = new funcDefNode(null);
    node.builtin = true;
    node.type = new typeNode(null);
    node.type.type = retType;
    node.name = name;
    node.argList = new argListNode(null);
    node.suite = new suiteNode(null);
    // we don't need to actually write stmt by stmt
    return node;
  }

  funcDefNode generateFunc(Type retType, String name, Type argType,
                           String argName) {
    funcDefNode node = new funcDefNode(null);
    node.builtin = true;
    node.type = new typeNode(null);
    node.type.type = retType;
    node.name = name;

    node.argList = new argListNode(null);
    typeNode tn = new typeNode(null);
    tn.type = argType;
    node.argList.types.add(tn);
    node.argList.identifiers.add(argName);

    node.suite = new suiteNode(null);
    return node;
  }

  funcDefNode generateFunc(Type retType, String name, Type argType1,
                           String argName1, Type argType2, String argName2) {
    funcDefNode node = new funcDefNode(null);
    node.builtin = true;
    node.type = new typeNode(null);
    node.type.type = retType;
    node.name = name;

    node.argList = new argListNode(null);
    typeNode tn1 = new typeNode(null), tn2 = new typeNode(null);
    tn1.type = argType1;
    tn2.type = argType2;
    node.argList.types.add(tn1);
    node.argList.types.add(tn2);
    node.argList.identifiers.add(argName1);
    node.argList.identifiers.add(argName2);

    node.suite = new suiteNode(null);
    return node;
  }

  //#endregion
}
