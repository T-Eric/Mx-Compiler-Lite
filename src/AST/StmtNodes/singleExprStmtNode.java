package AST.StmtNodes;

import AST.ASTVisitor;
import AST.ExprNodes.ExprNode;
import Util.position;

public class singleExprStmtNode extends StmtNode {
  public ExprNode expr = null;

  public singleExprStmtNode(position pos) {
    super(pos);
    type = StmtType.SingleExpr;
  }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
