package ast;
import java.util.ArrayList;

import utility.Type;
import utility.position;

public class typeNode extends ASTNode {
  public Type type;
  public ArrayList<arrayBlockNode> blocks=new ArrayList<>();
  public boolean usageHasDimension=false;
  // this is modified by those who call its accept() method
  // if caller is 'argList', 'funcDef' and 'varDef' it's false
  // if caller is 'newExpr' then need to check dimension

  public typeNode(position pos) {
    super(pos);
  }
  
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
