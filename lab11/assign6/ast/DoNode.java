package assign6.ast;

import assign6.visitor.* ;
import assign6.lexer.*;
import assign6.Env;
import assign6.ast.*;

public class DoNode extends StatementNode
{
	public StatementNode stmt = null;
	public ParenthesesNode cond = null;

	public DoNode ()
	{
		super();
	}

	public DoNode (StatementNode stmt, ParenthesesNode cond)
	{
		this.stmt = stmt;
		this.cond = cond;
	}

	public String toString()
	{
        return "do{decls; stmts;} while(cond)";
	}
	
	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}