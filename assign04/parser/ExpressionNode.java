package assign4.parser ;

import assign4.parser.*;
import assign4.visitor.* ;
import assign4.lexer.*;

public class ExpressionNode 
{
	public TermNode term;
	public ExpressionNode expr;
	public BinaryNode bin;

	public ExpressionNode()
	{

	}

	public ExpressionNode(TermNode term, ExpressionNode expr)
	{
		this.term = term;
		this.expr = expr;
	}

	public ExpressionNode(TermNode term, BinaryNode bin)
	{
		this.term = term;
		this.bin = bin;
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
