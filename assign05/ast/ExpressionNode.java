package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.ast.*;

public class ExpressionNode extends Node
{
	/*
		ExpressionNode -->	ExpressionNode + TermNode
						|	ExpressionNode - TermNode
						|	TermNode	
	*/

	public Node left = null;
	public Token op = null;
	public Node right = null; // Only right can be a TermNode
	public Token result = null;

	public ExpressionNode()
	{

	}

	public ExpressionNode(TermNode term)
	{
		this.left = term;
	}

	public ExpressionNode(ExpressionNode expr)
	{
		this.left = expr;
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
