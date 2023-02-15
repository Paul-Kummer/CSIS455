package intercode.ast;

import intercode.visitor.* ;
import intercode.lexer.*;
import intercode.Env;
import intercode.ast.*;

public class TrueNode extends BoolNode
{	
	public TrueNode ()
	{
		super(Word.True);
	}

	public void printNode()
	{
		System.out.println("TrueNode: " + value.lexeme);
	}

	public String toString()
	{
        return "true";
	}
	
	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
