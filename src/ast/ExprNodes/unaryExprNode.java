package ast.ExprNodes;
import ast.ASTVisitor;
import utility.position;

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
