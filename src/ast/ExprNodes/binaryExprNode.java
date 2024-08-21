package ast.ExprNodes;
import ast.ASTVisitor;
import utility.position;

public class binaryExprNode extends ExprNode {
  public ExprNode lhsExpr = null, rhsExpr = null;

  public binaryExprNode(position pos) {
    super(pos);
    exprType = ExprType.Binary;
  }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
