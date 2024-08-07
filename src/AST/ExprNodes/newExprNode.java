package AST.ExprNodes;

import AST.ASTVisitor;
import Util.Type;
import Util.position;

public class newExprNode extends ExprNode {
  public Type type;

  public newExprNode(position pos) {
    super(pos);
    exprType = ExprType.New;
  }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
