package ast.StmtNodes;

import ast.ASTNode;
import utility.Type;
import utility.position;

// father class of all statement ASTNode

public abstract class StmtNode extends ASTNode {
  public StmtType type;
  public Type returnType = null;
  // if the statement contains a return statement
  // we use this to push it up to function scope

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
