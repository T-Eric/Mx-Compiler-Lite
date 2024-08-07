package AST;

import Util.position;

public class programPartNode extends ASTNode {
  public PartType type;
  public classDefNode classDef = null;
  public funcDefNode funcDef = null;
  public varDefNode varDef = null;

  public programPartNode(position pos) { super(pos); }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }

  public enum PartType {
    ClassDef,
    FuncDef,
    VarDef,
  }
}
