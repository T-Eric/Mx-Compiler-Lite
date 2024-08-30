package midend.asm.asmassets.statements;

import java.util.LinkedHashMap;
import midend.asm.asmassets.asmClass;
import midend.asm.asmassets.asmId;
import midend.asm.asmassets.asmId.AsmType;
import midend.asm.asmassets.asmStatement;
import midend.llvm_ir.irassets.statements.irWorld;

public class asmWorld extends asmStatement {
  public irWorld ir = null;
  // global defines
  public asmBlock dataBlock = new asmBlock(null, false);
  public asmBlock rodataBlock = new asmBlock(null, false);
  // directives
  public asmBlock prefixBlock = new asmBlock(null, false);
  public LinkedHashMap<String, asmFunc> functions =
      new LinkedHashMap<>(); // .section text、、.globl <function_names>
  // class offsets
  public LinkedHashMap<String, asmClass> classes = new LinkedHashMap<>();

  public asmId label = new asmId(AsmType.Label, null);

  public asmWorld(irWorld world) {
    this.ir = world;
    dataBlock.label = new asmId(AsmType.Label, null);
    dataBlock.label.info = "\t.data";
    dataBlock.isSection = true;
    rodataBlock.label = new asmId(AsmType.Label, null);
    rodataBlock.label.info = "\t.rodata";
    rodataBlock.isSection = true;
    label.info = "\t.text\n";
  }

  @Override
  public String toString() {
    var ret = new StringBuilder();
    ret.append(label).append("\n");
    ret.append(prefixBlock);
    functions.values().forEach(fn -> ret.append(fn));
    ret.append(dataBlock);
    ret.append(rodataBlock);
    return ret.toString();
  }
}
