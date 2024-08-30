package midend.llvm_ir.irassets.statements.instructions;

import midend.llvm_ir.irassets.irId;
import midend.llvm_ir.irassets.irType;
import midend.llvm_ir.irassets.statements.irIns;

public class selectIns extends irIns {
  public irId cond = null, trueValue = null, falseValue = null;

  public selectIns(irType insType) {
    this.type = InsType.Select;
    this.result = new irId(insType);
  }

  @Override
  public void genIndex() {
    result.setIndex();
  }

  @Override
  public String toString() {
    var insType = result.valueType;
    return String.format("%s = select %s %s, %s %s, %s %s", result,
                         cond.valueType, cond, insType, trueValue, insType,
                         falseValue);
  }
}
