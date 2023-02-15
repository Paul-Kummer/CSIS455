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
        if(n.decls != null)
        {
            n.decls.accept(this);
        }
        if(n.stmts != null)
        {
            n.stmts.accept(this);  
        }
    }

    public void visit (DeclarationsNode n)
    {
        if(n.decls != null)
        {
            n.decl.accept(this);
            n.decls.accept(this);
        }
        if(n.stmts != null)
        {
            n.stmts.accept(this);  
        }
    }

    public void visit (DeclarationNode n)
    {
        n.typeNode.accept(this);
        n.id.accept(this);

        if(n.assign != null)
        {
            n.assign.accept(this);
        }
    }

    public void visit (TypeNode n)
    {
        if(n.array != null)
        {
            n.array.accept(this);
        }
    }

    public void visit (ArrayTypeNode n)
    {
        if(n.typeNode != null)
        {
            n.typeNode.accept(this);
        }
    }
    
    public void visit (StatementsNode n)
    {
        if(n.stmts != null)
        {
            n.stmt.accept(this);
            n.stmts.accept(this);
        }
        if(n.decls != null)
        {
            n.decls.accept(this);
        }
    }

    public void visit (StatementNode n)
    {
        if(n.node != null)
        {
            n.node.accept(this);
        }
    }

////////////////// NEW //////////////////////////////////////

    public void visit (ParenthesesNode n)
    {
        n.expr.accept(this);
    }

    public void visit (IfNode n)
    {
        if(n.cond != null)
        {
            n.cond.accept(this);
        }
        if(n.stmt != null)
        {
            n.stmt.accept(this);
        }
        if(n.elseStmt != null)
        {
            n.elseStmt.accept(this);
        }
    }

    public void visit (WhileNode n)
    {
        if(n.cond != null)
        {
            n.cond.accept(this);
        }
        if(n.stmt != null)
        {
            n.stmt.accept(this);
        }
    }

    public void visit (DoNode n)
    {
        if(n.stmt != null)
        {
            n.stmt.accept(this);
        }
        if(n.cond != null)
        {
            n.cond.accept(this);
        }
    }

    public void visit (ArrayAccessNode n)
    {

    }

    public void visit (ArrayDimsNode n)
    {
        n.size.accept(this);

        if(n.dim != null)
        {
            n.dim.accept(this);
        }
    }



/////////////////////////////////////////////////////////////////

    public void visit (AssignmentNode n)
    {
        if(n.left != null)
        {
            n.left.accept(this);
        }
        if(n.right != null)
        {
            n.right.accept(this);
        }
    }

    public void visit (ForNode n)
    {
        if(n.decl != null)
        {
            n.decl.accept(this);
        }
        if(n.stmt != null)
        {
            n.stmt.accept(this);
        }
        if(n.block != null)
        {
            n.block.accept(this);
        }
    }

    public void visit (BinaryNode n) 
    {
        if(n.left != null)
        {
            n.left.accept(this);
        }
        if(n.right != null)
        {
            n.right.accept(this);
        }
    }

    public void visit (IdentifierNode n) 
    {
        n.printNode();
    }

    public void visit (BreakStmtNode n)
    {

    }

    public void visit (TrueNode n)
    {

    }

    public void visit (FalseNode n)
    {

    }

    public void visit (NumNode n) //NEW
    {
        n.printNode();
    }

    public void visit (RealNode n) //NEW
    {
        n.printNode();
    }

    public void visit(Node n)
    {
        //Do Nothing
    }
}
