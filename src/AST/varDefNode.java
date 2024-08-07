package AST;

import Util.position;
import java.util.ArrayList;

public class varDefNode extends ASTNode {
  public typeNode type = null;
  public ArrayList<varTermNode> varTerms;

  public varDefNode(position pos) { super(pos); }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
