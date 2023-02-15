package assign4.parser ;

import assign4.parser.*;
import assign4.visitor.* ;
import assign4.lexer.*;

public class BinaryNode 
{
	public Node left = null;
	public Token op = null;
	public ExpressionNode right = null;

	public BinaryNode()
	{

	}

	public BinaryNode(Node left)
	{
		this.left = left;
	}

	public BinaryNode(Node left, ExpressionNode right)
	{
		this.left = left;
		this.right = right;
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
