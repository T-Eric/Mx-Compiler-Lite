package ast;
import ast.ExprNodes.ExprNode;
import utility.position;
import utility.Type;

public class varTermNode extends ASTNode {
  public String name = null;
  public ExprNode assignExpr = null;
  public exprListNode constructArgs = null;
  public Type callerType=null;// caller is varDef, this is varDef's type
  // the two should match

  public varTermNode(position pos) { super(pos); }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
