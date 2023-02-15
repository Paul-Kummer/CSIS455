package assign6.ast;

import assign6.visitor.* ;
import assign6.lexer.*;
import assign6.Env;
import assign6.ast.*;

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
}
