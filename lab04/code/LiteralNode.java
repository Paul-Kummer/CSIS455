//will not call accept because the visitor method is not used

public class LiteralNode extends Node
{
	public String literal;
	
	public LiteralNode()
	{

	}
	
	public LiteralNode (String literal)
	{
		this.literal = literal;
	}
	
	@Override
	public void accept(ASTVisitor v)
	{
		printNode();
	}
	
	void printNode()
	{
		System.out.println("Literal: " + literal);
	}
}