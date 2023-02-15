public interface Node
{
	public void accept(StatementVisitor visitor);
	//public void accept(AssignmentStatement visitor);
}