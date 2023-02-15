package assign5.visitor ;

import assign5.parser.* ;

public class ASTVisitor {

    public void visit (CompilationUnit n) 
    {
        n.block.accept(this) ;
    }

    public void visit (BlockStatementNode n)
    {
        n.stmts.accept(this);
    }
    
    public void visit (StatementsNode n)
    {
        if(n.stmts != null)
        {
            n.assign.accept(this); //This will have to change to accept function calls
            n.stmts.accept(this);
        }
    }

    public void visit (ExpressionNode n)
    {
        n.fact.accept(this);
        n.expr.accept(this);
        if(n.bin != null) // This shouldn't be used since expression derives binary expression
        {
            n.bin.accept(this);
        }
    }

    public void visit (AssignmentNode n)
    {
        n.left.accept(this);
        n.right.accept(this); //cast is not needed
    }
    //timestamp - 33:10

    public void visit (BinaryNode n) 
    {
        n.left.accept(this) ;
        n.right.accept(this) ;
    }

    public void visit (UnaryNode n) 
    {
        n.fact.accept(this);
    }
    
    public void visit (FactorNode n)
    {
        if(n.unary != null)
        {
            n.unary.accept(this);
        }
        if(n.id != null)
        {
            n.id.accept(this);
        }
        if(n.lit != null)
        {
            n.lit.accept(this);
        }
        if(n.expr != null)
        {
            n.expr.accept(this);
        }
    }

    //this is a variable
    public void visit (LiteralNode n) 
    {
        n.printNode();
    }

    //this is a terminal symbol number or string
    public void visit (IdentifierNode n) 
    {
        n.printNode();
    }

    public void visit(Node n)
    {
        //Do Nothing
    }
}
