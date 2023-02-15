package assign5.parser ;

import assign5.parser.*;
import assign5.visitor.* ;
import assign5.lexer.*;

public class UnaryNode extends Node
{
	public FactorNode fact;

	public UnaryNode()
	{

	}

	public UnaryNode(FactorNode fact)
	{
		
		this.fact = fact;
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
