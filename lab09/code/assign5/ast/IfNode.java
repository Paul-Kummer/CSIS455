package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;

public class IfNode extends StatementNode
{
	public ParenthesesNode cond;
	public StatementNode stmt;
	public StatementNode elseStmt;
	public boolean isElif = false;
	
	public IfNode ()
	{

	}

	public IfNode (ParenthesesNode cond, BlockStatementNode stmt, StatementNode elseStmt)
	{
		this.cond = cond;
		this.stmt = stmt;
		this.elseStmt = elseStmt;
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
