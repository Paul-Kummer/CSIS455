package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.ast.*;

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

    public AssignmentNode (Node left) 
    {
        this.left  = left;
    }

    public void accept(ASTVisitor v) 
    {
        v.visit(this);
    }
}