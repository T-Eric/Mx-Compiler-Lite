package AST.StmtNodes;

import AST.ASTVisitor;
import Util.position;

public class emptyStmtNode extends StmtNode {
  public emptyStmtNode(position pos) {
    super(pos);
    type = StmtType.Empty;
  }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
