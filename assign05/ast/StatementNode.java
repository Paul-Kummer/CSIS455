package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.ast.*;

public class StatementNode extends Node
{
	/*
	node can be AssignmentNode, DoNode, WhileNode, ForNode, IfNode, or BlockStatementNode
	*/
	public Node	node;

	public StatementNode ()
	{

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
