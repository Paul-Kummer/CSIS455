public class AssignmentStatementVisitor implements StatementVisitor
{
	int level = 0;
	String dots = "";

	@Override
	public void visit(CompilationUnit cUnit)
	{
		dots = new String(new char[level*4]).replace('\0', '.');
		System.out.println(dots + "Compilation Unit");

		level++;
		cUnit.root.accept(this);
		level--;
	}

	@Override
	public void visit(AssignmentStatement assign)
	{
		dots = new String(new char[level*4]).replace('\0', '.');
		System.out.println(dots + "Assignment Statement: " + assign.ident);

		level++;
		assign.valOne.accept(this);
		level--;

		level++;
		assign.valTwo.accept(this);
		level--;
	}

	@Override
	public void visit(IdentifierNode iNode)
	{
		dots = new String(new char[level*4]).replace('\0', '.');
		System.out.println(dots + "Identifier: " + iNode.ident);
	}

	@Override
	public void visit(AdditionExpression aEx)
	{
		dots = new String(new char[level*4]).replace('\0', '.');
		System.out.println(dots + "Addition Expression: " + aEx.ident);

		level++;
		aEx.valOne.accept(this);
		level--;

		level++;
		aEx.valTwo.accept(this);
		level--;
	}

	@Override
	public void visit(SubtractionExpression sEx)
	{
		dots = new String(new char[level*4]).replace('\0', '.');
		System.out.println(dots + "Subtraction Expression: " + sEx.ident);

		level++;
		sEx.valOne.accept(this);
		level--;

		level++;
		sEx.valTwo.accept(this);
		level--;
	}
}