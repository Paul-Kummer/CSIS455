package assign4.parser ;

import assign4.parser.*;
import assign4.visitor.* ;
import assign4.lexer.*;

public class TermNode
{
	public UnaryNode unary = null;
	public IdentifierNode id = null;

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

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
