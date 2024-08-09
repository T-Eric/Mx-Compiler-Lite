package ast.StmtNodes;
import ast.ASTVisitor;
import ast.ExprNodes.ExprNode;
import utility.position;

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
