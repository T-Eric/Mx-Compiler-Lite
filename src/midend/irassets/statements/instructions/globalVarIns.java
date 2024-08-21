package midend.irassets.statements.instructions;

import midend.irassets.statements.irIns;

public class globalVarIns extends irIns {
  public String globalVarInfo = null;

  public globalVarIns() { this.type = InsType.GlobalVar; }

  @Override
  public void genIndex() {}

  @Override
  public String toString() {
    var insType = (result == null) ? null : result.valueType;
    if (insType.arrayLength != -1) // String
      return String.format("%s = private unnamed_addr constant %s c\"%s\"",
                           result, insType.getDeref(), globalVarInfo);
    else // globalVar
      return String.format("%s = global %s %s", result, insType.getDeref(),
                           insType.getDeref().toZeroInitString());
  }
}
