package utility.scope;

import ast.classDefNode;
import ast.funcDefNode;
import java.util.HashMap;
import utility.Type;
import utility.Type.ASTType;
import utility.error.semanticError;
import utility.position;

public class classScope extends scope {
  // why don't we construct through classDefNode?
  // do it!

  // private HashMap<String, Type> variables=new HashMap<>();// only member
  // variables
  private HashMap<String, funcScope> methods =
      new HashMap<>(); // methods has scopes
  public String name = null;

  public classScope(scope parScope, classDefNode node) {
    // parScope must be mainScope
    super(parScope, (globalScope)parScope);
    name = node.name;
    // define variables
    // ! variables can't forward collect, so we'll consider deleting these
    // ...nonono, member variables are available throughout the whole class, no
    // need to consider that
    for (int i = 0; i < node.varDefs.size(); ++i) {
      var vd = node.varDefs.get(i);
      Type t = vd.type.type;
      for (int j = 0; j < vd.varTerms.size(); ++j) {
        var vt = vd.varTerms.get(j);
        defVar(vt.name, t, vt.pos);
      }
    }
    // define methods
    for (int i = 0; i < node.methodDefs.size(); ++i) {
      var md = node.methodDefs.get(i);
      defMethod(md);
    }
  }

  public void defMethod(funcDefNode node) {
    if (methods.containsKey(node.name))
      throw new semanticError("Has already defined such method [" + node.name +
                                  "] in class [" + name + "]!",
                              node.pos);
    funcScope newMethod = new funcScope(this, mainScope, node);
    if (newMethod.retType.type == ASTType.Null) {
      // check if it is constructor
      if (!newMethod.name.equals(this.name))
        throw new semanticError("Constructor must have same name with class",
                                node.pos);
    }
    methods.put(node.name, newMethod);
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

  @Override
  public Type getType(String name, boolean lookupon) {
    // for method retTypes and member variables
    if (methods.containsKey(name))
      return new Type(methods.get(name).retType.type, false);
    return super.getType(name, lookupon);
  }
}
