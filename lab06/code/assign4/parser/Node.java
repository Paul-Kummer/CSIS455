package assign4.parser ;

import assign4.visitor.* ;

public class Node 
{

    public Node () 
    {
        
    }

    public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
