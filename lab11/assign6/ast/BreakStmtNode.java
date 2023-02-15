package assign6.ast;

import assign6.visitor.* ;
import assign6.lexer.*;
import assign6.Env;
import assign6.ast.*;

public class BreakStmtNode extends StatementNode
{
	public Word value = Word.Break;

	public BreakStmtNode ()
	{
		super();
	}

	public String printNode ()
	{
		return "break";
	}

	public String toString()
	{
        return "break;";
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}