package midend.llvm_ir.irassets.statements.instructions;

import java.util.Collections;
import java.util.HashSet;
import midend.llvm_ir.irassets.irId;
import midend.llvm_ir.irassets.irId.IdType;
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

  @Override
  public HashSet<irId> useValue() {
    if (useIds != null)
      return useIds;
    if (result.valueType.type == IRType.Void ||
        retValue.type == IdType.Constant)
      useIds = new HashSet<irId>();
    else
      useIds = new HashSet<irId>(Collections.singleton(retValue));
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
    if (retValue.equals(origin))
      retValue = copy;
  }

  @Override
  public HashSet<irId> useAny() {
    if (useVars != null)
      return useVars;
    if (result.valueType.type == IRType.Void ||
        retValue.type == IdType.Constant)
      useVars = new HashSet<irId>();
    else
      useVars = new HashSet<irId>(Collections.singleton(retValue));
    return useVars;
  }
}
