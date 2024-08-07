package AST;

import AST.ExprNodes.ExprNode;
import Util.position;
import java.util.ArrayList;

public class exprListNode extends ASTNode {
  public ArrayList<ExprNode> expressions;

  public exprListNode(position pos) {
    super(pos);
    expressions = new ArrayList<>();
  }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
