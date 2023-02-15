package intercode.ast;

import intercode.ast.*;
import intercode.visitor.* ;
import intercode.lexer.*;
//import java.util.ArrayList;


public class ArrayTypeNode extends TypeNode 
{
    public Num size = null; // number of elements
    public TypeNode typeNode;   // array of type
    public Type type;
    public String arrayStr;
    //public ArrayList array;
    public Node[] nodes;

    public ArrayTypeNode ()
    {

    }

    public ArrayTypeNode (Type type)
    {
        this.type = type;
    }

    public void init() 
    {
        if(typeNode instanceof ArrayTypeNode)
        {
            nodes = new ArrayTypeNode[(this.size.value)];
        }
        else if(type == Type.Int)
        {
            nodes = new NumNode[(this.size.value)];
        }
        else if(type == Type.Float)
        {
            nodes = new RealNode[(this.size.value)];
        }
        else if(type == Type.Bool || type == Type.Boolean)
        {
            nodes = new BoolNode[(this.size.value)];
        }
    }

    
	public ArrayTypeNode (Num size) 
    { 
        this.size = size;
    }

    public String ToString()
	{
		String temp = this.size == null? "": ((Num)this.size).toString();
		
        if(this.typeNode instanceof ArrayTypeNode && this.typeNode != null)
        {
            temp = ("[" + temp + "]") + this.typeNode.toString();
        }
        else
        {
            temp = "[" + temp + "]";
        }
		
		return temp;
	}

    public String toString()
	{	
		return arrayStr;
	}

    public void accept(ASTVisitor v) 
    {
        v.visit(this);
    }
}