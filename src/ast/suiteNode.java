package ast;
import ast.StmtNodes.StmtNode;
import java.util.ArrayList;
import utility.Type;
import utility.position;

public class suiteNode extends ASTNode {
  public ArrayList<StmtNode> stmts = new ArrayList<>();
  public Type returnType = null; // for funcDef's return type check

  public suiteNode(position pos) { super(pos); }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
