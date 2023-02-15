package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.ast.*;

public class DoNode extends Node
{
	public BlockStatementNode block = null;
	public BoolNode bool = null;
	public WhileNode whileNode = null;	

	public DoNode ()
	{

	}

	public DoNode (BlockStatementNode block, BoolNode bool)
	{
		this.block = block;
		this.bool = bool;
	}

	public DoNode (BlockStatementNode block)
	{
		this.block = block;
	}

	public DoNode (BoolNode bool)
	{
		this.bool = bool;
	}
	
	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}