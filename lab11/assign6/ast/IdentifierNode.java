package assign6.ast;

import assign6.visitor.* ;
import assign6.lexer.*;
import assign6.Env;
import assign6.ast.*;

public class IdentifierNode extends ExpressionNode
{
	public Type type = null; //Is this needed, the symbol table, word, and parent may store this
	public TypeNode typeNode = null; //This can be ArrayTypeNode
	public Word w = null;
	public String lexeme = "";
	
	public IdentifierNode()
	{
		super();
	}

	public IdentifierNode(Word w, TypeNode t)
	{
		this.w = w;
		this.lexeme = w.lexeme;
		this.typeNode = t;
		this.type = t.basic;
	}

	public IdentifierNode(Word w, Type t)
	{
		this.w = w;
		this.lexeme = w.lexeme;
		this.type = t;
		this.typeNode = new TypeNode(t);
	}

	public IdentifierNode(Word w)
	{
		this.w = w;
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}

	public String toString()
	{
        return this.w.toString();
	}

	public void printNode()
	{
		System.out.print("IdentifierNode: " + lexeme + " | Type: " + type);
		if(this.typeNode != null && this.typeNode.array != null)
		{
			System.out.print(" | Array: " + ((ArrayTypeNode)this.typeNode.array).toString());
		}
		System.out.println("");
	}
}
