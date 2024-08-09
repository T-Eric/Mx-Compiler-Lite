package ast.ExprNodes;

import ast.ASTVisitor;
import ast.exprListNode;
import utility.position;

public class funcExprNode extends ExprNode {
  public ExprNode funcExpr = null;
  public exprListNode args = null;

  public funcExprNode(position pos) {
    super(pos);
    exprType = ExprType.Func;
  }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
