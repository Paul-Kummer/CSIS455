package intercode.ast;

import intercode.visitor.* ;
import intercode.lexer.*;
import intercode.Env;
import intercode.ast.*;

public class DeclarationNode extends Node
{
	public DeclarationNode head = null;
	public DeclarationNode nextDecl = null;
	public TypeNode typeNode = null;
	public IdentifierNode id = null;
	public AssignmentNode assign = null; // after a declaration there may be an assignment

	public DeclarationNode()
	{

	}

	public DeclarationNode(DeclarationNode head)
	{
		this.head = head;
	}

	public DeclarationNode(TypeNode typeNode, IdentifierNode id)
	{
		this.typeNode = typeNode;
		this.id = id;
	}

	public String toString()
	{
        return "Declaration";
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
