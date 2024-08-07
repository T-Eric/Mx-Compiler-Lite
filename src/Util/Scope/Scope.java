package Util.Scope;

import Util.Error.semanticError;
import Util.Type;
import Util.position;
import java.util.HashMap;

public class Scope {
  private HashMap<String, Type> variables=new HashMap<>();// only variables
  private Scope parentScope=null;
  public globalScope mainScope=null;// a way to reach directly to the root

  public Scope(Scope parentScope) {
    this.parentScope = parentScope;
  }

  public Scope parentScope() { return parentScope; }

  public void defVar(String name, Type t, position pos) {
    if (variables.containsKey(name))
      throw new semanticError("Has already defined such variable [" + name+"]!", pos);
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

  // get the type of one variable (or maybe funcs and structs)
  public Type getType(String name, boolean lookupon) {
    if (variables.containsKey(name))
      return variables.get(name);
    else if (parentScope != null && lookupon)
      return parentScope.getType(name, true);
    else
      return null;
  }
}
