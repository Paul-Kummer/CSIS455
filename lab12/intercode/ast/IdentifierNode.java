package intercode.ast;

import intercode.visitor.* ;
import intercode.lexer.*;
import intercode.Env;
import intercode.ast.*;

public class IdentifierNode extends ExpressionNode
{
	public Type type = null; //Is this needed, the symbol table, word, and parent may store this
	public TypeNode typeNode = null; //This can be ArrayTypeNode
	public Word w = null;
	public String lexeme = "";
	
	// This shouldn't be here, but allows a value to work without being initialized.
	public ExpressionNode storedVal = new NumNode(new Num(0));
	
	public IdentifierNode()
	{
		super();
	}

	// Copy Constructor
	public IdentifierNode(IdentifierNode id)
	{
		super();
		this.type = id.type;
		this.typeNode = id.typeNode;
		this.w = id.w;
		this.lexeme = id.lexeme;
		this.storedVal = id.storedVal;
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
		this.lexeme = w.lexeme;
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}

	public String toString()
	{
		String returnStr = ("<");

		if(type != null)
		{
			returnStr += this.type.toString() + ", ";
		}
		if(typeNode != null)
		{
			returnStr += this.typeNode.toString() + ", ";
		}
		if(w != null)
		{
			returnStr += this.w.toString() + ", ";
		}
		if(storedVal != null)
		{
			if(storedVal instanceof NumNode)
			{
				returnStr += ((NumNode)this.storedVal).toString();
			}
			else if(storedVal instanceof RealNode)
			{
				returnStr += ((RealNode)this.storedVal).toString();
			}
			else if(storedVal instanceof BoolNode)
			{
				returnStr += ((BoolNode)this.storedVal).toString();
			}
			else if(storedVal instanceof ParenthesesNode)
			{
				returnStr += ((ParenthesesNode)this.storedVal).toString();
			}
			else if(storedVal instanceof BinaryNode)
			{
				returnStr += ((BinaryNode)this.storedVal).toString();
			}
			else if(storedVal instanceof ArrayAccessNode)
			{
				returnStr += ((ArrayAccessNode)this.storedVal).toString();
			}
		}
		
		returnStr += ">";

		return returnStr;
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

	public Token getValue()
	{
		if(this.storedVal instanceof NumNode)
		{
			return ((NumNode)this.storedVal).v;
		}
		else if (this.storedVal instanceof RealNode)
		{
			return ((RealNode)this.storedVal).v;
		}
		else if(this.storedVal instanceof BoolNode)
		{
			return ((BoolNode)this.storedVal).value;
		}
		else if(this.storedVal instanceof ParenthesesNode)
		{
			return ((ParenthesesNode)this.storedVal).value;
		}
		else if(this.storedVal instanceof IdentifierNode)
		{
			return ((IdentifierNode)this.storedVal).getValue();
		}

		return null;
	}
}
