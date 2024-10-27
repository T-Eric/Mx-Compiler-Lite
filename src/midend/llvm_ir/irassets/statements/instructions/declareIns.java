package midend.llvm_ir.irassets.statements.instructions;

import java.util.HashSet;
import midend.llvm_ir.irassets.irId;
import midend.llvm_ir.irassets.statements.irIns;

public class declareIns extends irIns {
  public String decInfo = null;

  public declareIns() { this.type = InsType.Declare; }

  public declareIns(String decInfo) {
    // 可以用作asm阶段的comment
    this.type = InsType.Declare;
    this.decInfo = decInfo;
  }

  @Override
  public void genIndex() {}

  @Override
  public String toString() {
    return decInfo;
  }

  @Override
  public HashSet<irId> useValue() {
    return null;
  }

  @Override
  public HashSet<irId> defValue() {
    return null;
  }

  @Override
  public void rewrite(irId origin, irId copy) {}
}
