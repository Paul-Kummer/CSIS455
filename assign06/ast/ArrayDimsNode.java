package assign6.ast;

import assign6.visitor.* ;
import assign6.lexer.*;
import assign6.Env;
import assign6.ast.*;

public class ArrayDimsNode extends ExpressionNode
{
	public ExpressionNode size; // Is this actually index???
	public ArrayDimsNode dim = null;
	public String arrayStr;

	public ArrayDimsNode()
	{
		super();
	}

	public ArrayDimsNode(Type t)
	{
		super();
		this.type = t;
	}

	/*public Node getValue(ArrayTypeNode arr)
	{
		Node returnVal = arr.nodes[((Num)size.value).value];
		
		if(this.dim != null)
		{
			returnVal = this.dim.getValue(returnVal);
		}
		
		return returnVal;
	}*/

	public ArrayDimsNode (ExpressionNode size, ArrayDimsNode dim)
	{
		this.size = size;
		this.dim = dim;
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
