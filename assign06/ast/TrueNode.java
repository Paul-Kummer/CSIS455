package assign6.ast;

import assign6.visitor.* ;
import assign6.lexer.*;
import assign6.Env;
import assign6.ast.*;

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
