package utility.scope;

import java.util.HashMap;

import utility.Type;
import utility.position;
import utility.error.semanticError;

public class scope {
  private HashMap<String, Type> variables=new HashMap<>();// only variables
  private scope parentScope=null;
  public globalScope mainScope=null;// a way to reach directly to the root

  public scope(scope parentScope) {
    this.parentScope = parentScope;
  }

  public scope parentScope() { return parentScope; }

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
