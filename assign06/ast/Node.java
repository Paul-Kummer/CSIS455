package assign6.ast;

import assign6.visitor.* ;
import assign6.lexer.*;
import assign6.Env;
import assign6.ast.*;

public class Node 
{

    public Node () 
    {
        
    }

    public String toString()
	{
        return "Node";
	}

    public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
