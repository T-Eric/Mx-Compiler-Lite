package AST.StmtNodes;
import AST.ASTVisitor;
import Util.position;

public class breakStmtNode extends StmtNode {
  public breakStmtNode(position pos) {
    super(pos);
    type = StmtType.Break;
  }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
