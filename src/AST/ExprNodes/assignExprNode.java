package AST.ExprNodes;
import AST.ASTVisitor;
import Util.position;

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
