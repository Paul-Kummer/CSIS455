package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.Env;
import assign5.ast.*;

public class ArrayAccessNode extends ExpressionNode
{
	public IdentifierNode id;
	public ExpressionNode index;

	public ArrayAccessNode()
	{

	}

	public ArrayAccessNode (IdentifierNode id, ExpressionNode index)
	{
		this.id = id;
		this.index = index;
	}

	public void accept(ASTVisitor v) 
    {
        v.visit(this);
    }
}
