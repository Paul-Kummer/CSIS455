package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;

import assign5.ast.*;

public class RelationNode  extends Node
{	
	public ExpressionNode left = null;
	public Token op = null; // can be token('<'), token('>'), token(Tag.LE), token(Tag.GE)
	public ExpressionNode right = null; // The right side can only be a RelationNode
	public TrueFalseNode result = null;
	
	public RelationNode ()
	{

	}

	public RelationNode (ExpressionNode expr)
	{
		this.left = expr;
	}

	public RelationNode (ExpressionNode expr, ExpressionNode exprTwo)
	{
		this.left = expr;
		this.right = exprTwo;
	}
	
	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}