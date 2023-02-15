package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.ast.*;

public class WhileNode extends Node
{
	public BoolNode	bool = null;
	public BlockStatementNode block = null;
	public boolean isDo = false;

	public WhileNode ()
	{

	}

	public WhileNode (BlockStatementNode block, BoolNode bool)
	{
		this.bool = bool;
		this.block = block;
	}

	public WhileNode (BoolNode bool)
	{
		this.bool = bool;
	}

	public WhileNode (BlockStatementNode block)
	{
		this.block = block;
	}
	
	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}