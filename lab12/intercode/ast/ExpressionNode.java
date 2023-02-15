package intercode.ast;

import intercode.visitor.* ;
import intercode.lexer.*;
import intercode.Env;
import intercode.ast.*;

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

	public Token getValue()
	{
		return this.value;
	}
}
