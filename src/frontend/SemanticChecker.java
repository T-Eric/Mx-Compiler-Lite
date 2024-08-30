package frontend;

import ast.*;
import ast.ExprNodes.*;
import ast.ExprNodes.ExprNode.ExprType;
import ast.ExprNodes.ExprNode.OpType;
import ast.StmtNodes.*;
import ast.StmtNodes.StmtNode.StmtType;
import java.util.ArrayList;
import java.util.Stack;
import utility.Type;
import utility.Type.ASTType;
import utility.error.semanticError;
import utility.scope.*;

// used after ForwardCollector
public class SemanticChecker implements ASTVisitor {
  programNode root = null;
  globalScope ms; // mainScope
  Stack<scopeType> scopeTypeStack = new Stack<>();
  Stack<scope> scopeStack = new Stack<>();
  String curClass = null;

  public enum scopeType { Class, Func, Loop, Main, Custom }

  public SemanticChecker(globalScope gScope) { ms = gScope; }

  //#region basics

  @Override
  public void visit(programNode it) {
    scopeStack.push(ms);
    scopeTypeStack.push(scopeType.Main);
    it.parts.forEach(pt -> pt.accept(this));
    scopeStack.pop();
    scopeTypeStack.pop();
    if (!ms.containsFunc("main"))
      throw new semanticError("Where's your 'main' function?", it.pos);
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
      break;
    default:
      throw new semanticError("Failed to parse program section!", it.pos);
    }
  }

  @Override
  public void visit(funcDefNode it) {
    if (it.builtin)
      return;
    scopeTypeStack.push(scopeType.Func);
    scope curScope = null;
    if (in_class())
      curScope =
          ms.getClassScope(curClass, it.pos).getMethodScope(it.name, it.pos);
    else
      curScope = ms.getFuncScope(it.name, it.pos);
    scopeStack.push(curScope);
    // if (it.argList != null)
    //   it.argList.accept(this);
    it.suite.accept(this);
    scopeStack.pop();
    scopeTypeStack.pop();

    Type returnType = it.suite.returnType;

    boolean valid = true;
    if (it.name.equals("main")) {
      if (it.type == null || it.type.type.type != ASTType.Int ||
          it.argList != null) {
        valid = false;
      } else {
        valid = returnType == null || returnType.type == ASTType.Int;
      }
      if (!valid)
        throw new semanticError(
            "Main function Error! Even main function is hard to u?", it.pos);
      else
        return;
    }
    if (returnType == null) { // void func or constructor
      if (it.type == null)
        if (in_class() && it.name.equals(curClass))
          return;
        else
          throw new semanticError("Constructor bad!", it.pos);
      if (it.type.type.type == ASTType.Void)
        return;
      else
        throw new semanticError("Missing Return Statement", it.pos);
    }
    if (it.type == null)
      throw new semanticError("Class constructor should not have return value!",
                              it.pos);
    if (!it.type.type.equals(returnType))
      throw new semanticError("Type Mismatch", it.pos);
  }

  @Override
  public void visit(argListNode it) {
    if (it != null) {
      scope curScope = scopeStack.peek();
      for (int i = 0; i < it.types.size(); ++i) {
        it.types.get(i).usageHasDimension = false;
        it.types.get(i).accept(this);
        //////
        curScope.defVar(it.identifiers.get(i), it.types.get(i).type, it.pos);
      }
    }
  }

  @Override
  public void visit(varDefNode it) {
    it.type.usageHasDimension = true;
    it.type.accept(this);
    if (it.type.type.type == ASTType.Void)
      throw new semanticError("Variable cannot be void", it.pos);
    for (var term : it.varTerms) {
      term.callerType = it.type.type;
      term.accept(this);
      scopeStack.peek().defVar(term.name, it.type.type, term.pos);
    }
  }

  @Override
  public void visit(varTermNode it) {
    if (it.assignExpr != null) {
      it.assignExpr.accept(this);
      Type callerType = it.callerType, calleeType = it.assignExpr.valueType;
      if (!(((callerType.type == ASTType.ClassName ||
              callerType.dimension != 0) &&
             (calleeType.type == ASTType.Null)) ||
            callerType.equals(calleeType)))
        throw new semanticError("Type mismatch in variable assignment!",
                                it.pos);
    }
    // ! where's exprList's doings?
  }

  @Override
  public void visit(classDefNode it) {
    if (it.builtin)
      return;
    // in forward collection, we've already
    // collected this class
    // we only need to pick the class
    scopeTypeStack.push(scopeType.Class);
    var cScope = ms.getClassScope(it.name, it.pos);
    scopeStack.push(cScope);
    curClass = it.name;

    for (var funcdef : it.methodDefs)
      funcdef.accept(this);
    // and traverse all the functions

    scopeStack.pop();
    scopeTypeStack.pop();
    curClass = null;
  }

  @Override
  public void visit(suiteNode it) {
    // use the scope of stacktop
    // ! hasn't considered custom scope like:
    /*
     * int main(){
     *   {
     *     a=1;
     *   }
     *   a=1;
     * }
     */
    for (var stmt : it.stmts) {
      stmt.accept(this);
      // if multi-ret, throw
      if (stmt.returnType != null) {
        if (it.returnType == null || it.returnType.type == ASTType.Null)
          it.returnType = stmt.returnType;
        else if (!stmt.returnType.equals_feat_null(it.returnType))
          throw new semanticError("Type Mismatch", stmt.pos);
        // if multi-return-type there must be one type mismatch
      }
    }
  }

  @Override
  public void visit(typeNode it) {
    // only check identifier situations
    if (!ms.containsType(it.type))
      throw new semanticError("Undefined type usage!", it.pos);

    if (it.type.dimension != 0) {
      // usage: "argList","funcDef","varDef"
      // these situations all blocks are empty like int[][][] a
      if (!it.usageHasDimension) {
        for (var block : it.blocks)
          if (block != null)
            throw new semanticError(
                "ArrayType definition & arguments shouldn't set its length!",
                block.pos);
      } else {
        // usage: newExpr, int[][] a=new int[3][4], int[][][] b=new int[3][][]
        // but int[][][] c=new int[2][][4] is illegal
        if (it.blocks.size() == 0 || it.blocks.get(0) == null)
          throw new semanticError("By definition here should be dimensions!",
                                  it.pos);
        boolean hasNum = true;
        for (var block : it.blocks) {
          block.accept(this);
          if (block.expr == null)
            hasNum = false;
          else if (!hasNum)
            throw new semanticError(
                "Set length after an null-length dimension!", block.pos);
        }
      }
    }
  }

  @Override
  public void visit(exprListNode it) {
    for (var expr : it.expressions)
      expr.accept(this);
  }

  @Override
  public void visit(arrayBlockNode it) {
    if (it.expr != null) {
      it.expr.accept(this);
      if (it.expr.valueType.type != ASTType.Int ||
          it.expr.valueType.dimension != 0)
        throw new semanticError(
            "Array Bracket contains non-integer or extra bracket!", it.pos);
    }
  }

  @Override
  public void visit(arrayConstNode it) {}

  @Override
  public void visit(atomNode it) {
    switch (it.atomType) {
    case This:
      if (in_class())
        it.varType = new Type(curClass);
      else
        throw new semanticError("'This' cannot occur outside a class", it.pos);
      break;
    case Identifier:
      scope curScope = scopeStack.peek();
      Type varType = curScope.getType(it.identifier, true);
      if (varType != null) {
        it.varType = new Type(varType.type, true);
        it.varType.dimension = varType.dimension;
        it.varType.className = varType.className;
      } else
        throw new semanticError("Undefined Identifier", it.pos);
      break;
    case StringConst:
      it.varType = new Type(ASTType.String, false);
      break;
    case DecimalInt:
      it.varType = new Type(ASTType.Int, false);
      break;
    case Bool:
      it.varType = new Type(ASTType.Bool, false);
      break;
    case Null:
      it.varType = new Type(ASTType.Null, false);
      break;
    default:
      throw new semanticError("Unexpected error when checking an Atom!",
                              it.pos);
    }
  }

  //#endregion

  //#region statements
  // remember to save the returnType!
  // when pushing stack, first push scope, then push scopeType

  @Override
  public void visit(suiteStmtNode it) {
    if (it.indie) {
      scopeStack.push(new scope(scopeStack.peek(), ms));
      it.suite.accept(this);
      scopeStack.pop();
    } else
      it.suite.accept(this);
    it.returnType = it.suite.returnType;
  }

  @Override
  public void visit(ifStmtNode it) {
    it.ifCondExpr.accept(this);
    if (it.ifCondExpr.valueType.type != ASTType.Bool)
      throw new semanticError("Invalid Type", it.ifCondExpr.pos);
    if (it.trueStmt.type == StmtType.Suite)
      ((suiteStmtNode)it.trueStmt).indie = false;
    scopeStack.push(new scope(scopeStack.peek(), ms));
    it.trueStmt.accept(this);
    scopeStack.pop();
    if (it.falseStmt != null) {
      if (it.falseStmt.type == StmtType.Suite)
        ((suiteStmtNode)it.falseStmt).indie = false;
      scopeStack.push(new scope(scopeStack.peek(), ms));
      it.falseStmt.accept(this);
      scopeStack.pop();
      if (!(it.trueStmt.returnType == null && it.falseStmt.returnType == null ||
            it.trueStmt.returnType.equals_feat_null(it.falseStmt.returnType)))
        throw new semanticError(
            "Return types in true and false branch are not equal!", it.pos);
    }
    it.returnType = it.trueStmt.returnType;
  }

  @Override
  public void visit(whileStmtNode it) {
    scopeStack.push(new scope(scopeStack.peek(), ms));
    it.condExpr.accept(this);
    if (it.condExpr.valueType.type != ASTType.Bool)
      throw new semanticError(
          "While-condition expression is not bool expression!",
          it.condExpr.pos);
    if (it.bodyStmt.type == StmtType.Suite)
      ((suiteStmtNode)it.bodyStmt).indie = false;
    scopeTypeStack.push(scopeType.Loop);
    it.bodyStmt.accept(this);
    scopeStack.pop();
    scopeTypeStack.pop();
    it.returnType = it.bodyStmt.returnType;
  }

  @Override
  public void visit(forStmtNode it) {
    scopeStack.push(new scope(scopeStack.peek(), ms));
    if (it.initExpr != null||it.initVarDef!=null) {
      if (it.initWithVarDef)
        it.initVarDef.accept(this);
      else
        it.initExpr.accept(this);
    }
    if (it.condExpr != null) {
      it.condExpr.accept(this);
      if (it.condExpr.valueType.type != ASTType.Bool)
        throw new semanticError("Invalid Type", it.pos);
    }
    if (it.stepExpr != null)
      it.stepExpr.accept(this);
    if (it.forBodyStmt.type == StmtType.Suite)
      ((suiteStmtNode)it.forBodyStmt).indie = false;
    scopeTypeStack.push(scopeType.Loop);
    it.forBodyStmt.accept(this);
    it.returnType = it.forBodyStmt.returnType;
    scopeStack.pop();
    scopeTypeStack.pop();
  }

  @Override
  public void visit(continueStmtNode it) {
    if (!in_loop())
      throw new semanticError("Invalid Control Flow", it.pos);
  }

  @Override
  public void visit(breakStmtNode it) {
    if (!in_loop())
      throw new semanticError("Invalid Control Flow", it.pos);
  }

  @Override
  public void visit(returnStmtNode it) {
    if (!in_func())
      throw new semanticError("Return shouldn't be used outside a function!",
                              it.pos);
    if (it.retExpr != null) {
      it.retExpr.accept(this);
      it.returnType = it.retExpr.valueType;
    }
  }

  @Override
  public void visit(singleExprStmtNode it) {
    it.expr.accept(this);
    // it.returnType = it.expr.valueType;
  }

  @Override
  public void visit(varDefStmtNode it) {
    it.varDef.accept(this);
    // it.returnType=null;
  }

  @Override
  public void visit(emptyStmtNode it) {
    // what can i say?
  }

  //#endregion

  //#region expressions

  @Override
  public void visit(parenExprNode it) {
    it.expr.accept(this);
    it.valueType = it.expr.valueType;
  }

  @Override
  public void visit(atomExprNode it) {
    it.atom.accept(this);
    it.valueType = it.atom.varType;
  }

  @Override
  public void visit(memberExprNode it) {
    it.expr.accept(this);
    Type objectType = it.expr.valueType;
    if (objectType.dimension != 0 || objectType.type == ASTType.String ||
        objectType.type == ASTType.ClassName) {
      if (objectType.dimension != 0)
        objectType.className = "_array";
      if (objectType.type == ASTType.String)
        objectType.className = "_string";
      if (it.id == "_this")
        it.valueType = new Type(objectType.className);
      else
        it.valueType = ms.getClassScope(objectType.className, it.pos)
                           .getType(it.id, false);
    } else
      throw new semanticError("Syntax error in member call!", it.pos);
  } ////// need further analyze

  @Override
  public void visit(arrayExprNode it) {
    it.nameExpr.accept(this); // the name is actually type
    Type arrType = it.nameExpr.valueType;
    if (arrType.dimension < it.arr.size())
      throw new semanticError("Dimension Out Of Bound", it.pos);
    for (var block : it.arr) {
      block.accept(this);
      if (block.expr == null)
        throw new semanticError(
            "Fetching array element with incomplete indices!", it.pos);
    }
    it.valueType = new Type(arrType.type, true);
    it.valueType.className = arrType.className;
    it.valueType.dimension = arrType.dimension - it.arr.size();
  }

  @Override
  public void visit(funcExprNode it) {
    funcScope fScope = null;
    if (it.funcExpr.exprType == ExprType.Member) {
      it.funcExpr.accept(this);
      var objectExpr = ((memberExprNode)it.funcExpr).expr;
      if (objectExpr.valueType.dimension != 0)
        objectExpr.valueType.className = "_array";
      else if (objectExpr.valueType.type == ASTType.String)
        objectExpr.valueType.className = "_string";
      fScope = ms.getClassScope(objectExpr.valueType.className, it.pos)
                   .getMethodScope(((memberExprNode)it.funcExpr).id, it.pos);
    } else if (it.funcExpr.exprType == ExprType.Atom) {
      it.funcExpr.accept(this);
      var funcName = ((atomExprNode)it.funcExpr).atom.identifier;
      // get scope in separate ways
      if (in_class() &&
          ms.getClassScope(curClass, it.pos).containsMethod(funcName)) {
        fScope =
            ms.getClassScope(curClass, it.pos).getMethodScope(funcName, it.pos);
      } else {
        // in class but calls outer func
        fScope = ms.getFuncScope(funcName, it.pos);
      }
    } else
      throw new semanticError("Syntax error in function call!", it.pos);
    // fScope is for checking argList's length
    ArrayList<Type> curArgTypes = new ArrayList<>();
    if (it.args != null) {
      it.args.accept(this);
      for (var en : it.args.expressions) {
        curArgTypes.add(en.valueType);
      }
    }
    var originArgTypes = fScope.argTypes;
    if (originArgTypes.size() != curArgTypes.size())
      throw new semanticError("Wrong number of args!", it.pos);
    for (int i = 0; i < originArgTypes.size(); ++i) {
      if (!originArgTypes.get(i).equals_feat_null(curArgTypes.get(i)))
        throw new semanticError("Receiving wrong argument type here!",
                                it.args.expressions.get(i).pos);
    }
    // valueType is returnType
    it.valueType = fScope.retType;
    it.valueType.isVariable = false; // function's return value must be rvalue
  }

  @Override
  public void visit(newExprNode it) {
    it.rightType.usageHasDimension = true; // only array can be newed
    it.rightType.accept(this);
    if (it.arrayConst != null)
      it.arrayConst.accept(this);
    if (it.rightType.type.type == ASTType.Void)
      throw new semanticError("New expressions cannot apply to void", it.pos);
    if (it.arrayConst != null &&
        !it.rightType.type.equals_feat_null(it.arrayConst.varType))
      throw new semanticError("Type Mismatch", it.pos);
    it.valueType = it.rightType.type;
  }

  @Override
  public void visit(selfExprNode it) {
    // only int can use ++ and --
    it.expr.accept(this);
    Type t = it.expr.valueType;
    if (!t.isVariable)
      throw new semanticError("Non-variable shouldn't use ++ or --!", it.pos);
    if (t.type != ASTType.Int || t.dimension != 0)
      throw new semanticError("Only int can use ++ and --!", it.pos);
    it.valueType = t;
    it.valueType.isVariable = false;
  }

  @Override
  public void visit(unaryExprNode it) {
    it.rhsExpr.accept(this);
    Type t = it.rhsExpr.valueType;
    if (it.op == OpType.Not) {
      if (t.type != ASTType.Bool || t.dimension != 0)
        throw new semanticError("Op ! can only be used with bool", it.pos);
    } else {
      if (t.type != ASTType.Int || t.dimension != 0)
        throw new semanticError("This left op can only be used with int",
                                it.pos);
      // if rightType is rightvalue it can't be applied to prefix operands
      if (!t.isVariable && (it.op == OpType.Inc || it.op == OpType.Dec))
        throw new semanticError("'++/--[right value]' is illegal", it.pos);
    }
    it.valueType = t;
  }

  @Override
  public void visit(binaryExprNode it) {
    it.lhsExpr.accept(this);
    it.rhsExpr.accept(this);
    Type lType = it.lhsExpr.valueType, rType = it.rhsExpr.valueType;
    Type v;
    /*
     * categories:
     * ==,!=: all
     * &&,||: bool
     * <,>,<=,>=: int,string
     * +: int,string
     * default: only int
     */

    if (it.op == OpType.Eq || it.op == OpType.Ne) {
      // int, bool, class(address), array, string
      if (lType.equals_feat_null(rType))
        v = new Type(ASTType.Bool, false);
      else
        throw new semanticError(
            "Not same type or not [array & class] op [null]", it.pos);
    } else if (it.op == OpType.AndAnd || it.op == OpType.OrOr) {
      // bool
      if (lType.type == ASTType.Bool && rType.type == ASTType.Bool)
        v = new Type(ASTType.Bool, false);
      else
        throw new semanticError("Type Mismatch", it.pos);
    } else if (it.op == OpType.Ge || it.op == OpType.Geq ||
               it.op == OpType.Le || it.op == OpType.Leq) {
      // int, string
      if ((lType.type == ASTType.Int && rType.type == ASTType.Int) ||
          (lType.type == ASTType.String && rType.type == ASTType.String))
        v = new Type(ASTType.Bool, false);
      else
        throw new semanticError("Type Mismatch", it.pos);
    } else if (it.op == OpType.Add) {
      // int, string
      if (lType.type == ASTType.Int && rType.type == ASTType.Int)
        v = new Type(ASTType.Int, false);
      else if (lType.type == ASTType.String && rType.type == ASTType.String)
        v = new Type(ASTType.String, false);
      else
        throw new semanticError("Invalid Type", it.pos);
    } else if (it.op == OpType.None) {
      throw new semanticError("I can't understand this operand...", it.pos);
    } else {
      if (lType.type == ASTType.Int && lType.type == ASTType.Int) {
        v = new Type(ASTType.Int, false);
      } else
        throw new semanticError("Type Mismatch", it.pos);
    }
    it.valueType = v;
  }

  @Override
  public void visit(ternaryExprNode it) {
    it.condExpr.accept(this);
    if (it.condExpr.valueType.type != ASTType.Bool)
      throw new semanticError("Ternary expression's condition is not bool",
                              it.pos);
    it.lhsExpr.accept(this);
    it.rhsExpr.accept(this);
    if (!it.lhsExpr.valueType.equals_feat_null(it.rhsExpr.valueType))
      throw new semanticError(
          "Ternary expression's two branch return different types", it.pos);
    it.valueType = it.lhsExpr.valueType;
    it.valueType.isVariable = false;
    // ! here! actually the valuetype is not known!
    // maybe we should keep 'dual valuetypes'
  }

  @Override
  public void visit(formatStrExprNode it) {
    for (var expr : it.innerExprs) {
      expr.accept(this);
      if (expr.valueType.dimension != 0 ||
          !(expr.valueType.type == ASTType.Bool ||
            expr.valueType.type == ASTType.Int ||
            expr.valueType.type == ASTType.String)) {
        throw new semanticError("Invalid Type", it.pos);
      }
    }
    it.valueType = new Type(ASTType.String, false);
  }

  @Override
  public void visit(arrayConstExprNode it) {
    it.arrayConst.accept(this);
    it.valueType = it.arrayConst.varType;
  }

  @Override
  public void visit(assignExprNode it) {
    // Remember checking lvalue
    it.lhsExpr.accept(this);
    if (!it.lhsExpr.valueType.isVariable)
      throw new semanticError("Invalid Type", it.pos);
    it.rhsExpr.accept(this);
    Type leftType = it.lhsExpr.valueType, rightType = it.rhsExpr.valueType;

    if (!leftType.equals_feat_null(rightType)) {
      // if (leftType.type == rightType.type &&
      //     leftType.dimension != rightType.dimension)
      //   throw new semanticError("Dimension Out Of Bound", null);
      throw new semanticError("Type Mismatch", it.pos);
    }

    it.valueType = leftType;
    // if set to null we cannot support continuous assignment
  }

  //#endregion

  //#region utility

  boolean in_loop() {
    for (var sct : scopeTypeStack)
      if (sct == scopeType.Loop)
        return true;
    return false;
  }

  boolean in_func() {
    for (var sct : scopeTypeStack)
      if (sct == scopeType.Func)
        return true;
    return false;
  }

  boolean in_class() {
    for (var sct : scopeTypeStack)
      if (sct == scopeType.Class)
        return true;

    return false;
  }

  //#endregion
}
