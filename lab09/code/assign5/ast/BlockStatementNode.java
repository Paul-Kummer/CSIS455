package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.ast.*;

public class BlockStatementNode extends StatementNode
{
	public StatementsNode stmts;
	public DeclarationsNode decls;

	public BlockStatementNode()
	{

	}

	public BlockStatementNode(StatementsNode stmts, DeclarationsNode decls)
	{
		this.stmts = stmts;
		this.decls = decls;
	}

	public BlockStatementNode(StatementsNode stmts)
	{
		this.stmts = stmts;
	}

	public BlockStatementNode(DeclarationsNode decls)
	{
		this.decls = decls;
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
