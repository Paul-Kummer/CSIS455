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

    //this is a terminal symbol
    public void visit (LiteralNode n) 
    {
        n.printNode();
    }

    //this is a terminal symbol
    public void visit (IdentifierNode n) 
    {
        n.printNode();
    }
}
