package assign5.parser ;

import assign5.parser.*;
import assign5.visitor.* ;
import assign5.lexer.*;

public class BlockStatementNode extends Node
{
	public StatementsNode stmts;

	public BlockStatementNode()
	{

	}

	public BlockStatementNode(StatementsNode stmts)
	{
		this.stmts = stmts;
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
