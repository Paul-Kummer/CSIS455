package intercode.ast;

import intercode.visitor.* ;
import intercode.lexer.*;
import intercode.Env;
import intercode.ast.*;

public class BinaryNode extends ExpressionNode
{
	public AssignmentNode left = null;
	public Token op = null;
	public AssignmentNode right = null;
	public Type type;

	public StatementNode assigns = new StatementNode();

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
		this.left = left;
		this.op = op;
		this.right = right;

		this.value = getValue();
	}

	public String toString()
	{
		if(this.left != null && this.op != null && this.right != null)
		{
			return "Binary: " + this.type + " " + this.op.toString();
		}
        else if(this.left != null && this.right == null)
		{
			return "Unary: " + this.type;
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

	public Token getValue()
	{
		// Both left and right operands are children of the expression class
		// The expression class contains the attribute value, which is a token
		// The token should only be able to be Num, Real, Word, possibly Array
		// The Word token should only be able to be Word.True or Word.False
		Token returnTok = null;
		Token left = this.left != null? ((ExpressionNode)this.left).getValue(): null;
		Token right = this.right != null? ((ExpressionNode)this.right).getValue(): null;

		if(!(left instanceof Word) && right == null) // Unary numeric expression
		{
			if(left instanceof Real)
			{
				float tmp = 0;
				switch(this.op.tag)
				{
					case Tag.POSTINC :
						tmp = (((Real)left)).value + 1;
						break;
					case Tag.POSTDEC :
						tmp = (((Real)left)).value - 1;
						break;
				}
				returnTok = new Real(tmp);
			}
			else if (left instanceof Num)
			{
				int tmp = 0;
				switch(this.op.tag)
				{
					case Tag.POSTINC :
						tmp = (((Num)left)).value + 1;
						break;
					case Tag.POSTDEC :
						tmp = (((Num)left)).value - 1;
						break;
				}
				returnTok = new Num(tmp);
			}
		}
		else if(!(left instanceof Word) && !(right instanceof Word)) // Binary numeric expression
		{
			if(left instanceof Real || right instanceof Real) // return a Real
			{
				float tmp = 0;
				float leftVal = 0;
				float rightVal = 0;

				if(left instanceof Real)
				{
					leftVal = ((Real)left).getValue();
				}
				else if (left instanceof Num)
				{
					leftVal = ((float)((Num)left).getValue());
				}
				if(right instanceof Real)
				{
					rightVal = ((Real)right).getValue();
				}
				else if (right instanceof Num)
				{
					rightVal = ((float)((Num)right).getValue());
				}

				switch(this.op.tag)
				{
					case '*' :
						tmp = leftVal * rightVal;
						returnTok = new Real(tmp);
						break;
					case '/' :
						tmp = leftVal * rightVal;
						returnTok = new Real(tmp);
						break;
					case '%' :
						tmp = leftVal * rightVal;
						returnTok = new Real(tmp);
						break;
					case '+' :
						tmp = leftVal * rightVal;
						returnTok = new Real(tmp);
						break;
					case '-' :
						tmp = leftVal * rightVal;
						returnTok = new Real(tmp);
						break;							
					case '<' :
						returnTok = leftVal < rightVal? Word.True: Word.False;
						break;
					case '>' :
						returnTok = leftVal > rightVal? Word.True: Word.False;
						break;
					case Tag.GE :
						returnTok = leftVal >= rightVal? Word.True: Word.False;
						break;
					case Tag.LE :
						returnTok = leftVal <= rightVal? Word.True: Word.False;
						break;
					case Tag.EQ :
						returnTok = leftVal == rightVal? Word.True: Word.False;
						break;
					case Tag.NE :
						returnTok = leftVal != rightVal? Word.True: Word.False;
						break;
				}
			}
			else if(left instanceof Num && right instanceof Num) // return a Num
			{
				int tmp = 0;
				int leftVal = ((Num)left).getValue();
				int rightVal = ((Num)right).getValue();

				switch(this.op.tag)
				{
					case '*' :
						tmp = leftVal * rightVal;
						returnTok = new Num(tmp);
						break;
					case '/' :
						tmp = leftVal * rightVal;
						returnTok = new Num(tmp);
						break;
					case '%' :
						tmp = leftVal * rightVal;
						returnTok = new Num(tmp);
						break;
					case '+' :
						tmp = leftVal * rightVal;
						returnTok = new Num(tmp);
						break;
					case '-' :
						tmp = leftVal * rightVal;
						returnTok = new Num(tmp);
						break;							
					case '<' :
						returnTok = leftVal < rightVal? Word.True: Word.False;
						break;
					case '>' :
						returnTok = leftVal > rightVal? Word.True: Word.False;
						break;
					case Tag.GE :
						returnTok = leftVal >= rightVal? Word.True: Word.False;
						break;
					case Tag.LE :
						returnTok = leftVal <= rightVal? Word.True: Word.False;
						break;
					case Tag.EQ :
						returnTok = leftVal == rightVal? Word.True: Word.False;
						break;
					case Tag.NE :
						returnTok = leftVal != rightVal? Word.True: Word.False;
						break;
				}
			}
		}
		else if(left instanceof Word && !(right instanceof Word)) // Unary bool expression
		{
			return left;
		}
		else if(left instanceof Word && right instanceof Word) // Binary bool expression
		{
			switch(this.op.tag)
			{							
				case Tag.EQ :
					returnTok = left.tag == right.tag? Word.True: Word.False;
					break;
				case Tag.NE :
					returnTok = left.tag != right.tag? Word.True: Word.False;
					break;
				case Tag.AND :
					returnTok = (left.tag == Tag.TRUE && right.tag == Tag.TRUE)? Word.True: Word.False;
					break;
				case Tag.OR :
					returnTok = (left.tag == Tag.TRUE || right.tag == Tag.TRUE)? Word.True: Word.False;
					break;
			}
		}

		return returnTok;
	}
}
