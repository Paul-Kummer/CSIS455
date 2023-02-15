package intercode.inter;

import intercode.ast.*;
import intercode.lexer.*;
import intercode.visitor.*;


public class TempNode extends IdentifierNode 
{
	public static int num = 0;

	public TempNode()
	{
		
	}

	public static IdentifierNode newTemp()
	{
		num++;
		IdentifierNode temp = new IdentifierNode(new Word("t" + num, Tag.ID));
		return temp;
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}