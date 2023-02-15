package assign4.parser ;

import assign4.visitor.* ;

public class CompilationUnit extends Node 
{
    public BlockStatementNode block;

    public CompilationUnit () 
    {

    }

    public CompilationUnit (BlockStatementNode block) 
    {
        this.block = block;
    }

    public void accept(ASTVisitor v) 
    {

        v.visit(this);
    }
}
