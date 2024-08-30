package midend.llvm_ir.irassets.statements.instructions;

import midend.llvm_ir.irassets.statements.irIns;

public class declareIns extends irIns {
  public String decInfo = null;

  public declareIns() { this.type = InsType.Declare; }

  @Override
  public void genIndex() {}

  @Override
  public String toString() {
    return decInfo;
  }
}
