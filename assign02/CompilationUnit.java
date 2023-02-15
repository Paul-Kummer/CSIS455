public class CompilationUnit implements Node
{
	Node root = new AssignmentStatement();
	
	/*
	public CompilationUnit(String statement)
	{
		String [] parts = statement.split(" ");

		for(String ident : parts)
		{
			
		}

		root = new AssignmentStatement();
	}
	*/

	@Override
	public void accept(StatementVisitor visitor)
	{
		visitor.visit(this);
	}
}