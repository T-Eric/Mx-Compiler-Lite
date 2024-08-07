package AST;
import AST.ExprNodes.ExprNode;
import Util.position;

public class varTermNode extends ASTNode {
  public String name = null;
  public ExprNode assignExpr = null;
  public exprListNode constructArgs = null;

  public varTermNode(position pos) { super(pos); }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
