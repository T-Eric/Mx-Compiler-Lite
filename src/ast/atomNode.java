package ast;

import midend.llvm_ir.irassets.irId;
import utility.Type;
import utility.position;

public class atomNode extends ASTNode {
  public AtomType atomType = null;
  public String identifier = null;
  public String stringConst = null;
  public Integer decimalInt = null;
  public boolean bool = false;

  public Type varType = null; // as a variable, which type
  public irId value = null;   // ir

  public atomNode(position pos) { super(pos); }

  public enum AtomType {
    This,
    Identifier,
    StringConst,
    DecimalInt,
    Bool,
    Null,
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
