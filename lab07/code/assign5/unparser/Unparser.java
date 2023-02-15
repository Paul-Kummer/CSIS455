package assign5.unparser ;

import assign5.visitor.* ;
import assign5.parser.* ;
import java.io.* ;

public class Unparser extends ASTVisitor 
{
	public Parser parser = null;
	int indent = 0;

	public Unparser()
	{
		visit(this.parser.cu);
	}

	public Unparser(Parser parser)
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

    public void visit(AssignmentNode n)
    {
        n.left.accept(this);

        printSpace();

		print(n.op.toString());

        printSpace();

        n.right.accept(this);
    }

    public void visit (ExpressionNode n) 
    {
        if(n.parenthesis)
        {
            print("(");
        }

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
        if(n.parenthesis)
        {
            print(")");
        }
    }

    public void visit (BinaryNode n) 
    {
        if(n.left != null)
        {
            n.left.accept(this);

            if(n.right != null)
            {
                print(" " + n.op + " ");
    
                n.right.accept(this);	
            } 
        }
        else //check for end of expression symbol ')'
        {
            if(n.op != null)
            {
                print(n.op.toString());
            }
        }
    }

    //Unary: child of term, always negative otherwise it is an identifier node
    public void visit (UnaryNode n)
    {
        n.fact.accept(this);
    }

    public void visit (FactorNode n)
    {
		if(n.unary != null || n.id != null || n.lit != null || n.expr != null)
		{
			if(n.unary != null)
			{
                print("-");
				n.unary.accept(this);
			}
			if(n.id != null)
			{
				n.id.accept(this);
			}
			if(n.lit != null)
			{
				n.lit.accept(this);
			}
			if(n.expr != null)
			{
				n.expr.accept(this);
			}
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
