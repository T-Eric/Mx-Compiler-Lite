package ast;

import utility.position;

import java.util.ArrayList;

import ast.ExprNodes.ExprNode;

public class exprListNode extends ASTNode {
  public ArrayList<ExprNode> expressions;

  public exprListNode(position pos) {
    super(pos);
    expressions = new ArrayList<>();
  }
  @Override
  public void accept(ASTVisitor visitor) {
    visitor.visit(this);
  }
}
