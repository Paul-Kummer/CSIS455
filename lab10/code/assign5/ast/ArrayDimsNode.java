package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.Env;
import assign5.ast.*;

public class ArrayDimsNode extends ExpressionNode
{
	public ExpressionNode size;
	public ArrayDimsNode dim;

	public ArrayDimsNode()
	{

	}

	public ArrayDimsNode (ExpressionNode size, ArrayDimsNode dim)
	{
		this.size = size;
		this.dim = dim;
	}

	public void accept(ASTVisitor v) 
    {
        v.visit(this);
    }
}
