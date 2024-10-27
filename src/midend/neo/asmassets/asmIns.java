package midend.neo.asmassets;

import java.util.ArrayList;
import midend.llvm_ir.irassets.statements.irIns;

public class asmIns {
  public irIns ir = null;
  public OpType op = null;
  public asmId rs1 = null, rs2 = null, imm = null, rd = null; // ins
  public asmId label = null;                                  // call
  // directives
  public DirectiveType directiveType = null;
  public ArrayList<asmId> directiveArgs = new ArrayList<>();
  // global defines,
  /* variable and class pointers:
   *    .type   global.c,@object                # @global.c
        .globl  global.c
        .p2align        2, 0x0
global.c:
        .word   0                               # 0x0
        .size   global.c, 4
   * string const:
        .type   .L.strconst.0,@object           # @.strconst.0
.L.strconst.0:
        .asciz  "true\t\n\""
        .size   .L.strconst.0, 8
   */
  public asmId globleId = null;

  public asmIns(irIns ir, OpType op) {
    this.ir = ir;
    this.op = op;
  }

  public asmIns(OpType op) { this.op = op; }

  public asmIns(DirectiveType dt) {
    op = OpType.Directive;
    directiveType = dt;
  }

  public void setLabel(asmId variable) {
    op = OpType.Label; // todo 以下函数类似, 用 set 函数设置 op 值
    label = variable;
  }

  public void setL(asmId address, asmId dest) {
    rs1 = address;
    rd = dest;
  }

  public void setS(asmId reg, asmId address) {
    rs2 = reg;
    rs1 = address;
  }

  public void setBz(asmId cond, asmId label) {
    rs1 = cond;
    this.label = label;
  }

  public void setJ(asmId label) {
    op = OpType.j;
    this.label = label;
  }

  public void setMv(asmId data, asmId target) {
    op = OpType.mv;
    rs1 = data;
    rd = target;
  }

  public void setLi(asmId imm_, asmId target) {
    rd = target;
    imm = imm_;
  }

  public void setArithImm(asmId op1, asmId imm, asmId result) {
    rs1 = op1;
    this.imm = imm;
    rd = result;
  }

  public void setSz(asmId result, asmId rs) {
    rd = result;
    rs1 = rs;
  }

  public void setFull(asmId result, asmId src1, asmId src2) {
    rd = result;
    rs1 = src1;
    rs2 = src2;
  }

  public void setCall(asmId label) { this.label = label; }

  public String toString() {
    var ret = new StringBuilder();
    if (op == OpType.Global) {
      //.type
      ret.append(".type\t" + globleId.info + ",@object\n");
      //.p2align
      if (!globleId.isString)
        ret.append("\t.p2align\t2\n");
      if (globleId.isString) {
        ret.append(globleId.info).append(":\n");
        ret.append(String.format("\t.asciz\t\"%s\"\n", globleId.globalInfo));
        ret.append(String.format("\t.size\t%s, %d\n", globleId.info,
                                 globleId.actualSize));
      } else {
        ret.append(String.format("\t.globl\t%s\n", globleId.info));
        ret.append(globleId.info).append(":\n");
        ret.append(String.format(
            "\t.%s\t0\n",
            globleId.space == 1 ? "byte" : "word")); // 所有变量都是i32，故都是0
        ret.append(
            String.format("\t.size\t%s, %d\n", globleId.info, globleId.space));
      }
    } else if (op == OpType.Label) {
      ret.append(label).append(":\n");
    } else {
      assert op != OpType.Directive;
      String formatOp = String.format("%1$-8s", op.toString());
      if (op.ordinal() >= OpType.add.ordinal() &&
          op.ordinal() <= OpType.xor.ordinal()) {
        // rd, rs1, rs2
        ret.append(String.format("%s%s, %s, %s", formatOp, rd, rs1, rs2));
      } else if (op.ordinal() == OpType.addi.ordinal() ||
                 op.ordinal() >= OpType.slli.ordinal() &&
                     op.ordinal() <= OpType.xori.ordinal()) {
        // rd, rs1, imm
        ret.append(String.format("%s%s, %s, %s", formatOp, rd, rs1, imm));
      } else if (op == OpType.li || op == OpType.lui) {
        // rd, imm
        ret.append(String.format("%s%s, %s", formatOp, rd, imm));
      } else if (op == OpType.beqz || op == OpType.bnez) {
        // rs1, label
        ret.append(String.format("%s%s, %s", formatOp, rs1, label));
      } else if (op == OpType.j || op == OpType.call) {
        // j label
        ret.append(String.format("%s%s", formatOp, label));
      } else if (op == OpType.lb || op == OpType.lw) {
        // l rd, rs1
        ret.append(String.format("%s%s, %s", formatOp, rd, rs1));
      } else if (op == OpType.sb || op == OpType.sw) {
        // s rs2, rs1
        ret.append(String.format("%s%s, %s", formatOp, rs2, rs1));
      } else if (op.ordinal() >= OpType.seqz.ordinal() &&
                 op.ordinal() <= OpType.sgtz.ordinal()) {
        ret.append(String.format("%s%s, %s", formatOp, rd, rs1));
      } else if (op == OpType.ret) {
        // ret
        ret.append(op);
      } else if (op == OpType.mv) {
        // mv dest, src
        ret.append(String.format("%s%s, %s", formatOp, rd, rs1));
      } else
        throw new UnsupportedOperationException();
    }
    // 注释
    if (ir != null)
      ret.append(String.format("\t\t# %s", ir));

    return ret.toString();
  }

  public enum OpType {
    add,
    sub,
    mul,
    div,
    rem,
    sll,
    sra,
    and,
    or,
    xor, //天才顺序
    addi,
    lw,
    lb,
    sw,
    sb,
    slli,
    srai,
    andi,
    ori,
    xori,
    mv,
    li,
    lui,
    seqz,
    snez,
    sltz,
    sgtz,
    beqz,
    bnez,
    j,
    call,
    ret, //
    Label,
    Directive,
    Global
  }

  public enum DirectiveType { text, file, globl, type, word, size }
}
