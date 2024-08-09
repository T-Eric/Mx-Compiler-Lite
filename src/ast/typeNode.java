package ast;
import java.util.ArrayList;

import utility.Type;
import utility.position;

public class typeNode extends ASTNode {
  public Type type;
  public ArrayList<arrayBlockNode> blocks=new ArrayList<>();

  public typeNode(position pos) {
    super(pos);
  }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
