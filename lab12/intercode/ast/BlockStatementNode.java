package intercode.ast;

import intercode.visitor.* ;
import intercode.lexer.*;
import intercode.Env;
import intercode.ast.*;
import intercode.inter.*;

public class BlockStatementNode extends StatementNode
{
	public Boolean isLoop = false;
	public StatementNode stmts;
	public StatementNode headStmt;
	public DeclarationNode decls;
	public DeclarationNode headDecl;
	public BlockStatementNode parent;
	public Env sTable;

	public BlockStatementNode()
	{
		super();
		this.stmts = new StatementNode();
		this.decls = new DeclarationNode();
		sTable = new Env();
	}

	public BlockStatementNode(BlockStatementNode parent)
	{
		super();
		this.parent = parent;
		sTable = new Env();
	}

	public BlockStatementNode(StatementNode stmts, DeclarationNode decls)
	{
		this.stmts = stmts;
		this.decls = decls;
		this.sTable = new Env();
	}

	public BlockStatementNode(StatementNode stmts)
	{
		this.stmts = stmts;
		this.sTable = new Env();
	}

	public BlockStatementNode(DeclarationNode decls)
	{
		this.decls = decls;
		this.sTable = new Env();
	}

	public String toString()
	{
        return "{\t}";
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
