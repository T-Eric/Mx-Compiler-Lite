package midend.asm.asmassets.statements;

import java.util.ArrayList;
import java.util.HashMap;
import midend.asm.asmassets.asmId;
import midend.asm.asmassets.asmId.AsmType;
import midend.asm.asmassets.asmStack;
import midend.asm.asmassets.asmStatement;
import midend.llvm_ir.irassets.statements.irFunc;

public class asmFunc extends asmStatement {
  public irFunc ir = null;
  // starts with stack allocation and ends with stack recycle
  // 开头要.globl funcname，.type <funcName>,@function
  public static int LfuncEnd = 0;
  public static HashMap<irFunc, asmFunc> irFuncMap = new HashMap<>();
  public ArrayList<asmId> args = new ArrayList<>();
  public asmStack stack = null;
  public ArrayList<asmBlock> blocks = new ArrayList<>();
  // 末尾要.Lfunc_endi、、.size <func_name>, .Lfunc_endi-<func_name>

  public asmId label;
  public asmId raAddr = null;

  public asmFunc(irFunc ir) { // irStatement = irFunc
    this.ir = ir;
    irFuncMap.put(ir, this);

    stack = new asmStack();
    label = new asmId(AsmType.Label, null);
    label.info = ir.name; // entry block label, also function name
  }

  public void setInit() {
    // TODO 可能根据最终栈大小，对开头结尾的addi sp, ra和s0进行处理
  }

  // TODO
  // 一开始不知道要分配的栈大小，可能要在这里进行开头结尾套餐，或者函数结束后用setInit处理
  @Override
  public String toString() {
    // setInit();
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
