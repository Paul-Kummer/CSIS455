import java.util.*;

public class CompilationUnit extends Node
{
	//public AssignmentNode assign;
	Queue<Token> toks;
	
	public CompilationUnit(Queue<Token> t)
	{
		this.toks = t;
	}
	
	@Override
	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
