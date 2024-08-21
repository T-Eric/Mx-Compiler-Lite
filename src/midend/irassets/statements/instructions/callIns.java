package midend.irassets.statements.instructions;

import java.util.ArrayList;
import midend.irassets.irId;
import midend.irassets.irType;
import midend.irassets.irType.IRType;
import midend.irassets.statements.irFunc;
import midend.irassets.statements.irIns;

public class callIns extends irIns {
  public irFunc func = null;
  public String callName = null;
  public ArrayList<irId> args = new ArrayList<>();

  public callIns(irType insType) {
    this.type = InsType.Call;
    this.result = new irId(insType);
  }

  public callIns(irId result) {
    this.type = InsType.Call;
    this.result = result;
  }

  @Override
  public void genIndex() {
    if (result.valueType.type != IRType.Void)
      result.setIndex();
  }

  @Override
  public String toString() {
    var insType = (result == null) ? null : result.valueType;
    StringBuilder argStr = new StringBuilder();
    args.forEach(
        arg
        -> argStr.append(arg.valueType).append(" ").append(arg).append(", "));
    if (!args.isEmpty())
      argStr.delete(argStr.length() - 2, argStr.length());
    return (insType.type == IRType.Void ? "" : result + " = ") +
        String.format("call %s @%s(%s)", insType, callName, argStr);
  }
}
