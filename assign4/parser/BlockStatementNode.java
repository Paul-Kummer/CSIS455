package assign4.parser ;

import assign4.parser.*;
import assign4.visitor.* ;
import assign4.lexer.*;

public class BlockStatementNode extends Node
{
	public AssignmentNode assign;

	public BlockStatementNode()
	{

	}

	public BlockStatementNode(AssignmentNode assign)
	{
		this.assign = assign;
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
