package midend.llvm_ir.irassets.statements.instructions;

import midend.llvm_ir.irassets.irId;
import midend.llvm_ir.irassets.irType;
import midend.llvm_ir.irassets.statements.irIns;

public class icmpIns extends irIns {
  public OpType op;
  public irId lhs, rhs;

  public icmpIns(OpType op, irType insType) {
    this.type = InsType.Icmp;
    this.result = new irId(insType);
    this.op = op;
  }

  public enum OpType { Eq, Ne, Sgt, Sge, Slt, Sle, Ugt, Uge, Ult, Ule }

  @Override
  public void genIndex() {
    result.setIndex();
  }

  @Override
  public String toString() {
    var insType = (result == null) ? null : lhs.valueType;
    String cmpStr = null;
    switch (op) {
    case Eq:
      cmpStr = "eq";
      break;
    case Ne:
      cmpStr = "ne";
      break;
    case Ugt:
      cmpStr = "ugt";
      break;
    case Uge:
      cmpStr = "uge";
      break;
    case Ult:
      cmpStr = "ult";
      break;
    case Ule:
      cmpStr = "ule";
      break;
    case Sgt:
      cmpStr = "sgt";
      break;
    case Sge:
      cmpStr = "sge";
      break;
    case Slt:
      cmpStr = "slt";
      break;
    case Sle:
      cmpStr = "sle";
      break;
    }
    return String.format("%s = icmp %s %s %s, %s", result, cmpStr, insType, lhs,
                         rhs);
  }
}
