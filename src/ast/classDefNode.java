package ast;
import java.util.ArrayList;

import utility.position;

public class classDefNode extends ASTNode {
  public boolean builtin = false; // if builtin, such class should not be here
  public String name = null;
  public ArrayList<funcDefNode> methodDefs;
  public ArrayList<varDefNode> varDefs;

  public classDefNode(position pos) {
    super(pos);
    methodDefs = new ArrayList<>();
    varDefs = new ArrayList<>();
  }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
