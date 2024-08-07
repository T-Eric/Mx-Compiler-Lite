package AST.StmtNodes;

import AST.ASTVisitor;
import AST.ExprNodes.ExprNode;
import Util.position;

public class returnStmtNode extends StmtNode {
  public ExprNode retExpr = null;

  public returnStmtNode(position pos) {
    super(pos);
    type = StmtType.Return;
  }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
