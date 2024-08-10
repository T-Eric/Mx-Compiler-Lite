package ast;

import java.util.ArrayList;

import utility.Type;
import utility.position;

public class argListNode extends ASTNode {
  public ArrayList<typeNode> types=new ArrayList<>();
  public ArrayList<String> identifiers=new ArrayList<>();
  public ArrayList<Type> returnTypes=new ArrayList<>();

  public argListNode(position pos) {
    super(pos);
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
