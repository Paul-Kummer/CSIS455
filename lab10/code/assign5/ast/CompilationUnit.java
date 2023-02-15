package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.Env;
import assign5.ast.*;

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
