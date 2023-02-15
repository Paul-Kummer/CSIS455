package assign4.parser ;

import assign4.parser.*;
import assign4.visitor.* ;
import assign4.lexer.*;

public class StatementNode extends Node
{
	public AssignmentNode assign;

	//will create a singley linked list of statements
	public StatementNode nextStatement = null;

	public StatementNode()
	{

	}

	public StatementNode(AssignmentNode assign)
	{
		
		this.assign = assign;
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
