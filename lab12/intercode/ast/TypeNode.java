package intercode.ast;

import intercode.visitor.* ;
import intercode.lexer.*;
import intercode.Env;
import intercode.ast.*;

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
        if(this.array != null)
        {
            return this.array.toString();
        }
        return basic.toString();
	}

    public void printNode () 
    {
        System.out.println("TypeNode: " + basic) ;
    }
}
