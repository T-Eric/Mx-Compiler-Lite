package AST.StmtNodes;

import AST.ASTNode;
import Util.position;

// father class of all statement ASTNode

public abstract class StmtNode extends ASTNode {
  public StmtNode(position pos){
    super(pos);
  }
}
