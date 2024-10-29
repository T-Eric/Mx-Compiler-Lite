package backend;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import midend.llvm_ir.irassets.irId;
import midend.llvm_ir.irassets.statements.instructions.brIns;
import midend.llvm_ir.irassets.statements.instructions.retIns;
import midend.llvm_ir.irassets.statements.irBlock;
import midend.llvm_ir.irassets.statements.irFunc;
import midend.llvm_ir.irassets.statements.irIns;

public class asmPreOptimizer {
  public irFunc func = null;
  public HashSet<irId> ids = null;

  LinkedHashSet<irBlock> vis = new LinkedHashSet<>();
  LinkedList<irBlock> linearOrder = new LinkedList<>();

  public asmPreOptimizer(irFunc func) {
    this.func = func;
    this.ids = func.ids;
  }

  public void activeAnalysis() {
    mergeBlocks();
    func.retBlock = func.blocks.get(func.blocks.size() - 1);
    for (var blk : func.blocks)
      genBlockDefUse(blk);
    genBlockActiveVars();
    for (var blk : func.blocks)
      genInsActiveVars(blk);
    genBlockLinearOrd(func.blocks.get(0));
    genInsLinearOrd();
    genIdActiveInterval();
    // 输出一下，看看实力
    // System.out.println(func.name);
    // for (var id : ids)
    //   System.out.println(id + ": " + id.actLeft + " " + id.actRight);
    // System.out.println("------\n");
  }

  //#region active

  // merger
  void mergeBlocks() {
    boolean changed = false;
    do {
      changed = false;
      for (int i = func.blocks.size() - 1; i >= 0; --i) {
        var cur = func.blocks.get(i);
        if (cur.preBlocks.size() != 1)
          continue;
        var pre = cur.preBlocks.get(0);
        if (pre.sucBlocks.size() != 1)
          continue;
        // merge
        changed = true;
        for (var ins : cur.instructions)
          pre.instructions.add(ins);
        pre.terminal = cur.terminal;
        pre.sucBlocks.remove(cur);
        pre.sucBlocks.addAll(cur.sucBlocks);
        for (var suc : cur.sucBlocks)
          if (!pre.sucBlocks.contains(suc))
            pre.sucBlocks.add(suc);

        func.blocks.remove(cur);
        break;
      }
    } while (changed);
  }

  // active analysis and regAlloc

  void genBlockDefUse(irBlock block) {
    HashSet<irId> use = block.useIds, def = block.defIds;
    for (var inst : block.instructions) {
      var us = new HashSet<>(inst.useValue());
      var df = inst.defValue();
      us.removeAll(def);
      use.addAll(us);
      def.addAll(df);
    }
    var us = new HashSet<>(block.terminal.useValue());
    var df = block.terminal.defValue();
    us.removeAll(def);
    use.addAll(us);
    def.addAll(df);
  }

  void genBlockActiveVars() {
    // start from retBlock, whose outActiveIds is empty
    Queue<irBlock> Q = new LinkedList<>();
    Q.add(func.retBlock);
    func.retBlock.outActiveIds = new HashSet<>();
    while (!Q.isEmpty()) {
      var curBlock = Q.poll();
      calcInActiveIds(curBlock.outActiveIds, curBlock.useIds, curBlock.defIds,
                      curBlock.inActiveIds);

      for (var pre : curBlock.preBlocks) {
        HashSet<irId> result = new HashSet<>();
        for (var suc : pre.sucBlocks)
          result.addAll(suc.inActiveIds);
        if (!result.equals(pre.outActiveIds)) {
          pre.outActiveIds = result;
          Q.add(pre);
        }
      }
    }
    for (var block : func.blocks)
      if (block.outActiveIds == null)
        block.outActiveIds = new HashSet<>();
  }

  void genInsActiveVars(irBlock block) {
    block.terminal.outActiveIds.addAll(block.outActiveIds);
    calcInActiveIds(block.terminal.outActiveIds, block.terminal.useValue(),
                    block.terminal.defValue(), block.terminal.inActiveIds);
    var sucIn = block.terminal.inActiveIds;
    for (int i = block.instructions.size() - 1; i >= 0; i--) {
      var ins = block.instructions.get(i);
      ins.outActiveIds.addAll(sucIn); // should be empty currently
      calcInActiveIds(ins.outActiveIds, ins.useValue(), ins.defValue(),
                      ins.inActiveIds);
      sucIn = ins.inActiveIds;
    }
    assert block.inActiveIds.equals(
        block.instructions.isEmpty() ? block.terminal.inActiveIds
                                     : block.instructions.get(0).inActiveIds);
  }

  void genBlockLinearOrd(irBlock block) {
    if (vis.contains(block))
      return;
    vis.add(block);

    for (int i = block.sucBlocks.size() - 1; i >= 0; --i)
      genBlockLinearOrd(block.sucBlocks.get(i));
    linearOrder.addFirst(block);
  }

  void genInsLinearOrd() {
    int od = -1;
    for (var block : linearOrder) {
      for (var ins : block.instructions)
        ins.linearOrd = ++od;
      block.terminal.linearOrd = ++od;
    }
  }

  void genIdActiveInterval() {
    var iter = linearOrder.descendingIterator();

    while (iter.hasNext()) {
      var block = iter.next();

      int startOrd = block.instructions.isEmpty()
                         ? block.terminal.linearOrd
                         : block.instructions.getFirst().linearOrd,
          endOrd = block.terminal.linearOrd;
      for (var outid : block.outActiveIds)
        if (outid.isLocal()) {
          ids.add(outid);
          outid.actLeft = Math.min(outid.actLeft, startOrd);
          outid.actRight = Math.max(outid.actRight, endOrd);
        }

      var finalUse = block.terminal.type == irIns.InsType.Br
                         ? ((brIns)block.terminal).cond
                         : ((retIns)block.terminal).retValue;
      if (finalUse != null) {
        if (finalUse.isLocal()) {
          ids.add(finalUse);
          finalUse.actLeft = Math.min(finalUse.actLeft, startOrd);
          finalUse.actRight = Math.max(finalUse.actRight, endOrd);
        }
      }
      // 其实显然是重合了，因为block的outid就是terminal的outid

      var it = block.instructions.descendingIterator();

      while (it.hasNext()) {
        var ins = it.next();

        for (var def : ins.defValue())
          if (def.isLocal()) {
            ids.add(def);
            if (def.actLeft == Integer.MAX_VALUE)
              def.actLeft = def.actRight = ins.linearOrd;
            else
              def.actLeft = Math.max(def.actLeft, ins.linearOrd);
            // 这里做了妥协，没有use的def应当被优化掉的
            // 但是暂时没有精力改变源代码了
          }

        for (var use : ins.useValue())
          if (use.isLocal()) {
            ids.add(use);
            use.actRight = Math.max(use.actRight, ins.linearOrd);
            use.actLeft = Math.min(use.actLeft, startOrd);
          }
      }
    }

    // 加入了所有用过的id，但取出参数单独讨论
    // 他们在函数体中没有第一个def，虽然能保证活跃区间正确，但是不方便处理
    // 他们使用预先分配好的寄存器或直接用栈空间（因为是别人传入的）
    // 在参数不超过5个时，这样是很优的操作了
    int argIndex = -1;
    for (var arg : func.args)
      arg.argIndex = ++argIndex;
  }

  //#endregion

  //#region act tools

  void calcInActiveIds(HashSet<irId> out, HashSet<irId> use, HashSet<irId> def,
                       HashSet<irId> in) {
    HashSet<irId> minus = new HashSet<irId>(out);
    minus.removeAll(def);
    minus.addAll(use);
    in.clear();
    in.addAll(minus);
  }

  //#endregion
}
