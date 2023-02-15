package intercode.ast;

import intercode.visitor.* ;
import intercode.lexer.*;
import intercode.Env;
import intercode.ast.*;

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
