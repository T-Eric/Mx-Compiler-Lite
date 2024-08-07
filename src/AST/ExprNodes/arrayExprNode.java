package AST.ExprNodes;

import AST.ASTVisitor;
import AST.arrayBlockNode;
import Util.position;

import java.util.ArrayList;

public class arrayExprNode extends ExprNode{
  public ExprNode expr=null;//fore expression
  public ArrayList<arrayBlockNode> arr=new ArrayList<>();// array part

  public arrayExprNode(position pos){
    super(pos);
    exprType=ExprType.Array;
  }
    @Override
  public void accept(ASTVisitor visitor){
    visitor.visit(this);
  }
}
