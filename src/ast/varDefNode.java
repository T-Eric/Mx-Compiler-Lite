package ast;

import java.util.ArrayList;

import utility.position;

public class varDefNode extends ASTNode {
  public typeNode type = null;
  public ArrayList<varTermNode> varTerms=new ArrayList<>();

  public varDefNode(position pos) { super(pos); }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
