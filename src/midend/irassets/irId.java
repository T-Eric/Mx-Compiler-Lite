package midend.irassets;

public class irId {
  private static int lastId = 0;
  public IdType type = null;
  public irType valueType = null;
  public int id = -1;
  public String globalName = null;
  public int constValue = 0;

  public irId(IdType type) { this.type = type; }

  public irId(irType vtype){
    this.type=IdType.Local;
    this.valueType=vtype;
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

  public void setIndex() {
    if (id == -1)
      id = lastId++;
  }

  public int getLabel() {
    setIndex();
    return id;
  }

  public String toString() {
    switch (type) {
    case Global:
      return ("@" + globalName);
    case Constant:
      return Integer.toString(constValue);
    case Null:
      return "null";
    default:
      return "%" + id;
    }
  }

  public enum IdType { Global, Local, Constant, Label, Null }
}
