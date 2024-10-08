package ast.StmtNodes;

import ast.ASTVisitor;
import ast.ExprNodes.ExprNode;
import utility.position;

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
