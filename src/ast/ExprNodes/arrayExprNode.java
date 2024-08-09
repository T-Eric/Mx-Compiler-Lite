package ast.ExprNodes;

import utility.position;

import java.util.ArrayList;

import ast.ASTVisitor;
import ast.arrayBlockNode;

public class arrayExprNode extends ExprNode{
  public ExprNode nameExpr=null;//fore expression
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
