package assign6.ast;

import assign6.visitor.* ;
import assign6.lexer.*;
import assign6.Env;
import assign6.ast.*;

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
