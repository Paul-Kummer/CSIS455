public class AdditionExpression implements Node
{
	String ident = "+";

	Node valOne = new IdentifierNode("b");
	Node valTwo = new SubtractionExpression();
	
	@Override
	public void accept(StatementVisitor visitor)
	{
		visitor.visit(this);
	}
}