package AST.ExprNodes;
import AST.ASTVisitor;
import Util.position;

public class ternaryExprNode extends ExprNode {
  public ExprNode condExpr = null, lhsExpr = null, rhsExpr = null;

  public ternaryExprNode(position pos) {
    super(pos);
    exprType = ExprType.Ternary;
  }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
