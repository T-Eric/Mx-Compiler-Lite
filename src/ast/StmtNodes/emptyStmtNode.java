package ast.StmtNodes;

import ast.ASTVisitor;
import utility.position;

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
