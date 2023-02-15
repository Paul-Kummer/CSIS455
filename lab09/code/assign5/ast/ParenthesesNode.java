package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;

public class ParenthesesNode extends ExpressionNode
{
	public ExpressionNode expr;

	public ParenthesesNode ()
	{

	}
	
	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}