package intercode.ast;

import intercode.visitor.* ;
import intercode.lexer.*;
import intercode.Env;
import intercode.ast.*;

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
