package ast.StmtNodes;
import ast.ASTVisitor;
import ast.ExprNodes.ExprNode;
import utility.position;

public class whileStmtNode extends StmtNode {
  public ExprNode condExpr = null;
  public StmtNode bodyStmt = null;

  public whileStmtNode(position pos) {
    super(pos);
    type = StmtType.While;
  }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
