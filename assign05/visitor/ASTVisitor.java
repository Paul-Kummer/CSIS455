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
        n.type.accept(this);
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
        if(n.id != null)
        {
            n.id.accept(this);
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

    public void visit (DoNode n)
    {
        if(n.block != null)
        {
            n.block.accept(this);
        }
        if(n.bool != null)
        {
            n.bool.accept(this);
        }
    }

    public void visit (WhileNode n)
    {
        if(n.block != null)
        {
            n.block.accept(this);
        }
        if(n.bool != null)
        {
            n.bool.accept(this);
        }
    }

    public void visit (ForNode n)
    {
        if(n.decl != null)
        {
            n.decl.accept(this);
        }
        if(n.bool != null)
        {
            n.bool.accept(this);
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

    public void visit (IfNode n)
    {
        if(n.bool != null)
        {
            n.bool.accept(this);
        }
        if(n.block != null)
        {
            n.block.accept(this);
        }
        if(n.newIf != null)
        {
            n.newIf.accept(this);
        }
    }

    public void visit (BoolNode n)
    {
        if(n.left != null)
        {
            n.left.accept(this);
        }
        if(n.right != null)
        {
            n.right.accept(this);
        }
        if(n.result != null)
        {
            n.result.accept(this);
        }
    }

    public void visit (JoinNode n)
    {
        if(n.left != null)
        {
            n.left.accept(this);
        }
        if(n.right != null)
        {
            n.right.accept(this);
        }
        if(n.result != null)
        {
            n.result.accept(this);
        }
    }

    public void visit (EqualityNode n)
    {
        if(n.left != null)
        {
            n.left.accept(this);
        }
        if(n.right != null)
        {
            n.right.accept(this);
        }
        if(n.result != null)
        {
            n.result.accept(this);
        }
    }

    public void visit (RelationNode n)
    {
        if(n.left != null)
        {
            n.left.accept(this);
        }
        if(n.right != null)
        {
            n.right.accept(this);
        }
        if(n.result != null)
        {
            n.result.accept(this);
        }
    }

    public void visit (ExpressionNode n)
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

    public void visit (TermNode n)
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

    public void visit (UnaryNode n) 
    {
        if(n.unary != null)
        {
            n.unary.accept(this);
        }
        if(n.fact != null)
        {
            n.fact.accept(this);
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
    
    public void visit (FactorNode n)
    {
        if(n.trueFalse != null)
        {
            n.trueFalse.accept(this);
        }
        if(n.assign != null)
        {
            n.assign.accept(this);
        }
        if(n.num != null) //NEW, NumNode (former literal)
        {
            n.num.accept(this);
        }
        if(n.real != null) //NEW, RealNode
        {
            n.real.accept(this);
        }
        if(n.bool != null)
        {
            n.bool.accept(this);
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

    public void visit (TrueFalseNode n) //NEW
    {
        n.printNode();
    }

    public void visit(Node n)
    {
        //Do Nothing
    }
}
