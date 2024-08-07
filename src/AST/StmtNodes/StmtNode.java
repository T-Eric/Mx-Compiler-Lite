package AST.StmtNodes;

import AST.ASTNode;
import Util.position;

// father class of all statement ASTNode

public abstract class StmtNode extends ASTNode {
  public StmtType type;

  public StmtNode(position pos) { super(pos); }

  public enum StmtType {
    Suite,
    If,
    While,
    For,
    Continue,
    Break,
    Return,
    SingleExpr,
    VarDef,
    Empty,
  }
}
