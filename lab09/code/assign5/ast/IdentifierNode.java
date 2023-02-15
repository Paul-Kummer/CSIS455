package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.ast.*;

public class IdentifierNode extends ExpressionNode
{
	public TypeNode type = null; //Is this needed
	public Word w = null; //Should only be able to be a word, and be Tag.ID, Tag.TRUE, Tag.FALSE
	public String lexeme = "";
	
	public IdentifierNode()
	{
		super();
	}

	public IdentifierNode(Word w)
	{
		this.w = w;
		this.lexeme = w.lexeme;
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}

	public void printNode()
	{
		System.out.println("IdentNode: " + lexeme);
	}
}
