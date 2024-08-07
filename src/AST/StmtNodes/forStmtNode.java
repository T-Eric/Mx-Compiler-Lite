package AST.StmtNodes;
import AST.ASTVisitor;
import AST.ExprNodes.ExprNode;
import AST.varDefNode;
import Util.position;

public class forStmtNode extends StmtNode {
  public ExprNode initExpr = null, condExpr = null, stepExpr = null;
  public Boolean initWithVarDef = null; // whether the form is "int i=1"
  public varDefNode initVarDef = null;
  public StmtNode forBodyStmt = null;

  public forStmtNode(position pos) {
    super(pos);
    type = StmtType.For;
  }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
