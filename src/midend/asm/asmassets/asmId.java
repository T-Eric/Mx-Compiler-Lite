package midend.asm.asmassets;

import java.util.ArrayList;
import java.util.HashMap;
import midend.llvm_ir.irassets.irId;
import midend.llvm_ir.irassets.irId.IdType;

// 使用栈上空间代替
// idMap<irId,asmId>，将ir的变量、label等转化为asm的
public class asmId {
  public static HashMap<irId, asmId> idMap = new HashMap<>();

  public AsmType asmType = null;
  public RegName reg = null;
  public int imm = 0;
  public String info = null;
  public int offset = 0; // address(sp)
  public irId ir = null;
  public ArrayList<asmId> link = new ArrayList<>();

  public int hasBlocks = 1;

  // globl
  public String globalInfo = null;
  public boolean isString = false;
  public int actualSize = -1; // stringconst专用
  public asmId originVar = null;
  public boolean isLo = false;
  // public RegName loReg = null;//用上面的reg即可
  public int space = 4; // space是堆上或全局元素的

  // class，稍微标注一下这个值是哪个类的
  public asmClass theClass = null; //
  public int size = -1;            // 其在内存中的大小

  // 由getelementptr引发的血案：引入多级寄存器
  public RegName pointer = null;      // 标识是否是堆指针
  public boolean pointToHeap = false; //标识是否指向堆指针
  public int derefOffset = 0;
  // public boolean classPointToHeap=false;

  public asmId(AsmType asmType, irId ir) {
    if (ir != null) {
      assert (!idMap.containsKey(ir));
      idMap.put(ir, this);
    }
    this.asmType = asmType;
    this.ir = ir;
    switch (asmType) {
    case Global:
      if (ir != null)
        info = ir.globalName;
      break;
    case Address:
      reg = RegName.sp;
      break;
    case Register:
      reg = RegName.Virtual;
    case Immediate:
      assert ir.type == IdType.Constant;
      imm = ir.constValue;
    case Label:
      if (ir != null) {
        assert ir.type == IdType.Label;
        info = Integer.toString(ir.id);
      }
      break;
    default:
      break;
    }
  }

  public asmId(RegName reg) {
    this.asmType = AsmType.Register;
    this.reg = reg;
  }

  public asmId(int imm) {
    this.asmType = AsmType.Immediate;
    this.imm = imm;
  }

  public asmId(String info) {
    this.asmType = AsmType.Info;
    this.info = info;
  }

  public asmId(int offset, RegName reg) {
    this.asmType = AsmType.Address;
    this.offset = offset;
    this.reg = reg;
  }

  public void setAddress(int offset, RegName reg) {
    this.asmType = AsmType.Address;
    this.offset = offset;
    this.reg = reg;
  }

  public void linkAddress() {
    if (link != null) {
      for (var lk : link) {
        lk.setAddress(offset, reg);
        lk.info = info;
        lk.ir = ir;
        lk.hasBlocks = hasBlocks;
        lk.globalInfo = globalInfo;
        lk.isString = isString;
        lk.actualSize = actualSize;
        lk.originVar = originVar;
        lk.isLo = isLo;
        lk.space = space;
        lk.theClass = theClass;
        lk.size = size;
        lk.pointer = pointer;
        lk.pointToHeap = pointToHeap;
        lk.derefOffset = derefOffset;
      }
    }
  }

  public void setLabel(String prefix) { info = prefix + "." + info; }

  public void setGlobalPart(asmId orig, boolean lo, RegName re) {
    globalInfo = orig.globalInfo;
    isString = orig.isString;
    actualSize = orig.actualSize;
    originVar = orig;
    info = orig.info;
    isLo = lo;
    space = orig.space;
    reg = re;
  }

  public asmId getDeref() {
    assert !(pointer == null);
    asmId ret = new asmId(derefOffset, pointer);
    return ret;
  }

  public asmId copyAddr() {
    asmId ret = new asmId(offset, reg);
    ret.space = space;
    ret.derefOffset = derefOffset;
    ret.pointer = pointer;
    ret.pointToHeap = pointToHeap;
    ret.hasBlocks = hasBlocks;
    ret.isString = isString;

    ret.size = size;
    ret.space = space;
    ret.theClass = theClass;

    link.add(ret);
    return ret;
  }

  public String toString() {
    switch (asmType) {
    case Register:
      return reg.toString();
    case Address:
      return Integer.toString(offset) + "(" + reg.toString() + ")";
    case Immediate:
      return Integer.toString(imm);
    case Global: {
      if (isLo) {
        if (isString)
          return "%" + String.format("lo(%s)", info);
        else
          return "%" + String.format("lo(%s)(%s)", info, reg.toString());
      } else
        return "%" + String.format("hi(%s)", info);
    }
    case Label: {
      return info;
    }
    default:
      throw new RuntimeException();
    }
  }

  public enum AsmType {
    Global,
    Address,
    Immediate,
    Register,
    Label,
    Info,
    Hi,
    Lo
  }

  public enum RegName {
    zero,
    ra,
    sp,
    gp,
    tp, // 0-4
    t0,
    t1,
    t2, // 5-7
    fp,
    s1, // 8,9
    a0,
    a1,
    a2,
    a3,
    a4,
    a5,
    a6,
    a7, // 10-17
    s2,
    s3,
    s4,
    s5,
    s6,
    s7,
    s8,
    s9,
    s10,
    s11, // 18-27
    t3,
    t4,
    t5,
    t6,     // 28-31
    Virtual // When Unoptimized
  }
}
