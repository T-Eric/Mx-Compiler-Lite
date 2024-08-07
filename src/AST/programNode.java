package AST;

import Util.position;
import java.util.ArrayList;

public class programNode extends ASTNode {
  public ArrayList<programPartNode> parts;

  public programNode(position pos) {
    super(pos);
    parts = new ArrayList<>();
  }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
