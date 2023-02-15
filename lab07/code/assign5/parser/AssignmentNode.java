package assign5.parser ;

import assign5.visitor.* ;
import assign5.lexer.* ;
import assign5.parser.IdentifierNode;

public class AssignmentNode extends Node 
{
    public Node left;
    public Token op;
    public Node right;

    public AssignmentNode () 
    {
        
    }

    public AssignmentNode (Node left, Node right) 
    {
        this.left  = left;
        this.right = right;
    }

    public void accept(ASTVisitor v) 
    {
        v.visit(this);
    }
}

//timestamp - 32:38