package midend.llvm_ir.irassets.statements.instructions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import midend.llvm_ir.irassets.irId;
import midend.llvm_ir.irassets.irId.IdType;
import midend.llvm_ir.irassets.statements.irIns;

public class getelementptrIns extends irIns {
  public irId objectPtr = null;
  public ArrayList<irId> indices = new ArrayList<>();

  public getelementptrIns() { this.type = InsType.Getelementptr; }

  @Override
  public void genIndex() {
    result.setIndex();
  }

  @Override
  public String toString() {
    StringBuilder indexStr = new StringBuilder();
    indices.forEach(
        index
        -> indexStr.append(String.format("%s %s, ", index.valueType, index)));
    indexStr.delete(indexStr.length() - 2, indexStr.length());
    return String.format("%s = getelementptr %s, %s %s, %s", result,
                         objectPtr.valueType.getDeref(), objectPtr.valueType,
                         objectPtr, indexStr);
  }

  @Override
  public HashSet<irId> useValue() {
    if (useIds != null)
      return useIds;
    useIds = new HashSet<irId>();
    for (var index : indices)
      if (index.type == IdType.Local)
        useIds.add(index);
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
    for (int i = 0; i < indices.size(); ++i)
      if (indices.get(i).equals(origin))
        indices.set(i, copy);
    if (objectPtr.equals(origin))
      objectPtr = copy;
  }
}
