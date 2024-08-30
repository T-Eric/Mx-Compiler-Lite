package midend.llvm_ir.irassets.statements;

import java.util.ArrayList;
import midend.llvm_ir.irassets.irId;
import midend.llvm_ir.irassets.irStatement;

public class irBlock extends irStatement {
  public static boolean isFirst = true;
  public irId label = new irId(irId.IdType.Label);
  public ArrayList<irIns> instructions = new ArrayList<>();
  public irIns terminal = null; // br and ret

  public static void setFirstBlock() { isFirst = true; }

  @Override
  public void genIndex() {
    label.setIndex();
    instructions.forEach(ins -> ins.genIndex());
  }

  @Override
  public String toString() {
    StringBuilder ret = new StringBuilder();
    int labelId = label.getLabel();
    if (!isFirst)
      ret.append(labelId).append(":\n");
    else
      isFirst = false;
    instructions.forEach(ins -> ret.append("  ").append(ins).append("\n"));
    ret.append("  ").append(terminal).append("\n");
    return ret.toString();
  }
}
