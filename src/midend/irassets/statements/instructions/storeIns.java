package midend.irassets.statements.instructions;

import midend.irassets.irId;
import midend.irassets.irType;
import midend.irassets.statements.irIns;

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
    return String.format("store %s %s, %s %s", insType, storeValue, insType.getPtr(),
                         storeAddr);
  }
}
