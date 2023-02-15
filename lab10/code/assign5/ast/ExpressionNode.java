package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.Env;
import assign5.ast.*;

public class ExpressionNode extends AssignmentNode
{
	/*
		ExpressionNode -->		|| | && | == | != | < | <= | > | >= |
								+ | - | * | / | ! | -FactorNode |
								id | num | real | true | false |
								( ExpressionNode )
	*/
	// This is what is used
	public ExpressionNode expr; 
	public Type type = null;
	public Token value = null;

	public ExpressionNode fact;
	public Token op;
	public BinaryNode bin;
	public boolean parenthesis = false;

	public ExpressionNode()
	{

	}

	public ExpressionNode(ExpressionNode fact, Token op, ExpressionNode expr)
	{
		this.fact = fact; 	//left hand side (lhs)
		this.op = op;		//operator
		this.expr = expr;	//right hand side (rhs)
	}

	public ExpressionNode(ExpressionNode fact, Token op, BinaryNode bin)
	{
		this.fact = fact;
		this.op = op;
		this.bin = bin;

		this.bin.op = op;
	}

	public ExpressionNode(ExpressionNode fact, BinaryNode bin)
	{
		this.fact = fact;
		this.bin = bin;
	}

	public ExpressionNode(ExpressionNode fact, ExpressionNode expr)
	{
		this.fact = fact; 	//left hand side (lhs)
		this.expr = expr;	//right hand side (rhs)
	}

	public ExpressionNode(ExpressionNode fact)
	{
		this.fact = fact; 	//left hand side (lhs)
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
