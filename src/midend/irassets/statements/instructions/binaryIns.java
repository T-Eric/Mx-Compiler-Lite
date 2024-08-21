package midend.irassets.statements.instructions;

import midend.irassets.irId;
import midend.irassets.irType;
import midend.irassets.statements.irIns;

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
    return String.format("%s = %s %s %s, %s", result, opStr, lhs.valueType,
                         rhs.valueType);
  }
}
