package AST.ExprNodes;

import AST.ASTNode;
import Util.Type;
import Util.position;

public abstract class ExprNode extends ASTNode {
  public Type valueType;
  public ExprType exprType;
  public OpType opType=OpType.None;
  // public entity val;

  public ExprNode(position pos) { super(pos); }

  public boolean isAssignable() { return false; }

  public enum ExprType {
    Atom,
    Paren,
    Member,
    Array,
    Func,
    New,
    Self,
    Unary,
    Binary,
    Ternary,
    Assign,
    FormatStr,
  }

  public enum OpType {
    None,
    Inc,
    Dec,
    Not,
    Tilde,
    Add,
    Sub,
    Mul,
    Div,
    Mod,
    LShift,
    Rshift,
    Ge,
    Geq,
    Le,
    Leq,
    Eq,
    Ne,
    And,
    Or,
    Xor,
    AndAnd,
    OrOr,
  }
}
