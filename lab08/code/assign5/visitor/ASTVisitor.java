package assign5.visitor ;

import assign5.ast.* ;

public class ASTVisitor 
{
    public void visit (CompilationUnit n) 
    {
        n.block.accept(this) ;
    }

    public void visit (BlockStatementNode n)
    {
        n.stmts.accept(this);
        n.decls.accept(this);
    }

    public void visit (DeclarationsNode n) //NEW
    {
        if(n.decls != null)
        {
            n.decl.accept(this);
            n.decls.accept(this);
        }
    }

    public void visit (DeclarationNode n) //NEW
    {
        n.type.accept(this);
        n.id.accept(this);

        if(n.assign != null)
        {
            n.assign.accept(this);
        }
    }
    
    public void visit (StatementsNode n)
    {
        if(n.stmts != null)
        {
            n.stmt.accept(this); //NEW accepts stmt instead of assign
            n.stmts.accept(this);
        }
    }

    public void visit (StatementNode n) //NEW
    {
        n.assign.accept(this);
        if(n.node != null)
        {
            n.node.accept(this);
        }
    }

    public void visit (ExpressionNode n)
    {
        n.fact.accept(this);
        n.expr.accept(this);
        if(n.bin != null)
        {
            n.bin.accept(this);
        }
    }

    public void visit (AssignmentNode n)
    {
        n.left.accept(this);
        n.right.accept(this);
    }

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
        if(n.unary != null) //UnaryNode
        {
            n.unary.accept(this);
        }
        if(n.id != null) //IdentifierNode
        {
            n.id.accept(this);
        }
        if(n.num != null) //NEW, NumNode (former literal)
        {
            n.num.accept(this);
        }
        if(n.real != null) //NEW, RealNode
        {
            n.real.accept(this);
        }
        if(n.block != null) //NEW, BlockStatementNode
        {
            n.block.accept(this);
        }
        if(n.expr != null) //ExpressionNode
        {
            n.expr.accept(this);
        }
    }

    public void visit (IdentifierNode n) 
    {
        n.printNode();
    }

    public void visit (NumNode n) //NEW
    {
        n.printNode();
    }

    public void visit (RealNode n) //NEW
    {
        n.printNode();
    }

    public void visit (TypeNode n) //NEW
    {
        System.out.println("I'm in the wrong visit");
        n.array.accept(this);
    }

    public void visit (ArrayTypeNode n) //NEW
    {
        n.type.accept(this);
    }

    public void visit(Node n)
    {
        //Do Nothing
    }
}
