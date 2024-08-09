package frontend;

import ast.*;
import ast.ExprNodes.*;
import ast.StmtNodes.*;
import java.util.Stack;
import utility.Type.ASTType;
import utility.error.semanticError;
import utility.scope.*;

// used after ForwardCollector
public class SemanticChecker implements ASTVisitor {
  programNode root = null;
  globalScope ms = new globalScope(null); // mainScope
  Stack<scopeType> scopeTypeStack = new Stack<>();
  Stack<scope> scopeStack = new Stack<>();

  enum scopeType { Class, Func, Loop, Main, Custom }

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
    boolean in_class = false;
    if (scopeTypeStack.peek() == scopeType.Class)
      in_class = true;
    scopeTypeStack.push(scopeType.Func);
  }

  @Override
  public void visit(argListNode it) {}

  @Override
  public void visit(varDefNode it) {}

  @Override
  public void visit(varTermNode it) {}

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

    for (var funcdef : it.methodDefs)
      funcdef.accept(this);
    // and traverse all the functions

    scopeStack.pop();
    scopeTypeStack.pop();
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
        else if (stmt.returnType != it.returnType)
          throw new semanticError("Duplicated return declaration!", stmt.pos);
      }
    }
  }

  @Override
  public void visit(typeNode it) {}

  @Override
  public void visit(exprListNode it) {
    for(var expr:it.expressions)
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
  public void visit(atomNode it) {
    
  }

  @Override
  public void visit(suiteStmtNode it) {}

  @Override
  public void visit(ifStmtNode it) {}

  @Override
  public void visit(whileStmtNode it) {}

  @Override
  public void visit(forStmtNode it) {}

  @Override
  public void visit(continueStmtNode it) {}

  @Override
  public void visit(breakStmtNode it) {}

  @Override
  public void visit(returnStmtNode it) {}

  @Override
  public void visit(singleExprStmtNode it) {}

  @Override
  public void visit(varDefStmtNode it) {}

  @Override
  public void visit(emptyStmtNode it) {}

  @Override
  public void visit(parenExprNode it) {}

  @Override
  public void visit(atomExprNode it) {}

  @Override
  public void visit(memberExprNode it) {}

  @Override
  public void visit(arrayExprNode it) {}

  @Override
  public void visit(funcExprNode it) {}

  @Override
  public void visit(newExprNode it) {}

  @Override
  public void visit(selfExprNode it) {}

  @Override
  public void visit(unaryExprNode it) {}

  @Override
  public void visit(binaryExprNode it) {}

  @Override
  public void visit(ternaryExprNode it) {}

  @Override
  public void visit(formatStrExprNode it) {}

  @Override
  public void visit(assignExprNode it) {}
}
