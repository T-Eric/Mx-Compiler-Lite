package AST.StmtNodes;
import AST.ASTVisitor;
import AST.varDefNode;
import Util.position;

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
