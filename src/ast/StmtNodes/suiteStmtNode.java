package ast.StmtNodes;
import utility.position;

import ast.ASTVisitor;
import ast.suiteNode;

public class suiteStmtNode extends StmtNode {
  public suiteNode suite=null;

  public suiteStmtNode(position pos) {
    super(pos);
    type = StmtType.Suite;
  }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
