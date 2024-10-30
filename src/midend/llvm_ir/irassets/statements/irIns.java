package midend.llvm_ir.irassets.statements;

import java.util.HashSet;
import midend.llvm_ir.irassets.irId;
import midend.llvm_ir.irassets.irStatement;

public abstract class irIns extends irStatement {
  public InsType type;
  public irId result = null; // the result in the grammar

  // optimize
  public HashSet<irId> inActiveIds = new HashSet<>();
  public HashSet<irId> outActiveIds = new HashSet<>();
  protected HashSet<irId> useIds = null;
  protected HashSet<irId> defIds = null;
  protected HashSet<irId> useVars = null;

  public abstract HashSet<irId> useValue();
  public abstract HashSet<irId> defValue();
  public abstract HashSet<irId> useAny();
  public abstract void rewrite(irId origin, irId copy);
  public boolean shouldRemove = false;

  public int linearOrd = -1; // for linear scan

  public enum InsType {
    Declare,
    GlobalVar,
    Alloca,
    Load,
    Store,
    Move,
    Br,
    Call,
    Ret,
    Getelementptr,
    Icmp,
    Phi,
    Binary,
    Select,
    Bitcast
  }
}
