public class AssignmentStatement implements Node
{
	String ident = "=";

	Node valOne = new IdentifierNode("a");
	Node valTwo = new AdditionExpression();

	/*
	public AssignmentStatement(String statement)
	{
		valOne = 
	}
	*/
	
	@Override
	public void accept(StatementVisitor visitor)
	{
		visitor.visit(this);
	}
}