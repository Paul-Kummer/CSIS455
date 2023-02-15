package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.Env;
import assign5.ast.*;

public class FalseNode extends ExpressionNode
{
	public Word value = Word.False;
	public Type type = Type.Bool;
	
	public FalseNode ()
	{

	}

	public void printNode()
	{
		System.out.println("FalseNode: " + value.lexeme);
	}
	
	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
