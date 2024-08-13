package ast.ExprNodes;

import ast.ASTVisitor;
import ast.arrayConstNode;
import utility.position;

public class arrayConstExprNode extends ExprNode {
  public arrayConstNode arrayConst = null;

  public arrayConstExprNode(position pos) {
    super(pos);
    exprType = ExprType.ArrayConst;
  }

  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
