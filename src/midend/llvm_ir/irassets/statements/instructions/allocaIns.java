package midend.llvm_ir.irassets.statements.instructions;

import java.util.Collections;
import java.util.HashSet;
import midend.llvm_ir.irassets.irId;
import midend.llvm_ir.irassets.irType;
import midend.llvm_ir.irassets.statements.irIns;

public class allocaIns extends irIns {
  public irId allocaQuantity = null;
  // 可以保证Quantity一定是const

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

  // TODO alloca 迟早要消除，这里的use和def仅起到暂时作用
  @Override
  public HashSet<irId> useValue() {
    if (useIds != null)
      return useIds;
    useIds = new HashSet<>();
    return useIds;
  }

  @Override
  public HashSet<irId> defValue() {
    if (defIds != null)
      return defIds;
    defIds = new HashSet<irId>(Collections.singleton(result));
    return defIds;
  }

  @Override
  public void rewrite(irId origin, irId copy) {}
}
