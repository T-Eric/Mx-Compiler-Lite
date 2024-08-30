package midend.llvm_ir.irassets.statements;

import java.util.LinkedHashMap;
import midend.llvm_ir.irassets.irStatement;
import midend.llvm_ir.irassets.irType;
import org.antlr.v4.runtime.misc.Pair;

public class irClass extends irStatement {
  public boolean builtIn = false;
  public String name = null;
  public LinkedHashMap<String, Pair<Integer, irType>> vars =
      new LinkedHashMap<>();// the pair is label and type
  public irFunc constructor = null;
  public LinkedHashMap<String, irFunc> methods = new LinkedHashMap<>();

  @Override
  public void genIndex() {
    constructor.genIndex();
    methods.values().forEach(mt -> mt.genIndex());
  }

  @Override
  public String toString() {
    if (builtIn)
      return "";
    StringBuilder ret = new StringBuilder();
    StringBuilder varStr = new StringBuilder();
    vars.values().forEach(pair -> varStr.append(pair.b).append(", "));
    if (!vars.isEmpty())
      varStr.delete(varStr.length() - 2, varStr.length());
    ret.append(String.format("%s = type { %s }\n", new irType(this).getDeref(),
                             varStr));
    ret.append('\n').append(constructor);
    for (var mt : methods.values())
      ret.append('\n').append(mt);
    return ret.toString();
  }
}
