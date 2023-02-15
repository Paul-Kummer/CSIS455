package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;

import assign5.ast.*;

public class ForNode extends StatementNode
{
	// for (int x = 0; x == Num; x = x + 1) { Do Stuff }
	public DeclarationNode decl;
	public BoolNode bool;
	public StatementNode stmt; // Not currently used, but may need to be implemented
	public BlockStatementNode block;
	
	public ForNode ()
	{

	}

	public ForNode (DeclarationNode decl)
	{
		this.decl = decl;
	}

	public ForNode (BoolNode bool)
	{
		this.bool = bool;
	}

	public ForNode (StatementNode stmt)
	{
		this.stmt = stmt;
	}

	public ForNode (BlockStatementNode block)
	{
		this.block = block;
	}

	public ForNode (DeclarationNode decl, BoolNode bool, StatementNode stmt, BlockStatementNode block)
	{
		this.decl = decl;
		this.bool = bool;
		this.stmt = stmt;
		this.block = block;
	}
	
	public ForNode (DeclarationNode decl, BoolNode bool, BlockStatementNode block)
	{
		this.decl = decl;
		this.bool = bool;
		this.block = block;
	}
	
	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}