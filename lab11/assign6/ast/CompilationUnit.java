package assign6.ast;

import assign6.visitor.* ;
import assign6.lexer.*;
import assign6.Env;
import assign6.ast.*;

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

    public String toString()
	{
        return "Compilation Unit";
	}

    public void accept(ASTVisitor v) 
    {

        v.visit(this);
    }
}
