package ast;

import java.util.ArrayList;
import midend.llvm_ir.irassets.irId;
import utility.Type;
import utility.position;

public class arrayConstNode extends ASTNode {
  // all elements' type must be generalType or Empty
  public ArrayList<ArrayConstType> eachType = new ArrayList<>();
  public Type varType = null;

  public ArrayList<atomNode> atoms = new ArrayList<>();
  public ArrayList<arrayConstNode> arrayConst = new ArrayList<>();

  public irId value = null; // ir
  public int fullSize=0;// ir
  public int actualLength=0;
  public boolean isFirst=false;

  public arrayConstNode(position pos) { super(pos); }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }

  public enum ArrayConstType { Empty, Int, String, Bool, ArrayConst }
}
