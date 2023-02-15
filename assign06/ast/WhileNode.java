package assign6.ast;

import assign6.visitor.* ;
import assign6.lexer.*;
import assign6.Env;
import assign6.ast.*;

public class WhileNode extends StatementNode
{
	public Boolean Break = false;
	public ParenthesesNode	cond = null;
	public StatementNode stmt = null;

	public WhileNode ()
	{
		super();
	}

	public WhileNode (ParenthesesNode cond, StatementNode stmt)
	{
		this.cond = cond;
		this.stmt = stmt;
	}

	public String toString()
	{
        return "While(cond){decls; stmts;}";
	}
	
	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}