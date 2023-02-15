package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.Env;
import assign5.ast.*;

public class ArrayTypeNode extends TypeNode 
{
    public Type type;
	public TypeNode typeNode; // element's type
    public Num size = null; // number of elements
    public IdentifierNode id = null; // The array being indexed

    public ArrayTypeNode () 
    {

    }

    public ArrayTypeNode (IdentifierNode id) 
    {
        this.id = id;
    }

    public ArrayTypeNode (Type type) 
    {
        this.type = type;
    }

    public ArrayTypeNode (TypeNode typeNode) 
    {
        this.typeNode = typeNode;
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