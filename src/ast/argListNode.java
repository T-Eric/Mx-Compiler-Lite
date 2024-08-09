package ast;

import java.util.ArrayList;

import utility.position;

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
