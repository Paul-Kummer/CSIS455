package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.Env;
import assign5.ast.*;

public class ParenthesesNode extends ExpressionNode
{
	public ExpressionNode expr;
	public Type type;

	public ParenthesesNode ()
	{

	}
	
	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}