package assign4.parser ;

import assign4.visitor.* ;
import assign4.lexer.* ;
import assign4.parser.IdentifierNode;

public class AssignmentNode extends Node 
{
    public Token op;
    public TermNode  id;
    public ExpressionNode right; //this will allow unary and binary expressions

    public AssignmentNode () 
    {
        
    }

    public AssignmentNode (TermNode id, ExpressionNode right) 
    {
        this.id  = id;
        this.right = right;
    }

    public void accept(ASTVisitor v) 
    {
        v.visit(this);
    }
}
