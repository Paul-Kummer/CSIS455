package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.ast.*;

public class ArrayTypeNode extends TypeNode 
{
	public Type type; // element's type
    public Num size = null; // number of elements
    public IdentifierNode id = null; // The array being indexed
    public TypeNode typeNode = null;

    public ArrayTypeNode () 
    {

    }

    public ArrayTypeNode (IdentifierNode id) 
    {
        this.id = id;
    }

    public ArrayTypeNode (TypeNode typeNode) 
    {
        this.typeNode = typeNode;
        this.type = typeNode.basic;
    }

    public ArrayTypeNode (Type type) 
    {
        this.type = type;
    }
    
	public ArrayTypeNode (Num size, Type type) 
    { 
        this.size = size;
        this.type = type;
    }

    public void accept(ASTVisitor v) 
    {
        v.visit(this);
    }
}