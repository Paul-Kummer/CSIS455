package assign4.pretty ;

import assign4.visitor.* ;
import assign4.parser.* ;
import java.io.* ;

public class PrettyPrinter extends ASTVisitor 
{
	public Parser parser = null;
	int indent = 0;

	public PrettyPrinter()
	{
		visit(this.parser.cu);
	}

	public PrettyPrinter(Parser parser)
	{
		this.parser = parser;
		visit(this.parser.cu);
	}

	void print(String s)
	{
		System.out.print(s);
	}

	void println(String s)
	{
		System.out.println(s);
	}

	void printSpace()
	{
		System.out.print(" ");
	}

	void indentUp()
	{
		indent++;
	}

	void indentDown()
	{
		indent--;
	}

	void printIndent()
	{
		String s = "";

		for(int i=0; i<indent; i++)
		{
			s += " ";
		}

		print(s);
	}

    public void visit (CompilationUnit n) 
    {
        n.block.accept(this) ;
    }

	public void visit (IdentifierNode n)
    {
		//printIndent();
		print(n.id);
		//println(";");
    }

	public void visit (AssignmentNode n)
    {
		printIndent();
		n.id.accept(this);

		print(" = ");
		n.right.accept(this);

		println(";");
    }

    public void visit (BlockStatementNode n)
    {
		println("{");

		indentUp();
        n.assign.accept(this);
		indentDown();

		println("}");
    }
}
