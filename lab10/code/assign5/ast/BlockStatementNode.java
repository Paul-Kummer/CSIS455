package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.Env;
import assign5.ast.*;

public class BlockStatementNode extends StatementNode
{
	public StatementsNode stmts;
	public DeclarationsNode decls;
	public Env sTable;

	public BlockStatementNode()
	{
		sTable = new Env();
	}

	public BlockStatementNode(StatementsNode stmts, DeclarationsNode decls)
	{
		this.stmts = stmts;
		this.decls = decls;
		this.sTable = new Env();
	}

	public BlockStatementNode(StatementsNode stmts)
	{
		this.stmts = stmts;
		this.sTable = new Env();
	}

	public BlockStatementNode(DeclarationsNode decls)
	{
		this.decls = decls;
		this.sTable = new Env();
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
