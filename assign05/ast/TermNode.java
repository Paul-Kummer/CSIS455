package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.ast.*;

public class TermNode extends Node
{
	public Node left = null;
	public Token op = null;
	public Node right = null; // Only right can be a UnaryNode
	public Token result = null;

	public TermNode()
	{

	}

	public TermNode(UnaryNode unary)
	{
		this.left = unary;
	}

	public TermNode(TermNode term)
	{
		this.left = term;
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}