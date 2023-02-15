package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.ast.*;

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
