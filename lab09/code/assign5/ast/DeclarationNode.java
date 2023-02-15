package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;

import assign5.ast.*;

public class DeclarationNode extends DeclarationsNode
{
	public TypeNode type = null;
	public IdentifierNode id = null;
	public AssignmentNode assign = null; // after a declaration there may be an assignment

	public DeclarationNode()
	{
		super();
	}

	public DeclarationNode(TypeNode type, IdentifierNode id)
	{
		this.type = type;
		this.id = id;
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
