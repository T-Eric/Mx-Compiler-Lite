package utility.scope;

import utility.Type;
import utility.error.semanticError;

import java.util.HashMap;

import ast.funcDefNode;
import ast.typeNode;

public class funcScope extends scope {
  // construct from funcDefNode is fast
  // can contain same-name args, use inside def
  // use variables for inner-defined variables
  // but use arguments for argList
  private Type retType = null;
  public String name = null;
  // ! why not directly add in variables?
  private HashMap<String, Type> arguments = new HashMap<>();

  public funcScope(scope parScope, funcDefNode node) {
    super(parScope);

    name = node.name;
    retType = node.type.type;
    if (retType != null && !mainScope.containsType(retType))
      throw new semanticError("Undefined return type: [" + retType.toString() +
                                  "] in function [" + name + "]!",
                              node.pos);

    final var argList = node.argList;
    if (argList != null) {
      for (int i = 0; i < argList.types.size(); i++) {
        final typeNode tn = argList.types.get(i);
        Type t = tn.type;
        String id = argList.identifiers.get(i);
        if (!mainScope.containsType(retType))
          throw new semanticError("Undefined argument type: [" + t.toString() +
                                      "] in function [" + name + "]!",
                                  tn.pos);
        if (arguments.containsKey(id))
          throw new semanticError("Has defined such argument: [" + id +
                                      "] in function [" + name + "]!",
                                  tn.pos);
        arguments.put(id, t);
      }
    }
  }

  public boolean containsArg(String id) {
    // use before its defVar, vars cannot has same name with args
    return arguments.containsKey(id);
  }

  public Type getArgType(String id) { return arguments.get(id); }
}
