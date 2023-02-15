package assign5.parser ;

import assign5.lexer.*;
import assign5.parser.ExpressionNode;
import assign5.visitor.* ;

public class FactorNode extends Node
{
	public UnaryNode unary = null;
	public IdentifierNode id = null;
	public LiteralNode lit = null;
	public ExpressionNode expr = null;

	public FactorNode()
	{

	}

	public FactorNode(UnaryNode unary)
	{
		
		this.unary = unary;
	}

	public FactorNode(IdentifierNode id)
	{
		
		this.id = id;
	}

	public FactorNode(LiteralNode lit)
	{
		
		this.lit = lit;
	}

	public FactorNode(ExpressionNode expr)
	{
		
		this.expr = expr;
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
