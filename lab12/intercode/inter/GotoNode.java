package intercode.inter;

import intercode.ast.*;
import intercode.lexer.*;
import intercode.visitor.*;


public class GotoNode extends IdentifierNode 
{
	public static int num = 0;

	public GotoNode()
	{
		
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
