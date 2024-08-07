package AST.StmtNodes;
import AST.ASTVisitor;
import AST.ExprNodes.ExprNode;
import Util.position;

public class ifStmtNode extends StmtNode {
  public ExprNode ifCondExpr = null;
  public StmtNode trueStmt = null, falseStmt = null;

  public ifStmtNode(position pos) {
    super(pos);
    type = StmtType.If;
  }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
