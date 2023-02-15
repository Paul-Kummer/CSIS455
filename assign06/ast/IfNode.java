package assign6.ast;

import assign6.visitor.* ;
import assign6.lexer.*;
import assign6.Env;
import assign6.ast.*;

public class IfNode extends StatementNode
{
	public ParenthesesNode cond;
	public StatementNode stmt;
	public StatementNode elseStmt;
	public boolean isElif = false;
	
	public IfNode ()
	{
		super();
	}

	public IfNode (ParenthesesNode cond, BlockStatementNode stmt, StatementNode elseStmt)
	{
		this.cond = cond;
		this.stmt = stmt;
		this.elseStmt = elseStmt;
	}

	public String toString()
	{
        return "if(cond){decls; stmts;}\nelse if(cond){decls; stmts;}\nelse{decls; stmts;}";
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
