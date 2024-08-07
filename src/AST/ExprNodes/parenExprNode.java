package AST.ExprNodes;

import AST.ASTVisitor;
import Util.position;

public class parenExprNode extends ExprNode {
  public ExprNode expr = null;

  public parenExprNode(position pos) {
    super(pos);
    exprType = ExprType.Paren;
  }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
