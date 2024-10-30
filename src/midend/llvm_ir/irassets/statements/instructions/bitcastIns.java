package midend.llvm_ir.irassets.statements.instructions;

import java.util.Collections;
import java.util.HashSet;
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

  @Override
  public HashSet<irId> useValue() {
    if (useIds != null)
      return useIds;
    useIds = new HashSet<irId>(Collections.singleton(castPtr));
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
    if (castPtr.equals(origin))
      castPtr = copy;
    // else if (result .equals(origin))
    //   result = copy;
  }

  @Override
  public HashSet<irId> useAny() {
    if (useVars != null)
      return useVars;
    useVars = new HashSet<>(Collections.singleton(castPtr));
    return useVars;
  }
}
