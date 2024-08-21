package midend.irassets.statements.instructions;

import midend.irassets.irId;
import midend.irassets.irType;
import midend.irassets.statements.irIns;

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
}
