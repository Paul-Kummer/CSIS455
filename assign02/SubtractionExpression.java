public class SubtractionExpression implements Node
{
	String ident = "-";

	Node valOne = new IdentifierNode("c");
	Node valTwo = new IdentifierNode("d");
	
	@Override
	public void accept(StatementVisitor visitor)
	{
		visitor.visit(this);
	}
}