package assign6.ast;

import assign6.visitor.* ;
import assign6.lexer.*;
import assign6.Env;
import assign6.ast.*;

public class ArrayAccessNode extends IdentifierNode
{
	public IdentifierNode id;
	public ArrayDimsNode index; //This is an ArrayDimsNode IE: [10][3] or just [10]
	public Node	returnVal = null; //Most likely the index wasn't an integer
	public Token currentIndex;

	public ArrayAccessNode()
	{
		super();
	}

	public ArrayAccessNode (IdentifierNode id, ArrayDimsNode index)
	{
		super();
		this.id = id;
		this.index = index;
		this.type = id.type;
		this.currentIndex = this.index.size.value;
		//returnVal = ((ArrayDimsNode)index).getValue((ArrayTypeNode)id.typeNode);
	}

	/*public Node getValue()
	{
		return returnVal;
	}*/

	public String toString()
	{
		return ((ArrayDimsNode)this.index).toString() + ((IdentifierNode)this.id).lexeme;
	}

	public void accept(ASTVisitor v) 
    {
        v.visit(this);
    }
}
