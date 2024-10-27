package midend.llvm_ir.irassets.statements.instructions;

import java.util.Collections;
import java.util.HashSet;
import midend.llvm_ir.irassets.irId;
import midend.llvm_ir.irassets.irId.IdType;
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
  @Override
  public HashSet<irId> useValue() {
    if (useIds != null)
      return useIds;
    useIds = loadAddr.type == IdType.Local
                 ? new HashSet<irId>(Collections.singleton(loadAddr))
                 : new HashSet<>();
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
  public void rewrite(irId origin, irId copy) {
    if (loadAddr.equals(origin))
      loadAddr = copy;
    // else if (result.equals(origin))
    //   result = copy;
  }
}
