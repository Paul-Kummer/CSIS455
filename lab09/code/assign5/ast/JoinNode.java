package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;

import assign5.ast.*;

public class JoinNode  extends Node
{
	public Node left = null;
	public Token op = null;
	public Node right = null; // The right side can only be an EqualityNode
	public TrueFalseNode result = null; // True && True = True	True && False = False False  && False = False
	
	public JoinNode ()
	{

	}

	public JoinNode (JoinNode join)
	{
		this.left = join;
	}

	public JoinNode (EqualityNode equality)
	{
		this.left = equality;
	}
	
	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}