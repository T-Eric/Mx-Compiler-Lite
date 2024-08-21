package midend.irassets.statements.instructions;

import midend.irassets.irId;
import midend.irassets.irType;
import midend.irassets.statements.irIns;

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
}
