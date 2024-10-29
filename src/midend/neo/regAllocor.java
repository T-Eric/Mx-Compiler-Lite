package midend.neo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;
import midend.llvm_ir.irassets.irId;
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
  public HashSet<RegName> SaveRegsUsed = new HashSet<>();

  public TreeSet<irId> active = new TreeSet<>(new CompRight());
  public TreeSet<irId> idlist = new TreeSet<>(new CompLeft());

  public int r = 12;

  public regAllocor(irFunc irfunc) {
    this.irfunc = irfunc;
    // irfunc.ids 存了所有局部变量，irfunc.remainAllocas存了所有复合alloca

    for (var re : availRegs)
      emptyRegs.add(re);
  }

  public void allocReg() {
    // 首先为 alloca 的值分配足够的栈空间
    irfunc.remainAllocas.forEach((alc, ins) -> {
      asmId head = new asmId(alc, -1);
      head.size = (ins.allocaQuantity.constValue + 4) * 4;
      stackVars.add(head);
    });

    // 线性扫描这里开始

    for (var arg : irfunc.args) {
      if (arg.argIndex < 8) {
        if (arg.actLeft != 0) {
          var re = emptyRegs.poll();
          idMap.put(arg, new asmId(arg, re));
          emptyRegs.add(re);
        } else {
          idMap.put(arg, new asmId(arg, getEmptyReg()));
          active.add(arg);
        }
        //寄存器参数刚好把s0~s7用去
      } else {
        //栈参数，与caller同步
        var asmid = new asmId(arg, -1);
        idMap.put(arg, asmid);
        spilledArgs.add(arg.argIndex - 8, asmid);
      }
    }

    for (var id : irfunc.ids)
      if (id.argIndex == -1 && !irfunc.remainAllocas.containsKey(id))
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
    Iterator<irId> iterator = active.iterator();
    while (iterator.hasNext()) {
      var jd = iterator.next();
      if (jd.actRight >= id.actLeft)
        return;
      iterator.remove();
      var expired = idMap.get(jd);
      emptyRegs.addFirst(expired.reg);
    }
  }

  void spillAt(irId id) {
    var spill = active.last();
    if (spill.actRight > id.actRight) {
      // 此时 id 还没有自己的asmId
      var asmspill = idMap.get(spill);
      var asmid = new asmId(id, asmspill.reg);
      idMap.put(id, asmid);

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
    SaveRegsUsed.add(re);
    return re;
  }

  RegName[] availRegs = {RegName.s0, RegName.s1, RegName.s2,  RegName.s3,
                         RegName.s4, RegName.s5, RegName.s6,  RegName.s7,
                         RegName.s8, RegName.s9, RegName.s10, RegName.s11};
}

class CompLeft implements Comparator<irId> {
  @Override
  public int compare(irId o1, irId o2) {
    int first = Integer.compare(o1.actLeft, o2.actLeft);
    if (first != 0)
      return first;
    else if (o1.equals(o2))
      return 0;
    else
      return o1.toString().compareTo(o2.toString());
  }
}

class CompRight implements Comparator<irId> {
  @Override
  public int compare(irId o1, irId o2) {
    int first = Integer.compare(o1.actRight, o2.actRight);
    if (first != 0)
      return first;
    else if (o1.equals(o2))
      return 0;
    else
      return o1.toString().compareTo(o2.toString());
  }
}
