package utility.scope;

import ast.funcDefNode;
import ast.typeNode;
import java.util.ArrayList;
import utility.Type;
import utility.Type.ASTType;
import utility.error.semanticError;

public class funcScope extends scope {
  // construct from funcDefNode is fast
  // can contain same-name args, use inside def
  // use variables for inner-defined variables
  // but use arguments for argList
  public Type retType = null;
  public String name = null;
  // ! why not directly add in variables?
  public ArrayList<String> argNames = new ArrayList<>();
  public ArrayList<Type> argTypes = new ArrayList<>();
  // can't just use hashmap because we need an order

  public funcScope(scope parScope, globalScope mainScope, funcDefNode node) {
    super(parScope, mainScope);

    name = node.name;
    if (node.type == null) {
      // maybe constructor
      retType = new Type(ASTType.Null, false);
    } else
      retType = node.type.type;
    if (retType != null && !mainScope.containsType(retType))
      throw new semanticError("Undefined Identifier", node.pos);

    var argList = node.argList;
    if (argList != null) {
      for (int i = 0; i < argList.types.size(); i++) {
        typeNode tn = argList.types.get(i);
        Type t = tn.type;
        String id = argList.identifiers.get(i);
        if (!mainScope.containsType(retType))
          throw new semanticError("Undefined argument type: [" + t.toString() +
                                      "] in function [" + name + "]!",
                                  tn.pos);
        argTypes.add(t);
        if (variables.containsKey(id))
          throw new semanticError("Has defined such argument: [" + id +
                                      "] in function [" + name + "]!",
                                  tn.pos);
        argNames.add(id);
        variables.put(id, t);
      }
    }
  }
}
