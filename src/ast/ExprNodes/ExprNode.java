package ast.ExprNodes;

import ast.ASTNode;
import midend.llvm_ir.irassets.irId;
import utility.Type;
import utility.position;

public abstract class ExprNode extends ASTNode {
  public Type valueType;
  public ExprType exprType;
  public OpType op = OpType.None;
  public irId value = null;

  public boolean isLvalue = false;

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
    ArrayConst
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
    Shl,
    Shr,
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
