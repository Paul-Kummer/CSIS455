package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.ast.*;

public class IfNode extends Node
{
	public BoolNode bool = null;
	public BlockStatementNode block = null;
	public IfNode newIf = null;
	public boolean isElseIf = false;
	
	public IfNode ()
	{

	}

	public IfNode (BoolNode bool)
	{
		this.bool = bool;
	}

	public IfNode (BlockStatementNode block)
	{
		this.block = block;
	}

	public IfNode (IfNode newIf)
	{
		this.newIf = newIf;
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
