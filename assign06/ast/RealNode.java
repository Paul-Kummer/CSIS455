package assign6.ast;

import assign6.visitor.* ;
import assign6.lexer.*;
import assign6.Env;
import assign6.ast.*;

public class RealNode extends ExpressionNode 
{
	public Real v;
    public float value;
    public Type type = Type.Float;

    public RealNode () 
    {

    }
    
	public RealNode (Real v) 
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
        return Float.toString(value);
	}

    public void printNode () 
    {
        System.out.println("RealNode: " + value) ;
    }
}
