package ast.ExprNodes;

import ast.ASTVisitor;
import ast.arrayConstNode;
import ast.typeNode;
import utility.position;

public class newExprNode extends ExprNode {
  public typeNode rightType = null;
  public arrayConstNode arrayConst = null;

  public newExprNode(position pos) {
    super(pos);
    exprType = ExprType.New;
  }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
