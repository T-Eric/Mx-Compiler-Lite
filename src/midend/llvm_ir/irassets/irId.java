package midend.llvm_ir.irassets;

import midend.llvm_ir.irassets.irType.IRType;
import midend.llvm_ir.irassets.statements.irBlock;

public class irId {
  public static int lastId = 0;
  public IdType type = null;
  public irType valueType = null;
  public int id = -1;
  public int phiId = -1;  // SSA
  public int tempId = -1; // SSA
  public String globalName = null;
  public int constValue = 0;

  public int refTime = 0;
  public irBlock labelBlock = null;
  public irId origin = null; // SSA重命名需要
  public int actLeft = Integer.MAX_VALUE, actRight = 0;
  public int argIndex = -1;
  // 目前是不带hole的整块，所以可能有些非常大而空洞的东西占据了寄存器
  public String output = null;

  public irId(IdType type) { this.type = type; }

  public irId(irType vtype) {
    this.type = IdType.Local;
    this.valueType = vtype;
  }

  public irId(irType vtype, String globalName) {
    this.type = IdType.Global;
    this.valueType = vtype;
    this.globalName = globalName;
  }

  public irId(irType type, int value) {
    this.type = IdType.Constant;
    this.valueType = type;
    this.constValue = value;
  }

  public static void clearIndexCounter() { lastId = 0; }

  public irId copyPhi() {
    irId ret = new irId(this.valueType);
    ret.id = id;
    ret.phiId = ++phiId;
    ret.origin = this;
    return ret;
  }

  public irId copyTemp() {
    assert this.origin != null;
    irId ret = new irId(this.valueType);
    ret.id = id;
    ret.phiId = phiId;
    ret.tempId = ++this.origin.tempId;
    ret.origin = this.origin;
    return ret;
  }

  public boolean asEqual(irId other) {
    return this.equals(other) ||
        (other != null && this.type == IdType.Constant &&
         other.type == IdType.Constant &&
         this.constValue == other.constValue) ||
        (other != null && this.type == IdType.Null &&
         other.type == IdType.Null);
  }

  public boolean isLocal() {
    return this.type == IdType.Local && this.valueType.type != IRType.Void;
  }

  public boolean isLocalGlobal() {
    return isLocal() || this.type == IdType.Global;
  }

  public void setIndex() {
    if (id == -1)
      id = lastId++;
  }

  public int getLabel() {
    setIndex();
    return id;
  }

  public String toString() {
    if (type == IdType.Local)
      refTime++;
    if (output != null)
      return output;
    String ret = null;
    switch (type) {
    case Global:
      ret = "@" + globalName;
      break;
    case Constant:
      ret = Integer.toString(constValue);
      break;
    case Null:
      ret = "null";
      break;
    default:
      if (origin == null)
        ret = "%" + id;
      else if (tempId == -1)
        ret = "%" + id + ".phi." + phiId;
      else
        ret = "%" + id + ".tmp." + phiId + "." + tempId;
    }
    output = ret;
    return output;
  }

  public enum IdType { Global, Local, Constant, Label, Null }
}
