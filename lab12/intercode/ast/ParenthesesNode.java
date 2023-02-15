package intercode.ast;

import intercode.visitor.* ;
import intercode.lexer.*;
import intercode.Env;
import intercode.ast.*;

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