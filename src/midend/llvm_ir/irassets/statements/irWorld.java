package midend.llvm_ir.irassets.statements;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import midend.llvm_ir.irassets.irStatement;

public class irWorld extends irStatement {
  public ArrayList<irIns> declares = new ArrayList<>();
  public ArrayList<irIns> defines = new ArrayList<>();
  public LinkedHashMap<String, irClass> classes = new LinkedHashMap<>();
  public LinkedHashMap<String, irFunc> functions = new LinkedHashMap<>();

  public LinkedHashMap<String, irFunc> optees = new LinkedHashMap<>();

  @Override
  public void genIndex() {
    classes.values().forEach(cl -> cl.genIndex());
    functions.values().forEach(fn -> fn.genIndex());
  }

  @Override
  public String toString() {
    StringBuilder ret = new StringBuilder();
    declares.forEach(dec -> ret.append(dec).append("\n"));
    ret.append("\n");
    defines.forEach(def -> ret.append(def).append("\n"));
    ret.append("\n");
    if (!classes.isEmpty()) {
      classes.values().forEach(cl -> ret.append(cl).append("\n"));
      ret.append("\n");
    }
    if (!functions.isEmpty()) {
      functions.values().forEach(fn -> ret.append(fn).append("\n"));
      ret.deleteCharAt(ret.length() - 1);
    }
    return ret.toString();
  }
}
