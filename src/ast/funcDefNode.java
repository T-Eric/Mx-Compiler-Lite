package ast;
import utility.position;

public class funcDefNode extends ASTNode {
  public boolean builtin = false;
  public typeNode type = null;
  public String name=null;
  public argListNode argList = null;
  public suiteNode suite = null;

  public funcDefNode(position pos) { super(pos); }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
