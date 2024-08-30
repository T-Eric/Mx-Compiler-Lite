package midend.asm.asmassets.statements;

import java.util.ArrayList;
import java.util.HashMap;
import midend.asm.asmassets.asmId;
import midend.asm.asmassets.asmId.AsmType;
import midend.asm.asmassets.asmStatement;
import midend.llvm_ir.irassets.irId;
import midend.llvm_ir.irassets.statements.irBlock;

public class asmBlock extends asmStatement {
  public irBlock ir = null;
  public String funcName = null;
  public static HashMap<irBlock, asmBlock> irBlockMap = new HashMap<>();
  public static HashMap<irId, asmBlock> irLabelMap = new HashMap<>();

  public asmId label = null;
  public ArrayList<asmIns> instructions = new ArrayList<>();

  public ArrayList<asmIns> pre = new ArrayList<>();
  public boolean isSection = false;

  public asmBlock(irBlock ir, boolean explicitLabel) {
    this.ir = ir;
    if (ir != null && explicitLabel) {
      irBlockMap.put(ir, this);
      irLabelMap.put(
          ir.label,
          this); // 只有explicit的label才有可能被跳转，否则是被当成函数call
      label = new asmId(AsmType.Label, ir.label);
    }
  }

  @Override
  public String toString() {
    var ret = new StringBuilder();
    if (label != null) {
      if (isSection)
        ret.append(label.info).append("\n");
      else
        ret.append(label.info).append(":\n");
    }
    pre.forEach(ins -> ret.append("\t").append(ins).append("\n"));
    instructions.forEach(ins -> ret.append("\t").append(ins).append("\n"));
    return ret.toString();
  }
}
