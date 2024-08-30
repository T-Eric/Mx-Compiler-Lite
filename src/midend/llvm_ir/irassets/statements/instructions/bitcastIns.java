package midend.llvm_ir.irassets.statements.instructions;

import midend.llvm_ir.irassets.irId;
import midend.llvm_ir.irassets.irType;
import midend.llvm_ir.irassets.statements.irIns;

public class bitcastIns extends irIns {
  public irId castPtr;

  public bitcastIns(irType insType) {
    this.type = InsType.Bitcast;
    result = new irId(insType);
  }

  @Override
  public void genIndex() {
    result.setIndex();
  }

  @Override
  public String toString() {
    return String.format("%s = bitcast %s %s to %s", result, castPtr.valueType,
                         castPtr, result.valueType);
  }
}
