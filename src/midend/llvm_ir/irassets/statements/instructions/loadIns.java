package midend.llvm_ir.irassets.statements.instructions;

import midend.llvm_ir.irassets.irId;
import midend.llvm_ir.irassets.irType;
import midend.llvm_ir.irassets.statements.irIns;

public class loadIns extends irIns {
  public irId loadAddr = null;
  public loadIns(irType insType) {
    this.type = InsType.Load;
    result = new irId(insType);
  }
  @Override
  public void genIndex() {
    result.setIndex();
  }
  @Override
  public String toString() {
    var insType = (result == null) ? null : result.valueType;
    return String.format("%s = load %s, %s %s", result, insType,
                         insType.getPtr(), loadAddr);
  }
}
