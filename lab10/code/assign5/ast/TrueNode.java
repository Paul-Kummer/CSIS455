package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.Env;
import assign5.ast.*;

public class TrueNode extends ExpressionNode
{
	public Word value = Word.True;
	public Type type = Type.Bool;
	
	public TrueNode ()
	{

	}

	public void printNode()
	{
		System.out.println("TrueNode: " + value.lexeme);
	}
	
	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
