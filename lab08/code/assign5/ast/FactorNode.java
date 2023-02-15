package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.ast.*;

public class FactorNode extends Node
{
	public UnaryNode unary = null;
	public IdentifierNode id = null;
	public NumNode num = null;
	public RealNode real = null;
	public ArrayTypeNode array = null;
	public BlockStatementNode block = null;
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

	public FactorNode(NumNode num)
	{
		
		this.num = num;
	}

	public FactorNode(RealNode real)
	{
		this.real = real;
	}

	public FactorNode(ArrayTypeNode array)
	{
		this.array = array;
	}

	public FactorNode(BlockStatementNode block)
	{
		this.block = block;
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
