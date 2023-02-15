package assign5.unparser ;

import assign5.visitor.* ;
import assign5.parser.* ;

public class TreePrinter extends ASTVisitor
{
	public Parser parser = null;

	int level = 0;
	String indent = "....";
	int indentLevel = 0;

	public TreePrinter (Parser parser)
	{
		this.parser = parser;
		visit(this.parser.cu);
	}

	///////////////////////////////////////////////////
	//              Utility Methods                  //
	///////////////////////////////////////////////////

	// print the dots for displaying the abstract syntax tree (AST)
	private void dots()
	{
		System.out.print(new String(new char[indentLevel*4]).replace('\0', '.'));
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
		indentLevel++;
	}

	void indentDown()
	{
		indentLevel--;
	}

	void printIndent()
	{
		String s = "";
		for(int indent=0; indent<indentLevel; indent++)
		{
			s += "    ";
		}

		print(s);
	}

	///////////////////////////////////////////////////
	//              Visit Methods                    //
	///////////////////////////////////////////////////

	public void visit (CompilationUnit n) 
    {
		println("CompilationUnit");

		indentUp();
        n.block.accept(this) ;
		indentDown();
    }

    public void visit (BlockStatementNode n)
    {
		dots();
		println("BlockStatementNode");

		indentUp();
        n.stmts.accept(this);
		indentDown();
    }
    
    public void visit (StatementsNode n)
    {
        if(n.stmts != null)
        {
			dots();
			println("StatementsNode");

			indentUp();
            n.assign.accept(this);
			indentDown();
            n.stmts.accept(this);
        }
    }

    public void visit (AssignmentNode n)
    {
		dots();
		println("AssignmentNode");

		indentUp();
		dots();
        n.left.accept(this);
		indentDown();

		dots();
		println("op: =");

		indentUp();
		dots();
        n.right.accept(this);
		indentDown();
    }

    public void visit (ExpressionNode n)
    {
		println("ExpressionNode");

		indentUp();
		if(n.fact != null)
		{
			dots();
			n.fact.accept(this);
		}

		if(n.expr != null)
		{
			dots();
			n.expr.accept(this);
		}
        
        if(n.bin != null)
        {
			dots();
            n.bin.accept(this);
        }
		indentDown();
    }

    public void visit (BinaryNode n) 
    {
		if(n.left != null && n.op != null)
		{
			println("BinaryNode");

			indentUp();
	
			dots();
			println("op: " + n.op);
	
			dots();
			n.left.accept(this) ;
	
			if(n.right != null)
			{
				dots();
				n.right.accept(this);	
			}
	
			indentDown();
		}
    }

    public void visit (UnaryNode n) 
    {
		println("UnaryNode");

		indentUp();
		dots();
        n.fact.accept(this);
		indentDown();
    }
    
    public void visit (FactorNode n)
    {
		if(n.unary != null || n.id != null || n.lit != null || n.expr != null)
		{
			println("FactorNode");

			indentUp();
			if(n.unary != null)
			{
				dots();
				n.unary.accept(this);
			}
			if(n.id != null)
			{
				dots();
				n.id.accept(this);
			}
			if(n.lit != null)
			{
				dots();
				n.lit.accept(this);
			}
			if(n.expr != null)
			{
				dots();
				n.expr.accept(this);
			}
			indentDown();
		}
    }

    //this is a variable
    public void visit (LiteralNode n) 
    {
        n.printNode();
    }

    //this is a terminal symbol number or string
    public void visit (IdentifierNode n) 
    {
        n.printNode();
    }

    public void visit(Node n)
    {
        //Do Nothing
    }
}
