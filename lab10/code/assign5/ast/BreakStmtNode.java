package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.Env;
import assign5.ast.*;

public class BreakStmtNode extends StatementNode
{
	public Word value = Word.Break;

	public BreakStmtNode ()
	{

	}

	public String printNode ()
	{
		return "break";
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}