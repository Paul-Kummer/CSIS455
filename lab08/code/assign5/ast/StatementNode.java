package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.ast.*;

public class StatementNode extends Node
{
	public AssignmentNode assign;
	public Node	node;

	public StatementNode ()
	{

	}

	public StatementNode (AssignmentNode assign)
	{
		this.assign = assign;
	}

	public StatementNode (Node node)
	{
		this.node = node;
	}
	
	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
