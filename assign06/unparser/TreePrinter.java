package assign6.unparser ;

import assign6.visitor.* ;
import assign6.ast.*;
import assign6.parser.* ;

//Timestamp: 47:15

//Timestamp: 52:34
public class TreePrinter extends ASTVisitor
{
	public Parser parser = null;

	int level = 0;
	String indent = "....";
	int indentLevel = 0;

	public TreePrinter ()
	{

	}

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

		n.decls = n.headDecl;
		while(n.decls != null)
        {
			n.decls.accept(this);
			n.decls = n.decls.nextDecl;
		}

		n.stmts = n.headStmt;
		while(n.stmts != null)
		{
			n.stmts.accept(this);
			n.stmts = n.stmts.nextStmt;
		}

		indentDown();
    }

	public void visit (DeclarationsNode n)
    {
		//UNUSED
    }

	public void visit (DeclarationNode n)
    {
		dots();
		println("DeclarationNode");

		indentUp();
		if(n.typeNode != null)
		{	
			n.typeNode.accept(this);
			n.id.accept(this);

			if(n.assign != null) //assignment after declaration
			{
				indentUp();
				n.assign.accept(this);
				indentDown();
			}
		}
		indentDown();

		//if(n.nextDecl != null)
		//{
		//	n.nextDecl.accept(this);
		//}
    }
    
    public void visit (StatementsNode n)
    {
		//UNUSED
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
		indentDown();

		//if(n.nextStmt != null)
		//{
		//	n.nextStmt.accept(this);
		//}
    }

	public void visit (ParenthesesNode n)
    {
		dots();
		println("ParenthesesNode");

		indentUp();
		if(n.expr != null)
		{	
			n.expr.accept(this);
		}
		indentDown();
    }

	public void visit (IfNode n)
    {
		dots();
		println("IfNode");

		indentUp();
		if(n.cond != null)
		{
			n.cond.accept(this);
		}
		if(n.stmt != null)
		{
			n.stmt.accept(this);
		}
		indentDown();

		if(n.elseStmt != null)
		{
			dots();
			println("Else Clause");

			indentUp();
			n.elseStmt.accept(this);
			indentDown();
		}
    }

	public void visit (WhileNode n)
    {
		dots();
		println("WhileNode");

		indentUp();
		n.cond.accept(this);
		n.stmt.accept(this);
		indentDown();
    }

	public void visit (DoNode n)
    {
		dots();
		println("DoNode");

		indentUp();
		n.stmt.accept(this);
		n.cond.accept(this);
		indentDown();
    }

	public void visit (ArrayAccessNode n)
    {
		dots();
		println("ArrayAccessNode");

		indentUp();
		n.id.accept(this);
		n.index.accept(this);
		indentDown();
    }

	public void visit (ArrayDimsNode n)
    {
		dots();
		println("ArrayDimsNode");

		indentUp();
		n.size.accept(this);
		
		if (n.dim != null)
		{
			n.dim.accept(this);
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

		indentUp();
		dots();
		println("op: =");
		indentDown();

		indentUp();
		if (n.right instanceof IdentifierNode)
		{
			((IdentifierNode)n.right).accept(this);
		}			
		else if (n.right instanceof NumNode)
		{
			((NumNode)n.right).accept(this);
		}			
		else if (n.right instanceof RealNode)
		{
			((RealNode)n.right).accept(this);
		}
		else if (n.right instanceof TrueNode)
		{
			((TrueNode)n.right).accept(this);
		}
		else if (n.right instanceof FalseNode)
		{
			((FalseNode)n.right).accept(this);
		}
		else if (n.right instanceof ArrayAccessNode)
		{
			((ArrayAccessNode)n.right).accept(this);
		}	
		else if (n.right instanceof ParenthesesNode)
		{
			((ParenthesesNode)n.right).accept(this);
		}		
		else
		{
			((BinaryNode)n.right).accept(this);
		}   
		indentDown();
    }

	public void visit (BinaryNode n) 
    {
		if(n.left != null )
		{
			dots();
			println("BinaryNode");

			indentUp();
	
			if (n.left != null)
			{
				n.left.accept(this);
			}
	
			if(n.right != null)
			{
				dots();
				println("op: " + n.op);

				n.right.accept(this);
			}
	
			indentDown();
		}
    }

    public void visit (ExpressionNode n)
    {
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
		println("ArrayTypeNode: " + n.toString());
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

	public void visit (TrueNode n) // value is Word.True
    {
        dots();
        n.printNode();
    }


    public void visit (FalseNode n) // value is Word.False
    {
        dots();
        n.printNode();
    }

	public void visit (BreakStmtNode n)
    {
		dots();
		println("BreakStmtNode");
    }

    public void visit(Node n)
    {
		dots();
		println("Node");
    }
}
