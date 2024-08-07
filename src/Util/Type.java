// for judging a variable's, a function's type
// and judge whether the types are the same
package Util;

import Util.Error.internalError;

public class Type {
  public boolean isVariable;
  public ASTType type = null;
  public String className = null; // if is a self-defined class
  public int dimension = 0;       // if is an array

  public Type(ASTType type_, boolean isVariable_) {
    type = type_;
    isVariable = isVariable_;
  }

  public Type(String className_) {
    type = ASTType.ClassName;
    isVariable = true;
    className = className_;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Type otype = (Type)o;

    if ((type == otype.type) && (dimension == otype.dimension)) {
      if (type == ASTType.ClassName)
        return className.equals(otype.className);
      else
        return true;
    }

    return false;
  }

  public String toString() {
    String dim=new String();
    for(int i=0;i<dimension;++i)dim+="[]";
    switch (type) {
    case Bool:
      return "bool"+dim;
    case Int:
      return "int" + dim;
    case String:
      return "string" + dim;
    case Void:
      return "void" + dim;
    case Null:
      return "null" + dim;
    case ClassName:
      return "class " + className + dim;
    default:
      throw new internalError("unknown error in ASTType.toString()", null);
    }
  }

  public enum ASTType {
    Bool,
    Int,
    String,
    Void,
    Null,
    ClassName,
  }
}
