package backend;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import midend.llvm_ir.irassets.irId;
import midend.llvm_ir.irassets.statements.instructions.brIns;
import midend.llvm_ir.irassets.statements.instructions.phiIns;
import midend.llvm_ir.irassets.statements.irBlock;
import midend.llvm_ir.irassets.statements.irFunc;
import midend.llvm_ir.irassets.statements.irIns;
import midend.llvm_ir.irassets.statements.irIns.InsType;

public class adcer {
  public irFunc func = null;
  HashSet<irIns> live = new HashSet<>();
  HashSet<irBlock> liveBlock = new HashSet<>();
  HashSet<irIns> worklist = new HashSet<>();
  HashMap<irId, irIns> defMap = new HashMap<>();
  HashMap<irIns, irBlock> atBlocks = new HashMap<>();

  public adcer(irFunc func) { this.func = func; }

  public void adce() {
    for (var block : func.blocks) {
      for (var phi : block.phis.values()) {
        defMap.put(phi.result, phi);
        atBlocks.put(phi, block);
      }
      assert block.terminal != null;
      block.instructions.add(block.terminal);
      for (var ins : block.instructions) {
        for (var def : ins.defValue())
          defMap.put(def, ins);
        atBlocks.put(ins, block);
        if (ins.type == InsType.Store || ins.type == InsType.Call ||
            ins.type == InsType.Ret)
          worklist.add(ins);
      }
      block.instructions.removeLast();
    }

    while (!worklist.isEmpty()) {
      var ins = worklist.iterator().next();
      worklist.remove(ins);
      live.add(ins);
      liveBlock.add(atBlocks.get(ins));

      if (ins.type == InsType.Phi) {
        phiIns phi = (phiIns)ins;
        for (var lb : phi.args.keySet()) {
          var b = lb.labelBlock;
          if (!live.contains(b.terminal)) {
            worklist.add(b.terminal);
            liveBlock.add(b);
          }
        }
      }

      for (var cpre : atBlocks.get(ins).cdgPres)
        if (!live.contains(cpre.terminal))
          worklist.add(cpre.terminal);

      // jump到活块也是有副作用的，即自己的前驱中直接指向自己的
      for (var pre : atBlocks.get(ins).preBlocks) {
        var ter = (brIns)pre.terminal;
        if (ter.cond == null && !live.contains(ter))
          worklist.add(ter);
      }

      for (var use : ins.useValue()) {
        var defins = defMap.get(use);
        if (defins != null && !live.contains(defins))
          worklist.add(defins);
      }
    }

    for (var block : func.blocks) {
      HashMap<irId, phiIns> newPhis = new HashMap<>();
      LinkedList<irIns> newInstructions = new LinkedList<>();
      for (var phi : block.phis.values())
        if (live.contains(phi))
          newPhis.put(phi.result, phi);
      for (var ins : block.instructions)
        if (live.contains(ins))
          newInstructions.add(ins);
      block.phis = newPhis;
      block.instructions = newInstructions;
      if (!live.contains(block.terminal)) {
        brIns inactive = (brIns)block.terminal;

        var iter = block.antiidom;
        while (!liveBlock.contains(iter) && !iter.equals(iter.antiidom))
          iter = iter.antiidom;
        var ter = new brIns();
        ter.trueLabel = iter.label;
        block.terminal = ter;

        block.sucBlocks.clear();
        block.sucBlocks.add(iter);
        if (!iter.preBlocks.contains(block))
          iter.preBlocks.add(block);

        var tr = inactive.trueLabel.labelBlock;
        block.sucBlocks.remove(tr);
        tr.preBlocks.remove(block);

        if (iter.preBlocks.contains(tr))
          iter.preBlocks.remove(tr);

        if (inactive.falseLabel != null) {
          var fa = inactive.falseLabel.labelBlock;
          block.sucBlocks.remove(fa);
          fa.preBlocks.remove(block);
          if (iter.preBlocks.contains(fa))
            iter.preBlocks.remove(fa);
        }
      }
    }

    // 遍历，死掉的代码块（除0以外没有入口的）直接删除
    HashSet<irBlock> vis = new HashSet<>();
    LinkedList<irBlock> que = new LinkedList<>();
    que.add(func.blocks.get(0));
    while (!que.isEmpty()) {
      irBlock cur = que.poll();
      vis.add(cur);
      for (irBlock suc : cur.sucBlocks) {
        if (!vis.contains(suc))
          que.add(suc);
      }
    }
    var iterator = func.blocks.iterator();
    while (iterator.hasNext()) {
      var block = iterator.next();
      if (block.label.id != 0 && !vis.contains(block))
        iterator.remove();
    }
  }
}
