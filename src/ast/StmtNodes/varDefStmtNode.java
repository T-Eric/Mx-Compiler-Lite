package ast.StmtNodes;
import ast.ASTVisitor;
import ast.varDefNode;
import utility.position;

public class varDefStmtNode extends StmtNode {
  public varDefNode varDef = null;

  public varDefStmtNode(position pos) {
    super(pos);
    type = StmtType.VarDef;
  }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
