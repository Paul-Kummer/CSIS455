package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.ast.*;

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
