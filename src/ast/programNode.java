package ast;

import java.util.ArrayList;

import utility.position;

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
