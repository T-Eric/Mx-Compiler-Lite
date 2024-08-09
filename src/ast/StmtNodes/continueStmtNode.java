package ast.StmtNodes;
import ast.ASTVisitor;
import utility.position;

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
