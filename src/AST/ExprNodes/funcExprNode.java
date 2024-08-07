package AST.ExprNodes;

import AST.ASTVisitor;
import AST.exprListNode;
import Util.position;

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
