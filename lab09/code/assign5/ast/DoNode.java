package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;

public class DoNode extends StatementNode
{
	public StatementNode stmt = null;
	public ParenthesesNode cond = null;

	public DoNode ()
	{

	}

	public DoNode (StatementNode stmt, ParenthesesNode cond)
	{
		this.stmt = stmt;
		this.cond = cond;
	}
	
	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}