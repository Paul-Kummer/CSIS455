package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.Env;
import assign5.ast.*;

public class StatementNode extends StatementsNode
{
	/*
	node can be AssignmentNode, DoNode, WhileNode, ForNode, IfNode, or BlockStatementNode
	*/
	public Node	node;

	public StatementNode ()
	{
		super();
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
