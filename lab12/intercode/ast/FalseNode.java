package intercode.ast;

import intercode.visitor.* ;
import intercode.lexer.*;
import intercode.Env;
import intercode.ast.*;

public class FalseNode extends BoolNode
{	
	public FalseNode ()
	{
		super(Word.False);
	}

	public void printNode()
	{
		System.out.println("FalseNode: " + value.lexeme);
	}

	public String toString()
	{
        return "false";
	}
	
	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
