package AST.ExprNodes;
import AST.ASTVisitor;
import Util.position;

public class unaryExprNode extends ExprNode {
  public ExprNode rhsExpr = null;

  public unaryExprNode(position pos) {
    super(pos);
    exprType = ExprType.Unary;
  }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
