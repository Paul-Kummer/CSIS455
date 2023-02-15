package assign6.ast;

import assign6.visitor.* ;
import assign6.lexer.*;
import assign6.Env;
import assign6.ast.*;

public class TypeNode extends IdentifierNode 
{
	public Type basic;
    public ArrayTypeNode array = null;

    public TypeNode () 
    {
        
    }

    public TypeNode (Type basic, ArrayTypeNode array) 
    {
        this.basic = basic;
        this.array = array;
    }
    
	public TypeNode (Type basic) 
    {
        this.basic = basic;
    }
	
    public TypeNode (ArrayTypeNode array) 
    {
        this.array = array ;
    }

    public void accept(ASTVisitor v) 
    {
        v.visit(this);
    }

    public String toString()
	{
        return basic.toString();
	}

    public void printNode () 
    {
        System.out.println("TypeNode: " + basic) ;
    }
}
