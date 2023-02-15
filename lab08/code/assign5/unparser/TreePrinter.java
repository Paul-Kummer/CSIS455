package assign5.unparser ;

import assign5.visitor.* ;
import assign5.ast.*;
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
		if(n.stmts != null)
		{
			n.stmts.accept(this);
		}
		if(n.decls != null)
        {
			n.decls.accept(this);
		}
		indentDown();
    }

	public void visit (DeclarationsNode n)
    {
        if(n.decls != null)
        {
			dots();
			println("Declarations");

			indentUp();
            n.decl.accept(this);
			indentDown();

			n.decls.accept(this);  
        }
		if(n.stmts != null)
		{
			n.stmts.accept(this);
		}
    }

	public void visit (DeclarationNode n)
    {
		dots();
		println("DeclarationNode");

		indentUp();
		if(n.type != null)
		{	
			n.type.accept(this);
			n.id.accept(this);

			if(n.assign != null) //assignment after declaration
			{
				indentUp();
				n.assign.accept(this);
				indentDown();
			}
		}
		indentDown();
    }
    
    public void visit (StatementsNode n)
    {
        if(n.stmts != null)
        {
			dots();
			println("Statements");

			indentUp();
            n.stmt.accept(this);
			indentDown();

            n.stmts.accept(this);
        }
		if(n.decls != null)
		{
			indentUp();
			n.decls.accept(this);
			indentDown();
		}
    }

	public void visit (StatementNode n)
    {
		dots();
		println("StatementNode");

		indentUp();
		if(n.node != null)
		{	
			n.node.accept(this);
		}
		else
		{
			n.assign.accept(this);
		}
		indentDown();
    }

    public void visit (AssignmentNode n)
    {
		dots();
		println("AssignmentNode");

		indentUp();
        n.left.accept(this);
		indentDown();

		dots();
		println("op: =");

		indentUp();
        n.right.accept(this);
		indentDown();
    }

    public void visit (ExpressionNode n)
    {
		dots();
		println("ExpressionNode");

		indentUp();
		if(n.fact != null)
		{
			n.fact.accept(this);
		}

		if(n.expr != null)
		{
			n.expr.accept(this);
		}
        
        if(n.bin != null)
        {
            n.bin.accept(this);
        }
		indentDown();
    }

    public void visit (BinaryNode n) 
    {
		if(n.left != null && n.op != null)
		{
			dots();
			println("BinaryNode");

			indentUp();
	
			dots();
			println("op: " + n.op);
	
			n.left.accept(this) ;
	
			if(n.right != null)
			{
				n.right.accept(this);	
			}
	
			indentDown();
		}
    }

    public void visit (UnaryNode n) 
    {
		dots();
		println("UnaryNode");

		indentUp();
        n.fact.accept(this);
		indentDown();
    }
    
    public void visit (FactorNode n)
    {
		if(	n.unary	!= null || 
			n.id	!= null || 
			n.num	!= null || 
			n.real	!= null ||
			n.block	!= null || 
			n.expr	!= null		)
		{
			dots();
			println("FactorNode");

			indentUp();
			if(n.unary != null)
			{
				n.unary.accept(this);
			}
			if(n.id != null)
			{
				n.id.accept(this);
			}
			if(n.num != null)
			{
				n.num.accept(this);
			}
			if(n.real != null)
			{
				n.real.accept(this);
			}
			if(n.block != null)
			{
				n.block.accept(this);
			}
			if(n.expr != null)
			{
				n.expr.accept(this);
			}
			indentDown();
		}
    }

	public void visit (TypeNode n)
    {
		dots();
		println("TypeNode: " + n.basic.lexeme);

		indentUp();
		if(n.array != null)
		{
			n.array.accept(this);
		}
		indentDown();
	}

	public void visit (ArrayTypeNode n)
    {
		dots();
		println("ArrayTypeNode: size[ " + n.size + " ]");

		indentUp();
		if(n.type != null)
		{
			n.type.accept(this);
		}
		indentDown();
	}

    public void visit (NumNode n) 
    {
		dots();
        n.printNode();
    }

	public void visit (RealNode n) 
    {
		dots();
        n.printNode();
    }

    public void visit (IdentifierNode n) 
    {
		dots();
        n.printNode();
    }

    public void visit(Node n)
    {
		dots();
		println("Node");
    }
}
