package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.ast.*;

public class ArrayTypeNode extends TypeNode 
{
    public Token tokType;
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

    public ArrayTypeNode (Token tokType) 
    {
        this.tokType = tokType;
    }

    public ArrayTypeNode (TypeNode typeNode) 
    {
        this.typeNode = typeNode;
    }
    
	public ArrayTypeNode (Num size, TypeNode type) 
    { 
        this.size = size;
        this.type = type;
    }

    public void accept(ASTVisitor v) 
    {
        v.visit(this);
    }
}