public interface StatementVisitor
{
	public void visit(CompilationUnit cUnit);
	public void	visit(AssignmentStatement assign);
	public void visit (IdentifierNode iNode);
	public void visit (AdditionExpression aEx);
	public void visit(SubtractionExpression sEx);
}