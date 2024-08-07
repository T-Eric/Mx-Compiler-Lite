package AST;

import AST.ExprNodes.ExprNode;
import Util.position;

public class arrayBlockNode extends ASTNode {
  public ExprNode expr = null;

  public arrayBlockNode(position pos) { super(pos); }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
