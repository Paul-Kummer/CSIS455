package assign4.parser ;

import assign4.parser.*;
import assign4.visitor.* ;
import assign4.lexer.*;

public class UnaryNode 
{
	public IdentifierNode id;

	public UnaryNode()
	{

	}

	public UnaryNode(IdentifierNode id)
	{
		
		this.id = id;
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
