package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.Env;
import assign5.ast.*;

public class IdentifierNode extends ExpressionNode
{
	public Type type = null; //Is this needed, the symbol table, word, and parent may store this
	public Word w = null;
	public String lexeme = "";
	
	public IdentifierNode()
	{
		super();
	}

	public IdentifierNode(Word w, Type t)
	{
		this.w = w;
		this.lexeme = w.lexeme;
		this.type = t;
	}

	public IdentifierNode(Word w)
	{
		this.w = w;
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
