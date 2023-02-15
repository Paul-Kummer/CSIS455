package intercode.ast;

import intercode.visitor.* ;
import intercode.lexer.*;
import intercode.Env;
import intercode.ast.*;

public class NumNode extends ExpressionNode 
{
	public Num v;
    public int intValue;
    public static final Type type = Type.Int;

    public NumNode () 
    {

    }
    
	public NumNode (Num v) 
    {
        this.v = v ;
        this.intValue = v.value;
        this.value = v;
    }

    public void accept(ASTVisitor v) 
    {
        v.visit(this);
    }

    public String toString()
	{
        return Integer.toString(intValue);
	}

    public void printNode () 
    {
        System.out.println("NumNode: " + intValue) ;
    }

    public Token getValue()
	{
		return this.v;
	}
}
