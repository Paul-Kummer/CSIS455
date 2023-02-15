package assign4.pretty ;

import assign4.visitor.* ;
import assign4.parser.* ;
import java.io.* ;

import javax.lang.model.util.ElementScanner6;

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
			s += "    ";
		}

		print(s);
	}

	//////////// New Visit methods //////////////

    //Compilation Unit: start of program
    public void visit (CompilationUnit n) 
    {
        n.block.accept(this);
    }

    //Block Statement: child of compilation unit
    public void visit (BlockStatementNode n) 
    {
		printIndent();
		println("{");

		indentUp();

        n.stmts.accept(this);

		indentDown();
        
		printIndent();
		println("}");
    }

    public void visit (StatementsNode n)
    {
        if(n.stmts != null)
        {
            printIndent();
            n.assign.accept(this);
            print(";\n");

            //move to next statment
            n.stmts.accept(this);
        }
    }

    //UNUSED: Statement: child of block
    public void visit (StatementNode n) 
    {
        n.assign.accept(this);

		print(";\n");
    }

    //Assignment: child of expression
    public void visit(AssignmentNode n)
    {
        n.id.accept(this);

        printSpace();

		print(n.op.toString());

        printSpace();

        n.right.accept(this);
    }

    //Expression: child of assignment
    public void visit (ExpressionNode n) 
    {
        //If the next token isn't the end of the statement, it is binary
        if(n.bin != null)
        {
            n.bin.accept(this);  
        }
		else
		{
			n.term.accept(this);
		}
    }

    //Binary: child of expression
    public void visit (BinaryNode n) 
    {
        n.left.accept(this);

		printSpace();

		print(n.op.toString());

		printSpace();
        
        //second operand
        n.right.accept(this);       
    }

    //Unary: child of term, always negative otherwise it is an identifier node
    public void visit (UnaryNode n)
    {
        n.term.accept(this);
    }

    //Term: child of expression, binary
    public void visit (TermNode n) 
    {
        if(n.unary != null)
        {
			print("-");
            n.unary.accept(this);
        }
        else if(n.id != null)
        {
            n.id.accept(this); 
        }
        else if(n.lit != null)
        {
            n.lit.accept(this);  
        }
        else
        {
            println("An Error Occured");
        }
    }

    //this will assign an identifier a string
    public void visit (LiteralNode n) 
    {
        print(String.valueOf(n.literal));
    }

    public void visit (IdentifierNode n) 
    {
        print(n.id);
    }
}
