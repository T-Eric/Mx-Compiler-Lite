package AST.ExprNodes;

import AST.ASTVisitor;
import AST.atomNode;
import Util.position;

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
