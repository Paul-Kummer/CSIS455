package assign6.ast;

import assign6.visitor.* ;
import assign6.lexer.*;
import assign6.Env;
import assign6.ast.*;

public class DeclarationsNode extends Node
{
	public DeclarationsNode decls = null;
	public DeclarationNode decl = null;
	public StatementsNode stmts = null;

	public DeclarationsNode()
	{

	}

	public DeclarationsNode(DeclarationsNode decls, DeclarationNode decl)
	{
		this.decls = decls;
		this.decl = decl;
	}

	public DeclarationsNode(DeclarationNode decl)
	{
		this.decl = decl;
	}

	public DeclarationsNode(DeclarationsNode decls)
	{
		this.decls = decls;
	}

    public String toString()
	{
        return "Declarations";
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}