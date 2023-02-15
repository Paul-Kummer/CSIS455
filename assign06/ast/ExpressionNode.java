package assign6.ast;

import assign6.visitor.* ;
import assign6.lexer.*;
import assign6.Env;
import assign6.ast.*;

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

	public Token op;
	public BinaryNode bin;
	public IdentifierNode fact;

	public ExpressionNode()
	{

	}

	public ExpressionNode(Token value)
	{
		this.value = value;
	}

	public ExpressionNode(Type t)
	{
		this.type = t;
	}

	public String toString()
	{
        return "Expression";
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
