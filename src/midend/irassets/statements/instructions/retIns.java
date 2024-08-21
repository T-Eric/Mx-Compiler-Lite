package midend.irassets.statements.instructions;

import midend.irassets.irId;
import midend.irassets.irType;
import midend.irassets.irType.IRType;
import midend.irassets.statements.irIns;

public class retIns extends irIns {
  public irId retValue = null;

  public retIns() { this.type = InsType.Ret; }

  public retIns(irType insType) {
    this.type = InsType.Ret;
    this.retValue = new irId(insType);
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
}
