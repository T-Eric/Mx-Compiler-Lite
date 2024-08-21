package midend.irassets.statements.instructions;

import java.util.ArrayList;
import midend.irassets.irId;
import midend.irassets.statements.irIns;

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
    return String.format("%s=getelementptr %s, %s %s, %s", result,
                         objectPtr.valueType.getDeref(), objectPtr.valueType,
                         objectPtr, indexStr);
  }
}
