package assign6.ast;

import assign6.visitor.* ;
import assign6.lexer.*;
import assign6.Env;
import assign6.ast.*;

public class BinaryNode extends ExpressionNode
{
	public ExpressionNode left = null;
	public Token op = null;
	public ExpressionNode right = null;
	public Type type;

	public BinaryNode()
	{
		super();
	}

	public BinaryNode(Token op) 
	{
		this.op = op;
	}

	public BinaryNode(ExpressionNode left)
	{
		this.left = left;
	}

	public BinaryNode(ExpressionNode left, ExpressionNode right)
	{
		this.left = left;
		this.right = right;
	}

	public BinaryNode(ExpressionNode left, Token op, ExpressionNode right)
	{
		super();
		this.left = left; //only left, then unary
		this.op = op;
		this.right = right;
	}

	public String toString()
	{
		if(this.left != null && this.op != null && this.right != null)
		{
			return this.left.toString() + " " + this.op.toString() + " " + this.right.toString();
		}
        else if(this.left != null)
		{
			return this.left.toString();
		}
		else
		{
			return "";
		}
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
