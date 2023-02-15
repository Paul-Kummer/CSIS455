package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;

public class FalseNode extends ExpressionNode
{
	public FalseNode ()
	{

	}

	public String printNode ()
	{
		return "false";
	}
	
	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
