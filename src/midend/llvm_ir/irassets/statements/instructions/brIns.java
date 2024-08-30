package midend.llvm_ir.irassets.statements.instructions;

import midend.llvm_ir.irassets.irId;
import midend.llvm_ir.irassets.statements.irIns;

public class brIns extends irIns {
  public irId cond = null, trueLabel = null, falseLabel = null;

  public brIns() { this.type = InsType.Br; }

  public brIns(irId result) {
    this.type = InsType.Br;
    this.result = result;
  }

  @Override
  public void genIndex() {
    // as terminal no need to give it an index
  }

  @Override
  public String toString() {
    if (cond != null)
      return String.format("br %s %s, label %s, label %s", cond.valueType, cond,
                           trueLabel, falseLabel);
    else
      return String.format("br label %s", trueLabel);
  }
}
