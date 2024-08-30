package midend.llvm_ir.irassets.statements.instructions;

import midend.llvm_ir.irassets.irId;
import midend.llvm_ir.irassets.irType;
import midend.llvm_ir.irassets.irType.IRType;
import midend.llvm_ir.irassets.statements.irIns;

public class retIns extends irIns {
  public irId retValue = null;

  public retIns() { this.type = InsType.Ret; }

  public retIns(irType insType) {
    this.type = InsType.Ret;
    this.result = new irId(insType);
  }

  @Override
  public void genIndex() {}

  @Override
  public String toString() {
    var insType = (result == null) ? null : result.valueType;
    if (insType.type == IRType.Void)
      return "ret " + insType;
    else
      return "ret " + insType + " " + retValue;
  }
}
