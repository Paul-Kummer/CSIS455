package assign4.parser ;

import assign4.parser.*;
import assign4.visitor.* ;
import assign4.lexer.*;

public class TermNode extends Node
{
	public UnaryNode unary = null;
	public IdentifierNode id = null;
	public LiteralNode lit = null;

	public TermNode()
	{

	}

	public TermNode(UnaryNode unary)
	{
		
		this.unary = unary;
	}

	public TermNode(IdentifierNode id)
	{
		
		this.id = id;
	}

	public TermNode(LiteralNode lit)
	{
		
		this.lit = lit;
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
