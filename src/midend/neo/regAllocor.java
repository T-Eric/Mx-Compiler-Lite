package midend.neo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;
import midend.llvm_ir.irassets.irId;
import midend.llvm_ir.irassets.irType.IRType;
import midend.llvm_ir.irassets.statements.irFunc;
import midend.neo.asmassets.asmId;
import midend.neo.asmassets.asmId.AsmType;
import midend.neo.asmassets.asmId.RegName;

public class regAllocor {
  public irFunc irfunc = null;
  public HashMap<irId, asmId> idMap = new HashMap<>();

  public ArrayList<asmId> stackVars = new ArrayList<>();
  public ArrayList<asmId> spilledArgs = new ArrayList<>();

  public LinkedList<RegName> emptyRegs = new LinkedList<>();
  public HashSet<RegName> callerSaveRegsUsed = new HashSet<>();
  public HashSet<RegName> calleeSaveRegsUsed = new HashSet<>();

  public TreeSet<irId> active = new TreeSet<>(new CompRight());
  public TreeSet<irId> idlist = new TreeSet<>(new CompLeft());

  public int r = 22;

  public regAllocor(irFunc irfunc) {
    this.irfunc = irfunc;
    // irfunc.ids 存了所有局部变量，irfunc.remainAllocas存了所有复合alloca

    for (var re : availRegs)
      emptyRegs.add(re);
    // 如果有返回值，直接把a0算上
    if (irfunc.retBlock.terminal.result.valueType.type != IRType.Void)
      callerSaveRegsUsed.add(RegName.a0);
  }

  public void allocReg() {
    // 首先为 alloca 的值分配足够的栈空间
    irfunc.remainAllocas.forEach((alc, ins) -> {
      asmId head = new asmId(alc, -1);
      head.size = (ins.allocaQuantity.constValue + 4) * 4;
      stackVars.add(head);
    });

    // 线性扫描这里开始

    for(var arg:irfunc.args){
      if (arg.argIndex < 8) {
        assert arg.actLeft == 0;
        //寄存器参数，刚好把a0~a7用去
        idMap.put(arg, new asmId(arg, getEmptyReg()));
        active.add(arg);
      } else {
        //栈参数，与caller同步
        var asmid = new asmId(arg, -1);
        idMap.put(arg, asmid);
        spilledArgs.add(arg.argIndex - 8, asmid);
      }
    }

    for (var id : irfunc.ids) 
      if (id.argIndex == -1)
        idlist.add(id);

    for (var id : idlist) {
      expireOld(id);
      if (active.size() == r)
        spillAt(id);
      else {
        idMap.put(id, new asmId(id, getEmptyReg()));
        active.add(id);
      }
    }
  }

  void expireOld(irId id) {
    Iterator<irId> iterator =
        active.iterator(); // 替换 YourType 为 active 中元素的实际类型
    while (iterator.hasNext()) {
      var jd = iterator.next();
      if (jd.actRight >= id.actLeft) {
        return; // 一旦遇到满足条件的元素，停止遍历
      }
      iterator.remove(); // 使用迭代器的 remove 方法安全删除元素
      var expired = idMap.get(jd);
      emptyRegs.add(expired.reg);
    }
  }

  void spillAt(irId id) {
    var spill = active.last();
    if (spill.actRight > id.actRight) {
      // 此时 id 还没有自己的asmId
      var asmid = new asmId(id, idMap.get(spill).reg);
      idMap.put(id, asmid);

      var asmspill = idMap.get(spill);
      asmspill.asmType = AsmType.Address;
      asmspill.reg = RegName.sp;
      stackVars.add(asmspill);

      active.remove(spill);
      active.add(id);
    } else {
      var asmid = new asmId(id, -1); // 此时还不能确定其位置
      idMap.put(id, asmid);
      stackVars.add(asmid);
    }
  }

  RegName getEmptyReg() {
    var re = emptyRegs.poll();
    var ord = re.ordinal();
    if (ord >= 10 && ord <= 17 || ord >= 29 && ord <= 31)
      callerSaveRegsUsed.add(re);
    else
      calleeSaveRegsUsed.add(re);
    return re;
  }

  RegName[] availRegs = {
      RegName.a0, RegName.a1, RegName.a2,  RegName.a3, RegName.a4, RegName.a5,
      RegName.a6, RegName.a7, RegName.t4,  RegName.t5, RegName.t6, RegName.s1,
      RegName.s2, RegName.s3, RegName.s4,  RegName.s5, RegName.s6, RegName.s7,
      RegName.s8, RegName.s9, RegName.s10, RegName.s11};
}

class CompLeft implements Comparator<irId> {
  @Override
  public int compare(irId o1, irId o2) {
    return Integer.compare(o1.actLeft, o2.actLeft);
  }
}

class CompRight implements Comparator<irId> {
  @Override
  public int compare(irId o1, irId o2) {
    return Integer.compare(o1.actRight, o2.actRight);
  }
}
