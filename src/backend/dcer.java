package backend;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import midend.llvm_ir.irassets.irId;
import midend.llvm_ir.irassets.statements.instructions.phiIns;
import midend.llvm_ir.irassets.statements.irBlock;
import midend.llvm_ir.irassets.statements.irFunc;
import midend.llvm_ir.irassets.statements.irIns;
import midend.llvm_ir.irassets.statements.irIns.InsType;

public class dcer {
  public irFunc func = null;
  HashMap<irId, HashSet<irIns>> useMap = new HashMap<>();
  HashMap<irId, irIns> defMap = new HashMap<>();
  HashSet<irId> worklistSet = new HashSet<>();
  HashSet<irId> argSet = new HashSet<>();

  public dcer(irFunc func) { this.func = func; }

  public void dce() {
    argSet.addAll(func.args);

    for (var block : func.blocks) {
      block.instructions.add(block.terminal);
      for (var phi : block.phis.values()) {
        useMap.put(phi.result, new HashSet<>());
        defMap.put(phi.result, phi);
        worklistSet.add(phi.result);
        // backup
      }
      for (var ins : block.instructions) {
        if (ins.shouldRemove)
          continue;
        for (var def : ins.defValue()) {
          useMap.put(def, new HashSet<>());
          defMap.put(def, ins);
          worklistSet.add(def);
          // backup
        }
      }
    }

    for (var block : func.blocks) {
      getUse(block);
      block.instructions.removeLast();
    }

    while (!worklistSet.isEmpty()) {
      var id = worklistSet.iterator().next();
      worklistSet.remove(id);
      if (useMap.get(id).isEmpty()) {
        var ins = defMap.get(id);
        if (ins.type != InsType.Call) {
          ins.shouldRemove = true;
          for (var use : ins.useValue()) {
            useMap.get(use).remove(ins);
            worklistSet.add(use);
          }
        }
      }
    }

    for (var block : func.blocks) {
      Iterator<Map.Entry<irId, phiIns>> iterator =
          block.phis.entrySet().iterator();
      while (iterator.hasNext()) {
        var entry = iterator.next();
        if (entry.getValue().shouldRemove)
          iterator.remove();
      }
      block.instructions.removeIf(ins -> ins.shouldRemove);
    }
  }

  void getUse(irBlock block) {
    for (var phi : block.phis.values())
      for (var use : phi.useValue())
        if (useMap.containsKey(use)) {
          useMap.get(use).add(phi);
        }
    for (var ins : block.instructions) {
      if (ins.shouldRemove)
        continue;
      for (var use : ins.useValue())
        if (useMap.containsKey(use)) {
          useMap.get(use).add(ins);
        }
    }
  }
}
