package AST;

import Util.position;
import java.util.ArrayList;

public class argListNode extends ASTNode {
  public ArrayList<typeNode> types;
  public ArrayList<String> identifiers;

  public argListNode(position pos) {
    super(pos);
    types = new ArrayList<>();
    identifiers = new ArrayList<>();
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
