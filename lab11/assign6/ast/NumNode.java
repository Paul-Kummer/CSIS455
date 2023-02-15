package assign6.ast;

import assign6.visitor.* ;
import assign6.lexer.*;
import assign6.Env;
import assign6.ast.*;

public class NumNode extends ExpressionNode 
{
	public Num v;
    public int value;
    public Type type = Type.Int;

    public NumNode () 
    {

    }
    
	public NumNode (Num v) 
    {
        this.v = v ;
        this.value = v.value;
    }

    public void accept(ASTVisitor v) 
    {
        v.visit(this);
    }

    public String toString()
	{
        return Integer.toString(value);
	}

    public void printNode () 
    {
        System.out.println("NumNode: " + value) ;
    }
}
