package midend.llvm_ir.irassets.statements.instructions;

import java.util.Collections;
import java.util.HashSet;
import midend.llvm_ir.irassets.irId;
import midend.llvm_ir.irassets.irId.IdType;
import midend.llvm_ir.irassets.irType;
import midend.llvm_ir.irassets.statements.irIns;

public class storeIns extends irIns {
  public irId storeAddr = null, storeValue = null;

  public storeIns(irType insType) {
    this.type = InsType.Store;
    this.result = new irId(insType);
  }

  @Override
  public void genIndex() {}

  @Override
  public String toString() {
    var insType = (result == null) ? null : result.valueType;
    return String.format("store %s %s, %s %s", insType, storeValue,
                         insType.getPtr(), storeAddr);
  }

  @Override
  public HashSet<irId> useValue() {
    if (useIds != null)
      return useIds;
    if (storeValue.type == IdType.Local)
      useIds = new HashSet<irId>(Collections.singleton(storeValue));
    else
      useIds = new HashSet<irId>();
    useIds.add(storeAddr);
    return useIds;
  }

  @Override
  public HashSet<irId> defValue() {
    if (defIds != null)
      return defIds;
    defIds = new HashSet<irId>();
    return defIds;
  }

  @Override
  public void rewrite(irId origin, irId copy) {
    if (storeAddr.equals(origin))
      storeAddr = copy;
    else if (storeValue.equals(origin))
      storeValue = copy;
  }
}
