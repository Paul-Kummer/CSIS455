package assign5.parser ;

import assign5.parser.*;
import assign5.visitor.* ;
import assign5.lexer.*;

public class StatementsNode extends Node
{
	/*
	assign will only allow a statement node to assign values. If
	a statement calls a function or declares a variable, this will not work.
	*/
	public StatementsNode stmts = null;
	public AssignmentNode assign;

	public StatementsNode()
	{

	}

	//This is used with the singley linked list
	public StatementsNode(StatementsNode stmts, AssignmentNode assign)
	{
		this.stmts = stmts;
		this.assign = assign;
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}