package ast.StmtNodes;
import ast.ASTVisitor;
import ast.varDefNode;
import ast.ExprNodes.ExprNode;
import utility.position;

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
