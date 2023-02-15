package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.ast.*;

public class FactorNode extends Node
{
	public TrueFalseNode		trueFalse 	= null;
	public AssignmentNode 		assign 		= null;
	public NumNode 				num 		= null;
	public RealNode 			real 		= null;
	public BoolNode				bool		= null;

	//might use this to avoid checking all the different types of nodes
	//public Node	node = null;

	public Token				result		= null;

	public FactorNode()
	{

	}

	public FactorNode(TrueFalseNode trueFalse)
	{
		this.trueFalse = trueFalse;
	}

	public FactorNode(AssignmentNode assign)
	{
		this.assign = assign;
	}

	public FactorNode(NumNode num)
	{	
		this.num = num;
	}

	public FactorNode(RealNode real)
	{
		this.real = real;
	}

	public FactorNode(BoolNode bool)
	{
		this.bool = bool;
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
