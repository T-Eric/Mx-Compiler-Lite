package AST.StmtNodes;
import AST.ASTVisitor;
import Util.position;

public class continueStmtNode extends StmtNode {
  public continueStmtNode(position pos) {
    super(pos);
    type = StmtType.Continue;
  }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
