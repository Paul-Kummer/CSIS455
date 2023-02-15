package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.ast.*;

public class ArrayTypeNode extends TypeNode 
{
	public TypeNode type; // element's type
    public int size = 1; // number of elements

    public ArrayTypeNode () 
    {

    }
    
	public ArrayTypeNode (int size, TypeNode type) 
    { 
        this.size = size;
        this.type = type;
    }

    public void accept(ASTVisitor v) 
    {
        v.visit(this);
    }
}