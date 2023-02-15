package assign4.parser ;

import assign4.parser.*;
import assign4.visitor.* ;
import assign4.lexer.*;

public class UnaryNode extends Node
{
	public TermNode term;

	public UnaryNode()
	{

	}

	public UnaryNode(TermNode t)
	{
		
		this.term = t;
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
