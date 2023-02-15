package assign4.visitor ;

import assign4.parser.* ;

public class ASTVisitor {

    public void visit (CompilationUnit n) 
    {
        //n.block.accept(this) ;
    }

    public void visit (BlockStatementNode n)
    {
        //n.stmt.accept(this);
    }
    
    public void visit (StatementsNode n)
    {
        if(n.stmts != null)
        {
            n.assign.accept(this); //This will have to change to accept function calls
            n.stmts.accept(this);
        }
    }

    public void visit (StatementNode n) 
    {
        //n.assign.accept(this) ;
    }

    public void visit (ExpressionNode n)
    {
        //n.term.accept(this);
        //n.expr.accept(this);
        //n.bin.accept(this);
    }

    public void visit (AssignmentNode n)
    {
        //n.id.accept(this);
        //n.right.accept(this);
    }

    public void visit (BinaryNode n) 
    {
        //n.term.accept(this) ;
        //n.expr.accept(this) ;
    }

    public void visit (UnaryNode n) 
    {
        //n.id.accept(this);
    }

    public void visit (TermNode n) 
    {
        //n.unary.accept(this);
        //n.id.accept(this);
    }

    /*
    public void visit (Factor n)
    {

    }
    */

    //this is a variable
    public void visit (LiteralNode n) 
    {
        //n.printNode();
    }

    //this is a terminal symbol number or string
    public void visit (IdentifierNode n) 
    {
        //n.printNode();
    }

    public void visit(Node n)
    {
        
    }
}
