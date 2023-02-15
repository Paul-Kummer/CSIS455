package intercode.ast;

import intercode.visitor.* ;
import intercode.lexer.*;
import intercode.Env;
import intercode.ast.*;

public class StatementsNode extends Node
{
	/*
	assign will only allow a statement node to assign values. If
	a statement calls a function or declares a variable, this will not work.
	*/
	public StatementsNode stmts = null;
	public StatementNode stmt = null;
	public DeclarationsNode decls = null;

	public StatementsNode()
	{

	}

	public StatementsNode(StatementsNode stmts, StatementNode stmt)
	{
		this.stmts = stmts;
		this.stmt = stmt;
	}

	public String toString()
	{
        return "Statements";
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}