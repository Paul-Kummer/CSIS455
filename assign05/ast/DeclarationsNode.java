package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.ast.*;

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

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}