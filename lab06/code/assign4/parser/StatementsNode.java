package assign4.parser ;

import assign4.parser.*;
import assign4.visitor.* ;
import assign4.lexer.*;

public class StatementsNode extends Node
{
	/*
	assign will only allow a statement node to assign values. If
	a statement calls a function or declares a variable, this will not work.
	*/
	public StatementsNode stmts = null;
	public AssignmentNode assign;
	
	//This is used in a different version
	public StatementNode stmt = null;

	public StatementsNode()
	{

	}

	//This is used with the singley linked list
	public StatementsNode(StatementsNode stmts, StatementNode stmt)
	{
		this.stmt = stmt;
		this.stmts = stmts;
	}
	
	public StatementsNode(StatementsNode stmts, AssignmentNode assign)
	{
		this.assign = assign;
		this.stmts = stmts;
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}