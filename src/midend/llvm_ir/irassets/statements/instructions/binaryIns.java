package midend.llvm_ir.irassets.statements.instructions;

import java.util.Collections;
import java.util.HashSet;
import midend.llvm_ir.irassets.irId;
import midend.llvm_ir.irassets.irType;
import midend.llvm_ir.irassets.statements.irIns;

public class binaryIns extends irIns {
  public OpType op = null;
  public irId lhs = null, rhs = null;

  public binaryIns(irType insType) {
    this.type = InsType.Binary;
    this.result = new irId(insType);
  }

  public binaryIns(irId result) {
    this.type = InsType.Binary;
    this.result = result;
  }

  public enum OpType { Add, Sub, Mul, Sdiv, Srem, Shl, Ashr, And, Or, Xor }
  @Override
  public void genIndex() {
    result.setIndex();
  }
  @Override
  public String toString() {
    String opStr = null;
    var insType = (result == null) ? null : result.valueType;
    switch (op) {
    case Add:
      opStr = "add";
      break;
    case Sub:
      opStr = "sub";
      break;
    case Mul:
      opStr = "mul";
      break;
    case Sdiv:
      opStr = "sdiv";
      break;
    case Srem:
      opStr = "srem";
      break;
    case Shl:
      opStr = "shl";
      break;
    case Ashr:
      opStr = "ashr";
      break;
    case And:
      opStr = "and";
      break;
    case Or:
      opStr = "or";
      break;
    case Xor:
      opStr = "xor";
      break;
    }
    return String.format("%s = %s %s %s, %s", result, opStr, insType, lhs, rhs);
  }

  @Override
  public HashSet<irId> useValue() {
    if (useIds != null)
      return useIds;
    useIds = new HashSet<irId>();
    if (lhs.isLocal())
      useIds.add(lhs);
    if (rhs.isLocal())
      useIds.add(rhs);
    return useIds;
  }

  @Override
  public HashSet<irId> defValue() {
    if (defIds != null)
      return defIds;
    if (result.isLocal())
      defIds = new HashSet<>(Collections.singleton(result));
    else
      defIds = new HashSet<>();
    return defIds;
  }

  @Override
  public void rewrite(irId origin, irId copy) {
    if (lhs.equals(origin))
      lhs = copy;
    else if (rhs.equals(origin))
      rhs = copy;
    // else if (result .equals(origin))
    //   result = copy;
  }
}
