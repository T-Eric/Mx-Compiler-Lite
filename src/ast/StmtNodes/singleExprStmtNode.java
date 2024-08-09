package ast.StmtNodes;

import ast.ASTVisitor;
import ast.ExprNodes.ExprNode;
import utility.position;

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
