package midend.neo.asmassets;

import java.util.ArrayList;
import midend.llvm_ir.irassets.statements.irFunc;
import midend.neo.asmassets.asmId.AsmType;
import midend.neo.regAllocor;

public class asmFunc {
  public irFunc ir = null;
  // starts with stack allocation and ends with stack recycle
  // 开头要.globl funcname，.type <funcName>,@function
  public static int LfuncEnd = 0;
  // public static HashMap<irFunc, asmFunc> irFuncMap = new HashMap<>();
  public ArrayList<asmId> args = new ArrayList<>();
  public asmStack stack = null;
  public regAllocor rac = null;
  public ArrayList<asmBlock> blocks = new ArrayList<>();
  // 末尾要.Lfunc_endi、、.size <func_name>, .Lfunc_endi-<func_name>

  public asmId label;
  public asmIns ret = null; // 暂存的ret语句

  public asmFunc(irFunc ir) { // irStatement = irFunc
    this.ir = ir;
    // irFuncMap.put(ir, this);

    stack = new asmStack();
    rac = new regAllocor(ir);
    label = new asmId(AsmType.Label, null);
    label.info = ir.name; // entry block label, also function name
  }

  @Override
  public String toString() {
    var ret = new StringBuilder();
    ret.append("\t.globl\t" + label.info + "\n");
    ret.append("\t.type\t" + label.info + ",@function\n");
    // 第一个block没有Label，使用函数名
    blocks.get(0).label = label;
    boolean first = true;
    for (var blk : blocks) {
      if (first) {
        first = false;
        blk.label = label;
      } else {
        blk.label.setLabel(ir.name);
      }
      ret.append(blk);
    }
    ret.append(".lfunc_end" + Integer.toString(LfuncEnd) + ":\n\t.size\t" +
               label.info + ", .lfunc_end" + Integer.toString(LfuncEnd++) +
               "-" + label.info + "\n\n");
    return ret.toString();
  }
}
