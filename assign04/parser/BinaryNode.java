package assign4.parser ;

import assign4.parser.*;
import assign4.visitor.* ;
import assign4.lexer.*;

public class BinaryNode 
{
	public TermNode term = null;
	public IdentifierNode op = null;
	public ExpressionNode expr = null;

	public BinaryNode()
	{

	}

	public BinaryNode(TermNode term)
	{
		this.term = term;
	}

	public BinaryNode(TermNode term, ExpressionNode expr)
	{
		this.term = term;
		this.expr = expr;
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
