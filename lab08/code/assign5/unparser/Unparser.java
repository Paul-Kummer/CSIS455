package assign5.unparser ;

import assign5.visitor.* ;
import assign5.ast.*;
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
        if(n.decls != null)
        {
            n.decls.accept(this);
        }
        else if(n.stmts != null)
        {
            n.stmts.accept(this);
        }
		indentDown();
        
		printIndent();
		println("}");
    }

    public void visit (DeclarationsNode n)
    {
        if(n.decls != null)
        {
            n.decl.accept(this);

			n.decls.accept(this);  
        }
        if(n.stmts != null)
        {
            n.stmts.accept(this);
        }
    }

	public void visit (DeclarationNode n)
    {
		printIndent();
		if(n.type != null)
		{	
			n.type.accept(this);

            printSpace();

			if(n.assign != null) //assignment after declaration
			{
				n.assign.accept(this);
			}
            else
            {
                n.id.accept(this);
            }
		}

        print(";\n");
    }

    public void visit (StatementsNode n)
    {
        if(n.stmts != null)
        {
            n.stmt.accept(this);

            n.stmts.accept(this);
        }
        if(n.decls != null)
        {
            n.decls.accept(this);
        }
    }

    public void visit (StatementNode n)
    {
        printIndent();
		if(n.node != null)
		{	
			n.node.accept(this);
		}
		else
		{
			n.assign.accept(this);
		}

        print(";\n");
    }

    public void visit(AssignmentNode n)
    {
        n.left.accept(this);

        printSpace();

		print(n.op.toString());
        //print(" = ");

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
        print("-");
        n.fact.accept(this);
    }

    public void visit (FactorNode n)
    {
		if(	n.unary	!= null || 
			n.id	!= null || 
			n.num	!= null || 
			n.real	!= null ||
            n.array != null ||
			n.block	!= null || 
			n.expr	!= null		)
		{
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
            if(n.array != null)
			{
				n.array.accept(this);
			}
			if(n.block != null)
			{
				n.block.accept(this);
			}
			if(n.expr != null)
			{
				n.expr.accept(this);
			}
		}
    }

    public void visit (TypeNode n)
    {
        print(n.basic.toString());

        printSpace();

		if(n.array != null)
		{
			n.array.accept(this);
		}
	}

    public void visit (ArrayTypeNode n)
    {
        print("[");
        if(n.size > 1)
        {
            print("" + n.size);
        }
        print("]");

        if(n.type != null)
        {
            n.type.accept(this);
        }
    }

    public void visit (NumNode n) 
    {
        print("" + n.value);
    }

    public void visit (RealNode n) 
    {
        print("" + n.value);
    }

    public void visit (IdentifierNode n) 
    {
        print(n.id); 
    }
}
