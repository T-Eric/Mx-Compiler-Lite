package AST.StmtNodes;
import Util.position;
import java.util.ArrayList;

public class suiteStmtNode extends StmtNode {
  public ArrayList<StmtNode>stmts;

  public suiteStmtNode(position pos){
    super(pos);
    this.stmts=new ArrayList<>();
  }
}
