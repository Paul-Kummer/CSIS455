package assign6.ast;

import assign6.visitor.* ;
import assign6.lexer.*;
import assign6.Env;
import assign6.ast.*;

public class BlockStatementNode extends StatementNode
{
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