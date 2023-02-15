package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.ast.*;

public class IdentifierNode extends Node
{
	public Token w;
	public String id;
	
	public IdentifierNode()
	{

	}

	public IdentifierNode(Token w)
	{
		this.w = w;
		this.id = "" + (char)w.tag;
	}

	public IdentifierNode(Word w)
	{
		this.w = w;
		this.id = w.lexeme;
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}

	public void printNode()
	{
		System.out.println("IdentNode: " + id);
	}
}
