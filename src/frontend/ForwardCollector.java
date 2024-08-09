package frontend;

import ast.*;
import ast.ExprNodes.*;
import ast.StmtNodes.*;
import utility.Type;
import utility.scope.globalScope;

// collect all symbols right now!
/*
 * however:
 * 全局变量和局部变量不支持前向引用
 * 作用域为声明开始的位置直到最近的一个块的结束位置
 * this means we only need to fetch classes and variables now
 * process the variables when bfs-building the AST then
 */
public class ForwardCollector implements ASTVisitor {
  public globalScope ms;
  public ForwardCollector(globalScope gs){
    this.ms=gs;
  }

  @Override
  public void visit(programNode it) {
    it.parts.forEach(pt->pt.accept(this));
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
        // do nothing
        // global vars cannot be declared right now
        break;
      default:
        throw new IllegalStateException("Your bad: partType not set!");
    }
  }

  @Override
  public void visit(funcDefNode it) {
    ms.defFunc(it);
  }

  @Override
  public void visit(argListNode it) {}

  @Override
  public void visit(varDefNode it) {}

  @Override
  public void visit(varTermNode it) {}

  // the 
  @Override
  public void visit(classDefNode it) {
    ms.defClass(it);
    Type ctype = new Type(it.name);
    ms.addType(it.name, ctype, it.pos);
  }

  @Override
  public void visit(suiteNode it) {}

  @Override
  public void visit(typeNode it) {}

  @Override
  public void visit(exprListNode it) {}

  @Override
  public void visit(arrayBlockNode it) {}

  @Override
  public void visit(atomNode it) {}

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
