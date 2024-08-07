package AST;
import AST.ExprNodes.*;
import AST.StmtNodes.*;

public interface ASTVisitor {
  void visit(programNode it);
  void visit(programPartNode it);
  void visit(funcDefNode it);
  void visit(argListNode it);
  void visit(varDefNode it);
  void visit(varTermNode it);
  void visit(classDefNode it);
  void visit(suiteNode it);
  void visit(typeNode it);
  void visit(exprListNode it);
  void visit(arrayBlockNode it);
  void visit(atomNode it);

  void visit(suiteStmtNode it);
  void visit(ifStmtNode it);
  void visit(whileStmtNode it);
  void visit(forStmtNode it);
  void visit(continueStmtNode it);
  void visit(breakStmtNode it);
  void visit(returnStmtNode it);
  void visit(singleExprStmtNode it);
  void visit(varDefStmtNode it);
  void visit(emptyStmtNode it);

  void visit(parenExprNode it);
  void visit(atomExprNode it);
  void visit(memberExprNode it);
  void visit(arrayExprNode it);
  void visit(funcExprNode it);
  void visit(newExprNode it);
  void visit(selfExprNode it);
  void visit(unaryExprNode it);
  void visit(binaryExprNode it);
  void visit(ternaryExprNode it);
  void visit(formatStrExprNode it);
  void visit(assignExprNode it);
}
// ... 我们暂时不清楚这个visitor干什么用
// 其定义了一堆visit函数，猜想这个函数可以跟MxBaseVisitor联动（占位用），但是暂时不知道，后面补上