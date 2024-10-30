package backend;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import midend.llvm_ir.irassets.irId;
import midend.llvm_ir.irassets.irId.IdType;
import midend.llvm_ir.irassets.statements.instructions.allocaIns;
import midend.llvm_ir.irassets.statements.instructions.brIns;
import midend.llvm_ir.irassets.statements.instructions.loadIns;
import midend.llvm_ir.irassets.statements.instructions.moveIns;
import midend.llvm_ir.irassets.statements.instructions.phiIns;
import midend.llvm_ir.irassets.statements.instructions.storeIns;
import midend.llvm_ir.irassets.statements.irBlock;
import midend.llvm_ir.irassets.statements.irFunc;
import midend.llvm_ir.irassets.statements.irIns;

public class irOptimizer {
  public irFunc func;
  HashMap<irId, irId> curId = new HashMap<>();
  HashMap<irId, irId> lastDef = new HashMap<>();
  HashSet<irId> allocas = new HashSet<>();

  public irOptimizer(irFunc func) { this.func = func; }

  //#region interfaces

  public void ssa() {
    cleanDeadBlocks();
    calcPreSuc();
    genDom();
    genAntiDom();
    genPhi();
  }

  //#region SSA

  void cleanDeadBlocks() {
    // 死块空块消除
    while (true) {
      irId emptyLabel = null, gotoLabel = null;
      int emptyPos = -1;
      for (var b : func.blocks)
        if (b.instructions.isEmpty() && b.terminal.type == irIns.InsType.Br &&
            ((brIns)b.terminal).falseLabel == null) {
          emptyLabel = b.label;
          gotoLabel = ((brIns)b.terminal).trueLabel;
          break;
        }
      if (emptyLabel == null)
        break;
      for (int i = 0; i < func.blocks.size(); ++i) {
        var b = func.blocks.get(i);
        if (b == emptyLabel.labelBlock) {
          emptyPos = i;
          continue;
        }
        if (b.terminal.type == irIns.InsType.Br) {
          brIns ins = (brIns)b.terminal;
          if (ins.trueLabel == emptyLabel)
            ins.trueLabel = gotoLabel;
          if (ins.falseLabel == emptyLabel)
            ins.falseLabel = gotoLabel;
        }
      }
      func.blocks.remove(emptyPos);
    }
  }

  void calcPreSuc() {
    for (var b : func.blocks) {
      if (b.terminal.type == irIns.InsType.Br) {
        brIns ter = (brIns)b.terminal;
        ter.trueLabel.labelBlock.preBlocks.add(b);
        b.sucBlocks.add(ter.trueLabel.labelBlock);
        if (ter.falseLabel != null) {
          ter.falseLabel.labelBlock.preBlocks.add(b);
          b.sucBlocks.add(ter.falseLabel.labelBlock);
        }
      }
    }
  }

  void mergeSinglePreBlocks() {
    // 单边合并
    while (true) {
      boolean merged = false;
      for (var block : func.blocks) {
        if (block.preBlocks.size() == 1) {
          var pre = block.preBlocks.get(0);
          if (pre.sucBlocks.size() == 1) {
            assert pre.sucBlocks.get(0).equals(block);
            // merge
          }
        }
      }
      if (!merged)
        break;
    }
  }

  void genDom() {
    for (var bk : func.blocks)
      for (var b : func.blocks)
        bk.dom.add(b);

    // generate dom
    Queue<irBlock> Q = new LinkedList<>();
    while (true) {
      boolean changed = false;
      Q.add(func.blocks.get(0));
      HashSet<irId> visitedLabel = new HashSet<>();

      while (!Q.isEmpty()) {
        var cur = Q.poll();
        visitedLabel.add(cur.label);

        HashSet<irBlock> cap = new HashSet<>();
        if (cur.preBlocks.isEmpty()) {
          cap.add(cur);
        } else {
          cap.addAll(cur.preBlocks.get(0).dom);
          for (var pre : cur.preBlocks)
            cap.retainAll(pre.dom);
          cap.add(cur);
        }
        if (!cap.equals(cur.dom)) {
          cur.dom.clear();
          cur.dom.addAll(cap);
          changed = true;
        }

        for (var nxt : cur.sucBlocks)
          if (!visitedLabel.contains(nxt.label))
            Q.add(nxt);
      }
      if (!changed)
        break;
    }

    // pick idom from dom
    for (var blk : func.blocks) {
      int sz = blk.dom.size();
      for (var dm : blk.dom) {
        if (dm.dom.size() == sz - 1) {
          blk.idom = dm;
          break;
        }
      }
      if (blk.idom != null)
        blk.idom.idomOf.add(blk);
    }

    // add domFronts
    for (var blk : func.blocks) {
      HashSet<irBlock> ans = new HashSet<>();
      for (var pre : blk.preBlocks) {
        HashSet<irBlock> lhs = new HashSet<>(pre.dom);
        HashSet<irBlock> rhs = new HashSet<>(blk.dom);
        rhs.remove(blk);
        lhs.removeAll(rhs);
        ans.addAll(lhs);
      }
      for (var dff : ans)
        dff.domFronts.add(blk);
    }
  }

  void genAntiDom() {
    for (var bk : func.blocks)
      for (var b : func.blocks)
        bk.antidom.add(b);

    // generate dom
    Queue<irBlock> Q = new LinkedList<>();
    while (true) {
      boolean changed = false;
      Q.add(func.blocks.get(func.blocks.size() - 1));
      HashSet<irId> visitedLabel = new HashSet<>();

      while (!Q.isEmpty()) {
        var cur = Q.poll();
        visitedLabel.add(cur.label);

        HashSet<irBlock> cap = new HashSet<>();
        if (cur.sucBlocks.isEmpty()) {
          cap.add(cur);
        } else {
          cap.addAll(cur.sucBlocks.get(0).antidom);
          for (var suc : cur.sucBlocks)
            cap.retainAll(suc.antidom);
          cap.add(cur);
        }
        if (!cap.equals(cur.antidom)) {
          cur.antidom.clear();
          cur.antidom.addAll(cap);
          changed = true;
        }

        for (var nxt : cur.preBlocks)
          if (!visitedLabel.contains(nxt.label))
            Q.add(nxt);
      }
      if (!changed)
        break;
    }

    // pick idom from dom
    for (var blk : func.blocks) {
      int sz = blk.antidom.size();
      for (var dm : blk.antidom) {
        if (dm.antidom.size() == sz - 1) {
          blk.antiidom = dm;
          break;
        }
      }
      if (blk.antiidom != null)
        blk.antiidom.antiidomOf.add(blk);
    }

    // add domFronts
    for (var blk : func.blocks) {
      HashSet<irBlock> ans = new HashSet<>();
      for (var suc : blk.sucBlocks) {
        HashSet<irBlock> lhs = new HashSet<>(suc.antidom);
        HashSet<irBlock> rhs = new HashSet<>(blk.antidom);
        rhs.remove(blk);
        lhs.removeAll(rhs);
        ans.addAll(lhs);
      }
      for (var dff : ans)
        dff.antidomFronts.add(blk);
    }

    for (var block : func.blocks)
      for (var af : block.antidomFronts) {
        af.cdgSucs.add(block);
        block.cdgPres.add(af);
      }
  }

  public void genPhi() {
    // collect all allocas

    for (var blk : func.blocks)
      for (var ins : blk.instructions)
        if (ins.type == irIns.InsType.Alloca) {
          if (((allocaIns)ins).allocaQuantity == null)
            allocas.add(ins.result);
          else
            func.remainAllocas.put(ins.result, (allocaIns)ins);
        }
    for (var alc : allocas)
      --alc.valueType.dimension;
    // 从指针变为更低一级

    for (var blk : func.blocks)
      for (var ins : blk.instructions)
        if (ins.type == irIns.InsType.Store) {
          // 对这个变量的这个def，先找到其所有的df块
          var def = ((storeIns)ins).storeAddr;
          if (allocas.contains(def))
            for (var df : blk.domFronts)
              insertEmptyPhi(df, def);
        }

    // rename and value the phis
    rename(func.blocks.get(0));
  }

  public void phiEliminate() {
    // eliminate phis
    for (var block : func.blocks) {
      for (var phi : block.phis.values()) {
        if (uselessPhi(phi)) {
          // 直接替换为mv语句
          block.instructions.addFirst(
              new moveIns(phi.result, phi.args.values().iterator().next()));
          continue;
        }
        // 否则，在前一个块中插入tmp赋值，在这个块头部插入tmp到phi的赋值
        var tmp =
            phi.result
                .copyTemp(); // 大抵是不会重复的，假如重复了就用当前块的label去copy就行了
        for (var pre : phi.args.keySet())
          pre.labelBlock.instructions.add(new moveIns(tmp, phi.args.get(pre)));
        block.instructions.addFirst(new moveIns(phi.result, tmp));
      }
      block.phis.clear();
    }
  }

  //#endregion

  //#region SSA tools

  // 这个块是某个df，先插入phi，再递归地为一个store
  // def插入phi指令，直到无法再继续
  // 如果已经被插入phi说明后面的工作都做完了，直接返回
  void insertEmptyPhi(irBlock block, irId id) {
    // 如果已经有预留，不必再进行下去
    if (block.phis.containsKey(id))
      return;
    var ins = new phiIns(block);
    ins.result = id.copyPhi();

    for (var pre : block.preBlocks)
      ins.args.put(pre.label, ins.result.valueType.dimension > 0
                                  ? new irId(IdType.Null)
                                  : new irId(ins.result.valueType, 0));

    block.phis.put(id, ins);
    for (var df : block.domFronts)
      insertEmptyPhi(df, id);
  }

  void rename(irBlock block) {
    // 留存副本
    HashMap<irId, irId> curCopy = new HashMap<>();
    HashMap<irId, irId> LastDefCopy = new HashMap<>();
    for (var id : curId.keySet())
      curCopy.put(id, curId.get(id));
    for (var id : lastDef.keySet())
      LastDefCopy.put(id, lastDef.get(id));

    block.phis.forEach((id, ins) -> lastDef.put(id, ins.result));

    for (var phi : block.phis.values())
      for (var id : curId.keySet())
        phi.rewrite(id, curId.get(id));

    for (var ins : block.instructions) {
      for (var id : curId.keySet()) {
        assert curId.containsKey(id);
        ins.rewrite(id, curId.get(id));
      }

      if (ins.type == irIns.InsType.Store) {
        storeIns sto = (storeIns)ins;
        if (allocas.contains(sto.storeAddr)) {
          lastDef.put(sto.storeAddr, sto.storeValue);
          sto.shouldRemove = true;
        }
      } else if (ins.type == irIns.InsType.Load) {
        loadIns loa = (loadIns)ins;
        if (allocas.contains(loa.loadAddr)) {
          curId.put(loa.result, lastDef.get(loa.loadAddr));
          loa.shouldRemove = true;
        }
      } else if (ins.type == irIns.InsType.Alloca) {
        allocaIns alc = (allocaIns)ins;
        if (allocas.contains(alc.result))
          alc.shouldRemove = true; // 直接remove
      }
    }
    for (var id : curId.keySet())
      block.terminal.rewrite(id, curId.get(id));

    for (var b : block.sucBlocks) {
      for (var id : b.phis.keySet()) {
        var phi = b.phis.get(id);
        if (lastDef.get(id) != null && allocas.contains(id))
          phi.args.put(block.label, lastDef.get(id));
      }
    }
    for (var b : block.idomOf)
      rename(b);
    curId = curCopy;
    lastDef = LastDefCopy;

    LinkedList<irIns> newInstructions = new LinkedList<>();
    for (var ins : block.instructions)
      if (!ins.shouldRemove)
        newInstructions.add(ins);
    block.instructions = newInstructions;
  }

  boolean uselessPhi(phiIns phi) {
    if (phi.args.size() == 1)
      return true;
    else {
      irId one = null;
      for (var id : phi.args.values()) {
        if (one == null)
          one = id;
        else if (!id.asEqual(one))
          return false;
      }
    }
    return true;
  }

  //#endregion
}
