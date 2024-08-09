package ast;

import ast.ExprNodes.ExprNode;
import utility.position;

public class arrayBlockNode extends ASTNode {
  public ExprNode expr = null;

  public arrayBlockNode(position pos) { super(pos); }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
