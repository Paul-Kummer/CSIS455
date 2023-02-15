package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.Env;
import assign5.ast.*;

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

    public void printNode () 
    {
        System.out.println("RealNode: " + value) ;
    }
}
