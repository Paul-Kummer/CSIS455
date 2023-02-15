package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;

import assign5.ast.*;

public class BoolNode  extends Node
{
	public Node left = null;
	public Token op = null;
	public Node right = null; // The right side can only be a JoinNode
	public Node result = null; // True | False = True; True | True = True; False | False = Fale
	public boolean parenthesis = false;
	
	public BoolNode ()
	{

	}

	public BoolNode (JoinNode join)
	{
		this.left = join;
	}

	public BoolNode (BoolNode bool)
	{
		this.left = bool;
	}
	
	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
