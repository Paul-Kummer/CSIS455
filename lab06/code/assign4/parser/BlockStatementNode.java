package assign4.parser ;

import assign4.parser.*;
import assign4.visitor.* ;
import assign4.lexer.*;

public class BlockStatementNode extends Node
{
	public StatementsNode stmts;
	
	//start of singley linked list, if used
	public StatementNode head = null;

	public BlockStatementNode()
	{

	}

	//
	public BlockStatementNode(StatementsNode stmts)
	{
		this.stmts = stmts;
	}

	//use this for singley linked list version
	public BlockStatementNode(StatementNode stmt)
	{
		
		this.head = stmt;
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
