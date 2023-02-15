package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;

public class TrueNode extends ExpressionNode
{
	public TrueNode ()
	{

	}

	public String printNode ()
	{
		return "true";
	}
	
	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
