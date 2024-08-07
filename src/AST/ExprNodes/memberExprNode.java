package AST.ExprNodes;
import AST.ASTVisitor;
import Util.position;

public class memberExprNode extends ExprNode {
  public ExprNode expr = null;
  public String id = null; // the identifier

  public memberExprNode(position pos) {
    super(pos);
    exprType = ExprType.Member;
  }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
