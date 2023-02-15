package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.ast.*;

public class EqualityNode  extends Node
{
	public Node left = null;
	public Token op = null; // can be ==, token(Tag.EQ) 	or 		!=, token(Tag.NE)
	public Node right = null; // The right side can only be a RelationNode
	public TrueFalseNode result = null;
	
	public void JoinNode ()
	{

	}

	public void JoinNode (EqualityNode equality)
	{
		this.left = equality;
	}

	public void JoinNode (RelationNode rel)
	{
		this.left = rel;
	}
	
	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}