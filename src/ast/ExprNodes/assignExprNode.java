package ast.ExprNodes;
import ast.ASTVisitor;
import utility.position;

public class assignExprNode extends ExprNode{
  public ExprNode lhsExpr=null,rhsExpr=null;

  public assignExprNode(position pos){
    super(pos);
    exprType=ExprType.Assign;
  }
    @Override
  public void accept(ASTVisitor visitor){
    visitor.visit(this);
  }
}
