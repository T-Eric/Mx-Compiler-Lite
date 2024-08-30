package midend.llvm_ir.irassets.statements.instructions;

import midend.llvm_ir.irassets.irId;
import midend.llvm_ir.irassets.irType;
import midend.llvm_ir.irassets.statements.irIns;

public class allocaIns extends irIns {
  public irId allocaQuantity = null;

  public allocaIns(irType insType) {
    this.result = new irId(insType.getPtr());
    this.type = InsType.Alloca;
  }

  public allocaIns(irId result) {
    this.type = InsType.Alloca;
    this.result = result;
  }

  @Override
  public void genIndex() {
    result.setIndex();
  }

  @Override
  public String toString() {
    var insType = result == null ? null : result.valueType;
    String qtStr = allocaQuantity == null ? "" : (", i32 " + allocaQuantity);
    return String.format("%s = alloca %s%s", result, insType.getDeref(), qtStr);
  }
}
