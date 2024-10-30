package midend.llvm_ir.irassets.statements.instructions;

import java.util.Collections;
import java.util.HashSet;
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

  @Override
  public HashSet<irId> useValue() {
    if (useIds != null)
      return useIds;
    if (cond != null && cond.isLocal())
      useIds = new HashSet<irId>(Collections.singleton(cond));
    else
      useIds = new HashSet<irId>();
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
    if (cond == origin)
      cond = copy;
  }

  @Override
  public HashSet<irId> useAny() {
    if (useVars != null)
      return useVars;
    useVars = new HashSet<>();
    if (cond != null && cond.isLocalGlobal())
      useVars.add(cond);
    return useVars;
  }
}
