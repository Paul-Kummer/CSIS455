package intercode.inter;

import intercode.ast.*;
import intercode.lexer.*;
import intercode.visitor.*;

//Time(5:25)
public class LabelNode extends IdentifierNode 
{
	static int label = 0;
	public ExpressionNode value;
	public static LabelNode prevLabel = null;
	public static LabelNode lastLabel = null;

	public LabelNode(Word word, Type type)
	{
		super(word, type);
	}

	public static LabelNode newLabel()
	{
		label++;
		prevLabel = lastLabel;
		lastLabel = new LabelNode(new Word("L" + label, Tag.ID), Type.Null);
		return lastLabel;
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
