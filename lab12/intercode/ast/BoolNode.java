package intercode.ast;

import intercode.visitor.* ;
import intercode.lexer.*;
import intercode.Env;
import intercode.ast.*;

public class BoolNode extends ExpressionNode
{
	public Word value;
	public Type type = Type.Bool;
	
	public BoolNode ()
	{

	}

	public BoolNode (Word value)
	{
		this.value = value;
	}

	public void printNode()
	{
		System.out.println("BoolNode: " + value.lexeme);
	}

	public String toString()
	{
        return this.value.toString();
	}
	
	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}

	public Token getValue()
	{
		return this.value;
	}
}
