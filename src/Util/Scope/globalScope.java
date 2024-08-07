package Util.Scope;

import AST.classDefNode;
import AST.funcDefNode;
import Util.Error.semanticError;
import Util.Type;
import Util.position;
import java.util.HashMap;

public class globalScope extends Scope {
  // private HashMap<String, Type> variables=new HashMap<>();// only variables
  private HashMap<String, Type> types =
      new HashMap<>(); // store all occured types in the program, variables and
                       // classes
  private HashMap<String, classScope> classes = new HashMap<>();
  private HashMap<String, funcScope> functions = new HashMap<>();

  public globalScope(Scope parScope) {
    super(parScope);
    // parScope must be null
  }

  public void addType(String name, Type t, position pos) {
    if (types.containsKey(name))
      throw new semanticError("Has already defined such type: [" + name + "]!",
                              pos);
    types.put(name, t);
  }

  public boolean containsType(String name) { return types.containsKey(name); }

  public boolean containsType(Type t) { return types.containsValue(t); }

  public Type getTypeFromName(String name, position pos) {
    if (types.containsKey(name))
      return types.get(name);
    else
      throw new semanticError("Undefined type: [" + name + "]!", pos);
  }

  public void defClass(classDefNode node) {
    if (classes.containsKey(node.name))
      throw new semanticError("Has already defined such class: [" + node.name +
                                  "] in mainScope!",
                              node.pos);
    classes.put(node.name, new classScope(mainScope, node));
  }

  // classes cannot strate, no need to 'lookupon'
  public boolean containsClass(String name) {
    return classes.containsKey(name);
  }

  public classScope getClassScope(String name, position pos) {
    if (classes.containsKey(name))
      return classes.get(name);
    else
      throw new semanticError("Undefined class [" + name + "] in mainScope!",
                              pos);
  }

  public void defFunc(funcDefNode node) {
    if (functions.containsKey(node.name))
      throw new semanticError("Has already defined such function: [" +
                                  node.name + "] in mainScope!",
                              node.pos);
    functions.put(node.name, new funcScope(mainScope, node));
  }

  // funcs also cannot strate
  public boolean containsFunc(String name) {
    return functions.containsKey(name);
  }

  // also if no def the pos is undefined
  public funcScope getFuncScope(String name, position pos) {
    if (functions.containsKey(name))
      return functions.get(name);
    else
      throw new semanticError(
          "Undefined function: [" + name + "] in mainScope!", pos);
  }
}
