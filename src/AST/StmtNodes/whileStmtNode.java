package AST.StmtNodes;
import AST.ASTVisitor;
import AST.ExprNodes.ExprNode;
import Util.position;

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
