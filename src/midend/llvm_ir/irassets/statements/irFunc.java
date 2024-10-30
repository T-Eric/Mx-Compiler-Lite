package midend.llvm_ir.irassets.statements;

import backend.irOptimizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import midend.llvm_ir.irassets.*;
import midend.llvm_ir.irassets.statements.instructions.allocaIns;

public class irFunc extends irStatement {
  public boolean builtIn = false;
  public irType retType = null;
  public String name = null;

  public irClass theClass = null;
  public ArrayList<irId> args = new ArrayList<>();

  public irId retValPtr = null;
  public irBlock retBlock = null;
  public ArrayList<irBlock> blocks = new ArrayList<>();

  // regAlloc
  public HashSet<irId> ids = new HashSet<>();
  public HashMap<irId, allocaIns> remainAllocas = new HashMap<>();
  public irOptimizer opter = null;

  @Override
  public void genIndex() {
    irId.clearIndexCounter();
    args.forEach(arg -> arg.setIndex());
    blocks.forEach(block -> block.genIndex());
  }

  @Override
  public String toString() {
    if (builtIn)
      return "";
    irId.clearIndexCounter();
    StringBuilder ret = new StringBuilder();
    ret.append("define ").append(retType).append(" @").append(name).append("(");
    if (!args.isEmpty()) {
      args.forEach(
          arg
          -> ret.append(arg.valueType).append(" ").append(arg).append(", "));
      ret.setLength(ret.length() - 2);
    }
    ret.append(") {\n");
    // retblock already in blocks
    irBlock.setFirstBlock();
    blocks.forEach(block -> ret.append(block).append("\n"));
    ret.deleteCharAt(ret.length() - 1);
    ret.append("}\n");
    return ret.toString();
  }
}
