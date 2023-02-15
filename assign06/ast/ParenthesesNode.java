package assign6.ast;

import assign6.visitor.* ;
import assign6.lexer.*;
import assign6.Env;
import assign6.ast.*;

public class ParenthesesNode extends ExpressionNode
{
	public ExpressionNode expr;
	public Type type;

	public ParenthesesNode ()
	{
		super();
	}

	public String toString()
	{
        return "( " + this.expr.toString() + " )";
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}