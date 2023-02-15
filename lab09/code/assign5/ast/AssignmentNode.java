package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.ast.*;

public class AssignmentNode extends StatementNode 
{
    // Only IdentifierNode or ArrayTypeNode can be node left
    // Possible bug is that IdentifierNode can be true or false, which can be reassigned
    public ExpressionNode left; 
    public Token op; // Do not automatically make this Token('='). It can be used to detect if assignment is actually happening
    public ExpressionNode right;
    public Token result; //This may not be needed because the symbol table will be updated instead
    public BoolNode index;

    public AssignmentNode () 
    {
        
    }

    public AssignmentNode (ArrayTypeNode left) 
    {
        this.left  = left;
    }

    public AssignmentNode (ExpressionNode left, ExpressionNode right) 
    {
        this.left  = left;
        this.right = right;
    }

    public AssignmentNode (IdentifierNode left) 
    {
        this.left  = left;
    }

    public void accept(ASTVisitor v) 
    {
        v.visit(this);
    }
}