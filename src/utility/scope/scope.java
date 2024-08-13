package utility.scope;

import java.util.HashMap;

import utility.Type;
import utility.position;
import utility.error.semanticError;

public class scope {
  public HashMap<String, Type> variables=new HashMap<>();// only variables
  public scope parentScope=null;
  public globalScope mainScope=null;

  public scope(scope parentScope,globalScope mainScope) {
    this.parentScope = parentScope;
    this.mainScope = mainScope;
  }

  public void defVar(String name, Type t, position pos) {
    if (variables.containsKey(name))
      throw new semanticError("Multiple Definitions", pos);
    t.isVariable=true;
    variables.put(name, t);
  }

  public boolean containsVar(String name, boolean lookupon) {
    if (variables.containsKey(name))
      return true;
    else if (parentScope != null && lookupon)
      return parentScope.containsVar(name, true);
    else
      return false;
  }

  // get the type of one variable (or maybe funcs' returnType and classType)
  // wantWhich的设计：对于当前scope同时有同名类和变量时
  // 注意到getType只会找变量，故先
  public Type getType(String name, boolean lookupon) {
    if (variables.containsKey(name))
      return variables.get(name);
    else if (parentScope != null && lookupon)
      return parentScope.getType(name, true);
    else
      return null;
  }
}
