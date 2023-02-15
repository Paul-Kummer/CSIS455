package intercode.ast;

import intercode.visitor.* ;
import intercode.lexer.*;
import intercode.Env;
import intercode.ast.*;

public class AssignmentNode extends StatementNode 
{
    // Only IdentifierNode or ArrayTypeNode can be node left
    // Possible bug is that IdentifierNode can be true or false, which can be reassigned
    public ExpressionNode left; 
    public Token op; // Do not automatically make this Token('='). It can be used to detect if assignment is actually happening
    public ExpressionNode right;
    public Token result; //This may not be needed because the symbol table will be updated instead
    public Token value;
    public Type type;

    public StatementNode assigns = new StatementNode();

    public AssignmentNode () 
    {
        super();
    }

    public AssignmentNode (ArrayTypeNode left) 
    {
        this.left  = left;
    }

    public AssignmentNode (ExpressionNode left, ExpressionNode right) 
    {
        this.op = new Token((int)'=');
        this.left  = left;
        this.right = right;
        this.type = left.type;
    }

    public AssignmentNode (IdentifierNode left) // An IdentifierNode is also an ExpressionNode
    {
        this.left  = left;
    }

    public String toString()
	{
        return this.left.toString() + " = " + this.right.toString() + ";";
	}

    public void accept(ASTVisitor v) 
    {
        v.visit(this);
    }
}