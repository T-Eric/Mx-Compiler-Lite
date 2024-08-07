package AST;
import AST.StmtNodes.StmtNode;
import Util.position;
import java.util.ArrayList;

public class suiteNode extends ASTNode {
  public ArrayList<StmtNode> stmts = new ArrayList<>();

  public suiteNode(position pos) { super(pos); }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
