package midend.llvm_ir.irassets.statements.instructions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import midend.llvm_ir.irassets.irId;
import midend.llvm_ir.irassets.irId.IdType;
import midend.llvm_ir.irassets.irType;
import midend.llvm_ir.irassets.irType.IRType;
import midend.llvm_ir.irassets.statements.irFunc;
import midend.llvm_ir.irassets.statements.irIns;

public class callIns extends irIns {
  public irFunc func = null;
  public String callName = null;
  public ArrayList<irId> args = new ArrayList<>();
  public boolean builtin = false;

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

  @Override
  public HashSet<irId> useValue() {
    if (useIds != null)
      return useIds;
    useIds = new HashSet<irId>();
    for (var arg : args)
      if (arg.type == IdType.Local)
        useIds.add(arg);
    return useIds;
  }

  @Override
  public HashSet<irId> defValue() {
    if (defIds != null)
      return defIds;
    if (result.valueType.type == IRType.Void)
      defIds = new HashSet<irId>();
    else
      defIds = new HashSet<irId>(Collections.singleton(result));
    return defIds;
  }

  @Override
  public void rewrite(irId origin, irId copy) {
    for (int i = 0; i < args.size(); ++i)
      if (args.get(i).equals(origin))
        args.set(i, copy);
    // if(result==origin)result=copy;
  }
}
