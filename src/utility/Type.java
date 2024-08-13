// for judging a variable's, a function's type
// and judge whether the types are the same
package utility;

public class Type {
  public boolean isVariable = false;
  public ASTType type = null;
  public String className = null; // if is a self-defined class
  public int dimension = 0;       // if is an array

  public Type(ASTType type, boolean isVariable) {
    this.type = type;
    this.isVariable = isVariable;
  }

  public Type(String className) {
    type = ASTType.ClassName;
    isVariable = true;
    this.className = className;
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

  public boolean equals_feat_null(Type otype) {
    if (this == otype)
      return true;
    if ((type == otype.type) && (dimension == otype.dimension)) {
      if (type == ASTType.ClassName)
        return className.equals(otype.className);
      else
        return true;
    }

    return (type == ASTType.Null && otype.type == ASTType.ClassName) ||
        (type == ASTType.ClassName && otype.type == ASTType.Null) ||
        (type == ASTType.Null && otype.dimension != 0 &&
         otype.dimension >= dimension) ||
        (dimension != 0 && otype.type == ASTType.Null &&
         dimension >= otype.dimension);
  }

  // only requires same type, no need for dimension
  public boolean sameWith(Type otype) {
    if (this == otype)
      return true;
    if (otype == null || getClass() != otype.getClass())
      return false;

    if (type == otype.type) {
      if (type == ASTType.ClassName)
        return className.equals(otype.className);
      else
        return true;
    }

    return false;
  }

  public String toString() {
    String dim = new String();
    for (int i = 0; i < dimension; ++i)
      dim += "[]";
    switch (type) {
    case Bool:
      return "bool" + dim;
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
      throw new IllegalStateException("unknown error in ASTType.toString()");
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
