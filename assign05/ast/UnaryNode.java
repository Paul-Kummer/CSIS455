package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.ast.*;

public class UnaryNode extends Node
{
	public Token op = null;
	public UnaryNode unary = null;
	public FactorNode fact = null;
	public Token result = null;

	public UnaryNode()
	{

	}

	public UnaryNode(FactorNode fact)
	{
		
		this.fact = fact;
	}

	public UnaryNode(UnaryNode unary)
	{
		
		this.unary = unary;
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
