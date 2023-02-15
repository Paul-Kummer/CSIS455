package assign4.parser ;

import assign4.visitor.* ;
import assign4.lexer.* ;
import assign4.parser.IdentifierNode;

public class AssignmentNode extends Node {

    public IdentifierNode  id;
    public IdentifierNode right;

    public AssignmentNode () 
    {
        
    }

    public AssignmentNode (IdentifierNode id, IdentifierNode right) 
    {
        this.id  = id;
        this.right = right;
    }

    public void accept(ASTVisitor v) 
    {
        v.visit(this);
    }
}
