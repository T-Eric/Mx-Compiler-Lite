package utility.scope;

import ast.classDefNode;
import ast.funcDefNode;
import java.util.HashMap;
import utility.Type;
import utility.Type.ASTType;
import utility.error.semanticError;
import utility.position;

public class globalScope extends scope {
  // private HashMap<String, Type> variables=new HashMap<>();// only variables
  private HashMap<String, Type> types =
      new HashMap<>(); // store all occured types in the program, variables and
                       // classes
  private HashMap<String, classScope> classes = new HashMap<>();
  private HashMap<String, funcScope> functions = new HashMap<>();

  public globalScope(scope parScope) {
    super(parScope, null);
    // parScope must be null
  }

  public void addType(String name, Type t, position pos) {
    if (types.containsKey(name))
      throw new semanticError("Undefined Identifier", pos);
    types.put(name, t);
  }

  public boolean containsType(String name) { return types.containsKey(name); }

  public boolean containsType(Type t) {
    if (t.type == ASTType.ClassName)
      return types.containsKey(t.className);
    else
      return true;
  }

  public Type getTypeFromName(String name, position pos) {
    if (types.containsKey(name))
      return types.get(name);
    else
      throw new semanticError("Undefined type: [" + name + "]!", pos);
  }

  // first collect classes, then define their methods
  public void preDefClass(classDefNode node) {
    if (classes.containsKey(node.name))
      throw new semanticError("Multiple Definitions", node.pos);
    if (functions.containsKey(node.name))
      throw new semanticError("Multiple Definitions",
                              node.pos); // actually won't twigger
    classes.put(node.name, null);
  }

  public void defClass(classDefNode node) {
    if (classes.containsKey(node.name)) {
      if (classes.get(node.name) != null)
        throw new semanticError("Multiple Definitions", node.pos);
      classes.remove(node.name);
    }
    if (functions.containsKey(node.name))
      throw new semanticError("Multiple Definitions", node.pos);
    classes.put(node.name, new classScope(this, node));
  }

  // classes cannot strate, no need to 'lookupon'
  public boolean containsClass(String name) {
    return classes.containsKey(name);
  }

  public classScope getClassScope(String name, position pos) {
    if (classes.containsKey(name))
      return classes.get(name);
    else
      throw new semanticError("Undefined Identifier",
                              pos);
  }

  public void defFunc(funcDefNode node) {
    if (functions.containsKey(node.name))
      throw new semanticError("Multiple Definitions", node.pos);
    if (classes.containsKey(node.name))
      throw new semanticError("Multiple Definitions", node.pos);
    functions.put(node.name, new funcScope(this, this, node));
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
      throw new semanticError("Undefined Identifier", pos);
  }

  @Override
  public Type getType(String name, boolean lookupon) {
    if (functions.containsKey(name))
      return new Type(functions.get(name).retType.type, false);
    return super.getType(name, false);
  }
}
