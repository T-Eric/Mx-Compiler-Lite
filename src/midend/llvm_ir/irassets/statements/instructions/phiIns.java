package midend.llvm_ir.irassets.statements.instructions;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import midend.llvm_ir.irassets.irId;
import midend.llvm_ir.irassets.statements.irBlock;
import midend.llvm_ir.irassets.statements.irIns;

public class phiIns extends irIns {
  public HashMap<irId, irId> args = new HashMap<>(); // label-value
  public irBlock atBlock = null;

  public phiIns(irBlock blk) {
    this.type = InsType.Phi;
    this.atBlock = blk;
  }

  @Override
  public void genIndex() {
    result.setIndex();
  }

  @Override
  public String toString() {
    var insType = result.valueType;
    var argStr = new StringBuilder();
    args.forEach(
        (arga,
         argb) -> argStr.append(String.format("[ %s, %s ], ", argb, arga)));
    argStr.delete(argStr.length() - 2, argStr.length());
    return String.format("%s = phi %s %s", result, insType, argStr);
  }

  @Override
  public HashSet<irId> useValue() {
    if (useIds != null)
      return useIds;
    useIds = new HashSet<irId>();
    for (var argval : args.values())
      if (argval.isLocal())
        useIds.add(argval);
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
  public HashSet<irId> useAny() {
    if (useVars != null)
      return useVars;
    useVars = new HashSet<>();
    for (var arg : args.values())
      if (arg.isLocalGlobal())
        useVars.add(arg);
    return useVars;
  }
}
