package intercode.ast;

import intercode.visitor.* ;
import intercode.lexer.*;
import intercode.Env;
import intercode.ast.*;

public class BreakStmtNode extends StatementNode
{
	public Word value = Word.Break;

	public BreakStmtNode ()
	{
		super();
	}

	public String printNode ()
	{
		return "BreakStmtNode";
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