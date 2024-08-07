package Util.Scope;

import AST.classDefNode;
import AST.funcDefNode;
import Util.Error.semanticError;
import Util.Type;
import Util.position;
import java.util.HashMap;

public class classScope extends Scope {
  // why don't we construct through classDefNode?
  // do it!

  // private HashMap<String, Type> variables=new HashMap<>();// only member
  // variables
  private HashMap<String, funcScope> methods; // methods has scopes
  public String name = null;

  public classScope(Scope parScope, classDefNode node) {
    // parScope must be mainScope
    super(parScope);
    name = node.name;
    // define variables
    for (int i = 0;i<node.varDefs.size();++i){
      var vd=node.varDefs.get(i);
      Type t=vd.type.type;
      for(int j=0;j<vd.varTerms.size();++j){
        var vt=vd.varTerms.get(j);
        parScope.defVar(vt.name, t, vt.pos);
      }
    }
    //define methods
    for(int i=0;i<node.methodDefs.size();++i){
      var md=node.methodDefs.get(i);
      defMethod(md);
    }
  }

  public void defMethod(funcDefNode node) {
    if (methods.containsKey(node.name))
      throw new semanticError("Has already defined such method [" + node.name +
                                  "] in class [" + name + "]!",
                              node.pos);
    methods.put(node.name, new funcScope(this, node));
  }

  // methods cannot strate also
  public boolean containsMethod(String name) {
    return methods.containsKey(name);
  }

  // ! one problem here: if no such method, the pos is undefined
  public funcScope getMethodScope(String name, position pos) {
    if (methods.containsKey(name))
      return methods.get(name);
    else
      throw new semanticError(
          "Undefined method [" + name + "] in class [" + name + "]!", pos);
  }
}
