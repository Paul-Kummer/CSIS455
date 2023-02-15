package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.ast.*;

public class NumNode extends ExpressionNode 
{
	public Num v;
    public int value;

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

    public void printNode () 
    {
        System.out.println("NumNode: " + value) ;
    }
}
