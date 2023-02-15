package assign6.ast;

import assign6.visitor.* ;
import assign6.lexer.*;
import assign6.Env;
import assign6.ast.*;

public class StatementNode extends Node
{
	/*
	node can be AssignmentNode, DoNode, WhileNode, ForNode, IfNode, or BlockStatementNode
	*/
	public StatementNode head = null;
	public StatementNode nextStmt = null;
	public Boolean isConditional = false;
	public Node	node;
	public Type type;

	public StatementNode ()
	{

	}

	public StatementNode(StatementNode head)
	{
		this.head = head;
	}

	public StatementNode(StatementNode head, Node node)
	{
		this.head = head;
		this.node = node;
	}

	public StatementNode(Node node)
	{
		this.node = node;
	}

	public String toString()
	{
		if(this.node != null)
		{
			return node.toString();
		}
		else if(this.type != null)
		{
			return this.type.toString();
		}
		else
		{
			return "Statement";
		}
	}
	
	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
