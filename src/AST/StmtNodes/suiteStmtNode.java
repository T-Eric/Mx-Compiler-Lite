package AST.StmtNodes;
import AST.ASTVisitor;
import Util.position;
import java.util.ArrayList;

public class suiteStmtNode extends StmtNode {
  public ArrayList<StmtNode> stmts;

  public suiteStmtNode(position pos) {
    super(pos);
    this.stmts = new ArrayList<>();
    type = StmtType.Suite;
  }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
