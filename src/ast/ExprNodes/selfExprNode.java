package ast.ExprNodes;

import ast.ASTVisitor;
import utility.position;

public class selfExprNode extends ExprNode {
  public ExprNode expr = null;

  public selfExprNode(position pos) {
    super(pos);
    exprType = ExprType.Self;
  }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
