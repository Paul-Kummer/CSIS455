package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;

import assign5.ast.*;

public class DeclarationNode extends Node
{
	public TypeNode type = null;
	public FactorNode id = null;
	public AssignmentNode assign = null; // after a declaration there may be an assignment

	public DeclarationNode()
	{

	}

	public DeclarationNode(TypeNode type, FactorNode id) //IdentifierNode id)
	{
		this.type = type;
		this.id = id;
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
