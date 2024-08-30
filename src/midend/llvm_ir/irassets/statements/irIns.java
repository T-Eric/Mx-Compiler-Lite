package midend.llvm_ir.irassets.statements;

import midend.llvm_ir.irassets.irId;
import midend.llvm_ir.irassets.irStatement;

public abstract class irIns extends irStatement{
  public InsType type;
  public irId result=null;// the result in the grammar
  public enum InsType{
    Declare,GlobalVar,Alloca,Load,Store,Br,Call,Ret,Getelementptr,Icmp,Phi,Binary,Select,Bitcast
  }
}
