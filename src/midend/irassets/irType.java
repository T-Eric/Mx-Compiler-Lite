package midend.irassets;

import ast.typeNode;
import midend.irassets.statements.irClass;
import midend.irassets.statements.irWorld;
import utility.error.internalError;
import utility.position;

public class irType {
  public IRType type = null;
  public int dimension = 0;       // getelementptr
  public irClass theClass = null; // if it is class pointer
  public int arrayLength = -1;

  public irType(IRType type) { this.type = type; }

  public irType(irType other) {
    this.type = other.type;
    this.dimension = other.dimension;
    this.arrayLength = other.arrayLength;
    this.theClass = other.theClass;
  }

  public irType(IRType type, int arrayLength) {
    this.type = type;
    this.arrayLength = arrayLength;
    this.dimension = 0;
  }

  public irType(irClass theClass) {
    this.type = IRType.Class;
    this.theClass = theClass;
    this.dimension = 1;
  }

  public irType(typeNode astType, irWorld irt) {
    switch (astType.type.type) {
    case Bool:
      this.type = IRType.I1;
      break;
    case Int:
      this.type = IRType.I32;
      break;
    case String:
      this.type = IRType.I8;
      ++dimension;
      break;
    case Void:
      this.type = IRType.Void;
      break;
    case ClassName:
      this.type = IRType.Class;
      this.theClass = irt.classes.get("class." + astType.type.className);
      dimension++;
      break;
    default:
      throw new internalError("ASTType not defined", new position(0, 0));
    }
    dimension += astType.type.dimension;
  }

  public irType getPtr() {
    irType ret = new irType(this);
    ret.dimension++;
    return ret;
  }

  public irType getDeref() {
    irType ret = new irType(this);
    if (dimension == 0)
      throw new internalError("Trying to get deref of which dimension is 0",
                              new position(0, 0));
    ret.dimension--;
    return ret;
  }

  public int sizeof() {
    if (dimension != 0)
      return 4; // pointer size
    else if (type == IRType.Class) {
      // %cls=type{i8,i32,%other}=1+4+4=9;
      int ret = 0;
      for (var v : theClass.vars.values())
        ret += v.b.sizeof();
      return ret;
    } else {
      switch (type) {
      case I1:
        return 1;
      case I8:
        return 1;
      case I32:
        return 4;
      default:
        throw new internalError("Bad IRType", new position(0, 0));
      }
    }
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    switch (type) {
    case I1:
      sb.append("i1");
      break;
    case I8:
      sb.append("i8");
      break;
    case I32:
      sb.append("i32");
      break;
    case Void:
      sb.append("void");
      break;
    case Class:
      sb.append("%class." + theClass.name);
      break;
    }
    if (arrayLength == -1)
      sb.append("*".repeat(dimension));
    else
      return String.format("[%d x i8]", arrayLength) + "*".repeat(dimension);
    return sb.toString();
  }

  public String toZeroInitString() {
    if (dimension != 0)
      return "null"; // array
    switch (type) {
    case Class:
      return "zeroinitializer";
    case Void:
      throw new internalError("Can't cast Void to String!", new position(0, 0));
    default:
      return "0";
    }
  }

  public enum IRType { I1, I8, I32, Void, Class }
}
