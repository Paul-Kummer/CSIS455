public class IdentifierNode implements Node
{
	String ident;

	public IdentifierNode(String id)
	{
		ident = id;
	}

	@Override
	public void accept(StatementVisitor visitor)
	{
		visitor.visit(this);
	}
}