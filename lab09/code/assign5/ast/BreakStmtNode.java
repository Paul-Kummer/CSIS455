package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;

public class BreakStmtNode extends StatementNode
{
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