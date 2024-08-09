package ast.ExprNodes;

import utility.position;

import java.util.ArrayList;

import ast.ASTVisitor;

public class formatStrExprNode extends ExprNode {
  // the value of inner expression must not be null
  // the order is String-Expr-String..., if no string at front
  // remain it empty
  public ArrayList<ExprNode> innerExprs = new ArrayList<>();
  public ArrayList<String> strings = new ArrayList<>();

  public formatStrExprNode(position pos) {
    super(pos);
    exprType = ExprType.FormatStr;
  }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
