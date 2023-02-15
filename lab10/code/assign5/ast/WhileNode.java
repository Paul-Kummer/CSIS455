package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.Env;
import assign5.ast.*;

public class WhileNode extends StatementNode
{
	public ParenthesesNode	cond = null;
	public StatementNode stmt = null;

	public WhileNode ()
	{

	}

	public WhileNode (ParenthesesNode cond, StatementNode stmt)
	{
		this.cond = cond;
		this.stmt = stmt;
	}
	
	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}