package midend.llvm_ir.irassets.statements.instructions;

import java.util.ArrayList;
import midend.llvm_ir.irassets.irId;
import midend.llvm_ir.irassets.irType;
import midend.llvm_ir.irassets.statements.irIns;
import org.antlr.v4.runtime.misc.Pair;

public class phiIns extends irIns {
  public ArrayList<Pair<irId, irId>> args = new ArrayList<>();

  public phiIns(irType insType) {
    this.type = InsType.Phi;
    result = new irId(insType);
  }

  public phiIns(){
    this.type = InsType.Phi;
  }

  @Override
  public void genIndex() {
    result.setIndex();
  }

  @Override
  public String toString() {
    var insType = (result == null) ? null : result.valueType;
    var argStr = new StringBuilder();
    args.forEach(
        arg -> argStr.append(String.format("[ %s, %s ], ", arg.a, arg.b)));
    argStr.delete(argStr.length() - 2, argStr.length());
    return String.format("%s = phi %s %s", result, insType, argStr);
  }
}
