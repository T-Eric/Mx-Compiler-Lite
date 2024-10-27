package midend.llvm_ir.irassets.statements.instructions;

import java.util.Collections;
import java.util.HashSet;
import midend.llvm_ir.irassets.irId;
import midend.llvm_ir.irassets.irId.IdType;
import midend.llvm_ir.irassets.statements.irIns;

public class moveIns extends irIns {
  public irId src = null;

  public moveIns(irId result, irId rhs) {
    this.type = InsType.Move;
    this.result = result;
    this.src = rhs;
  }

  @Override
  public HashSet<irId> useValue() {
    if (useIds != null)
      return useIds;
    if (src.isLocal())
      useIds = new HashSet<irId>(Collections.singleton(src));
    else
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

  @Override
  public void genIndex() {
    result.setIndex();
  }

  @Override
  public String toString() {
    assert result.type != IdType.Global && src.type != IdType.Global;
    return String.format("%s <- %s", result, src);
  }
}
