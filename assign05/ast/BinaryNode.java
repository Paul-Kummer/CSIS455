package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.ast.*;

public class BinaryNode extends Node
{
	public Token value; //real or num

	public Node left = null;
	public Token op = null;
	public Node right = null;

	public BinaryNode()
	{

	}

	public BinaryNode(Token op)
	{
		this.op = op;
	}

	public BinaryNode(Node left)
	{
		this.left = left;
	}

	public BinaryNode(Node left, Node right)
	{
		this.left = left;
		this.right = right;
	}

	public BinaryNode(Node left, Token op, Node right)
	{
		this.left = left; //identifier, unary, 
		this.op = op;
		this.right = right;
	}

	/*public Token getValue()
	{
		float real = 0;

		switch (this.op.tag) 
		{
			case '+':
				this.real = this.left.getValue() + this.right.getValue();
				this.value = new Real(this.real);
				break;

			case '-':
				this.real = this.left.getValue() - this.right.getValue();
				this.value = new Real(this.real);
				break;

			case '*':
				this.real = this.left.getValue() * this.right.getValue();
				this.value = new Real(this.real);
				break;

			case '/':
				this.real = this.left.getValue() / this.right.getValue();
				this.value = new Real(this.real);
				break;

			case '%':
				this.real = this.left.getValue() % this.right.getValue();
				this.value = new Real(this.real);
				break;
		
			default:
				this.value = new Real(this.real);
				break;
		}

		return this.value;
	}*/

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
