package assign4.parser ;

import assign4.visitor.* ;
import assign4.lexer.*;

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
		System.out.print("IdentNode: " + id);
	}
}
