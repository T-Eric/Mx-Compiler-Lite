package midend.llvm_ir.irassets.statements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import midend.llvm_ir.irassets.irId;
import midend.llvm_ir.irassets.irStatement;
import midend.llvm_ir.irassets.statements.instructions.phiIns;

public class irBlock extends irStatement {
  public static boolean isFirst = true;
  public irId label = null;
  public LinkedList<irIns> instructions = new LinkedList<>();
  public irIns terminal = null; // br and ret

  // optimize: CFG
  public ArrayList<irBlock> preBlocks = new ArrayList<>();
  public ArrayList<irBlock> sucBlocks = new ArrayList<>();

  // optimize: Active Analysis
  public HashSet<irId> inActiveIds = new HashSet<>();
  public HashSet<irId> outActiveIds = new HashSet<>();
  public HashSet<irId> useIds = new HashSet<>();
  public HashSet<irId> defIds = new HashSet<>();

  // optimize: SSA
  public HashSet<irBlock> dom = new HashSet<>(); // 支配集，支配该节点的集合
  public irBlock idom = null;
  public HashSet<irBlock> idomOf = new HashSet<>();
  public HashSet<irBlock> domFronts = new HashSet<>();
  public HashMap<irId, phiIns> phis = new HashMap<>();

  public irBlock() {
    label = new irId(irId.IdType.Label);
    label.labelBlock = this;
  }

  public static void setFirstBlock() { isFirst = true; }

  @Override
  public void genIndex() {
    label.setIndex();
    instructions.forEach(ins -> ins.genIndex());
  }

  @Override
  public String toString() {
    StringBuilder ret = new StringBuilder();
    int labelId = label.getLabel();
    if (!isFirst)
      ret.append(labelId).append(":\n");
    else
      isFirst = false;
    phis.forEach((id, ins) -> ret.append("  ").append(ins).append("\n"));
    instructions.forEach(
        ins -> ret.append(ins.linearOrd).append("  ").append(ins).append("\n"));
    ret.append(terminal.linearOrd).append("  ").append(terminal).append("\n");
    return ret.toString();
  }
}
