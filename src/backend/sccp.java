package backend;

import java.util.HashMap;
import java.util.HashSet;
import midend.llvm_ir.irassets.irId;
import midend.llvm_ir.irassets.irId.IdType;
import midend.llvm_ir.irassets.statements.instructions.binaryIns;
import midend.llvm_ir.irassets.statements.instructions.brIns;
import midend.llvm_ir.irassets.statements.instructions.icmpIns;
import midend.llvm_ir.irassets.statements.instructions.phiIns;
import midend.llvm_ir.irassets.statements.irBlock;
import midend.llvm_ir.irassets.statements.irFunc;
import midend.llvm_ir.irassets.statements.irIns;
import midend.llvm_ir.irassets.statements.irIns.InsType;

public class sccp {
  public irFunc func;
  HashSet<irIns> worklist = new HashSet<>();
  HashMap<irId, HashSet<irIns>> useMap = new HashMap<>();
  HashMap<irIns, irBlock> atBlocks = new HashMap<>();

  public sccp(irFunc func) { this.func = func; }

  public void conspro() {
    for (var block : func.blocks) {
      block.instructions.add(block.terminal);
      for (var phi : block.phis.values()) {
        useMap.put(phi.result, new HashSet<>());
        atBlocks.put(phi, block);
      }
      for (var ins : block.instructions) {
        if (ins.shouldRemove)
          continue;
        atBlocks.put(ins, block);
        for (var def : ins.defValue())
          useMap.put(def, new HashSet<>());
      }
    }
    for (var block : func.blocks)
      getUse(block);

    for (var block : func.blocks) {
      worklist.addAll(block.phis.values());
      worklist.addAll(block.instructions);
      block.instructions.removeLast();
    }

    while (!worklist.isEmpty()) {
      var ins = worklist.iterator().next();
      worklist.remove(ins);
      if (ins.shouldRemove)
        continue;
      if (getConst(ins) != null) {
        // 假如可以，将变量的use替换成常数
        var value = getConst(ins);
        ins.shouldRemove = true;
        for (var def : ins.defValue()) {
          for (var use : useMap.get(def)) {
            if (use.type == InsType.Phi) {
              var phi = (phiIns)use;
              for (var entry : phi.args.entrySet()) {
                if (entry.getValue().asEqual(def))
                  phi.args.put(entry.getKey(), value);
              }
              worklist.add(phi);
            } else {
              use.rewrite(def, value);
              if (value.isLocal()) {
                // TODO 会不会有可能是GLOBAL？
                // 有时会因为phi变为固定变量，导致getConst的值不一定是个Constant
                useMap.get(value).add(use);
              }
              worklist.add(use);
            }
          }
        }
      } else if (ins.type == InsType.Br) {
        var br = (brIns)ins;
        if (br.cond != null && br.cond.type == IdType.Constant) {
          // 路径可折叠
          var now = atBlocks.get(br);
          var go = br.cond.constValue == 1 ? br.trueLabel.labelBlock
                                           : br.falseLabel.labelBlock;
          var other = br.cond.constValue == 1 ? br.falseLabel.labelBlock
                                              : br.trueLabel.labelBlock;
          now.terminal = new brIns();
          ((brIns)now.terminal).trueLabel = go.label;
          ins.shouldRemove = true;
          now.sucBlocks.remove(other);
          changePhi(now, other);
          if (other.preBlocks.size() == 0)
            eraseBlock(other);
        }
      }
    }
    for (var block : func.blocks)
      block.instructions.removeIf(ins -> ins.shouldRemove);
  }

  void getUse(irBlock block) {
    for (var phi : block.phis.values())
      for (var use : phi.useValue())
        if (useMap.containsKey(use))
          useMap.get(use).add(phi);

    for (var ins : block.instructions) {
      if (ins.shouldRemove)
        continue;
      for (var use : ins.useValue())
        if (useMap.containsKey(use))
          useMap.get(use).add(ins);
    }
  }

  void changePhi(irBlock now, irBlock other) {
    other.preBlocks.remove(now);
    for (var phi : other.phis.values())
      phi.args.remove(now.label);
  }

  irId getConst(irIns ins) {
    // 只对可能会产生i32结果的Ins做法
    if (ins.type == InsType.Binary) {
      var bi = (binaryIns)ins;
      if (bi.lhs.type == IdType.Constant && bi.rhs.type == IdType.Constant) {
        return bi.calc();
      }
    } else if (ins.type == InsType.Icmp) {
      var ic = (icmpIns)ins;
      if (ic.lhs.type == IdType.Constant && ic.rhs.type == IdType.Constant) {
        return ic.calc();
      }
    } else if (ins.type == InsType.Phi) {
      var phi = (phiIns)ins;
      if (phi.args.size() == 1) {
        return phi.args.values().iterator().next();
      } else {
        var first = phi.args.values().iterator().next();
        if (first.type == IdType.Constant) {
          for (var c : phi.args.values())
            if (!c.asEqual(first))
              return null;
          return first;
        } else
          return null;
      }
    }
    return null;
  }

  void eraseBlock(irBlock other) {
    func.blocks.remove(other);
    other.instructions.add(other.terminal);
    for (var phi : other.phis.values())
      for (var use : phi.useValue())
        if (useMap.containsKey(use))
          useMap.get(use).remove(phi);
    for (var ins : other.instructions) {
      ins.shouldRemove = true;
      for (var use : ins.useValue()) {
        if (useMap.containsKey(use))
          useMap.get(use).remove(ins);
      }
    }
    other.instructions.removeLast();
    for (var suc : other.sucBlocks) {
      changePhi(other, suc);
      if (suc.preBlocks.size() == 0)
        eraseBlock(suc);
    }
  }
}
