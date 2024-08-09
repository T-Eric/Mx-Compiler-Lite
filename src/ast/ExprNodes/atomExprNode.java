package ast.ExprNodes;

import ast.ASTVisitor;
import ast.atomNode;
import utility.position;

public class atomExprNode extends ExprNode{
  public atomNode atom=null;

  public atomExprNode(position pos){
    super(pos);
    exprType=ExprType.Atom;
  }
    @Override
  public void accept(ASTVisitor visitor){
    visitor.visit(this);
  }
}
