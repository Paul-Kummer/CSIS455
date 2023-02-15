package intercode.ast;

import intercode.visitor.* ;
import intercode.lexer.*;
import intercode.Env;
import intercode.ast.*;

public class RealNode extends ExpressionNode 
{
	public Real v;
    public float floatValue;
    public static final Type type = Type.Float;

    public RealNode () 
    {

    }
    
	public RealNode (Real v) 
    {
        this.v = v ;
        this.floatValue = v.value;
    }

    public void accept(ASTVisitor v) 
    {
        v.visit(this);
    }

    public String toString()
	{
        return Float.toString(floatValue);
	}

    public void printNode () 
    {
        System.out.println("RealNode: " + floatValue) ;
    }

    public Token getValue()
	{
		return this.v;
	}
}
