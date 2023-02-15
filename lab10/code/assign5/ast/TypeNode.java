package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.Env;
import assign5.ast.*;

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

    public void printNode () 
    {
        System.out.println("TypeNode: " + basic) ;
    }
}
