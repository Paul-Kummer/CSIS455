package assign5.unparser ;

import assign5.visitor.* ;
import assign5.ast.*;
import assign5.parser.* ;
import java.io.* ;
import java.util.* ;

public class Unparser extends ASTVisitor 
{
	public Parser parser = null;
	int indent = 0;
    public String bigString = "";
    private FileWriter output;

	public Unparser()
	{
		visit(this.parser.cu);
	}

	public Unparser(Parser parser) throws IOException, FileNotFoundException
	{
		this.parser = parser;
		visit(this.parser.cu);

        output = new FileWriter("output.txt");
        output.write(bigString);
        output.close();
        //System.out.println("\n" + bigString);
	}

	void print(String s)
	{
		System.out.print(s);
        bigString += s;
	}

	void println(String s)
	{
		System.out.println(s);
        bigString += (s + "\n");
	}

	void printSpace()
	{
		System.out.print(" ");
        bigString += " ";
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
		print("}");
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
                println(";"); 
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
        if(n.size != null)
        {
            print("" + n.size);
        }
        print("]");

        if(n.typeNode != null)
        {
            n.typeNode.accept(this);
        }
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
    }

    public void visit(AssignmentNode n)
    {
        if(n.right != null && n.op != null)
        {
            n.left.accept(this);

            print(" " + n.op.toString() + " ");

            printSpace();
    
            n.right.accept(this);
            println("; ");
        }
        else if(n.right != null)
        {
            n.left.accept(this);
            printSpace();
            n.right.accept(this);
            println("; ");
        }
        else
        {
            n.left.accept(this);
        }
    }

    public void visit(DoNode n)
    {
        println("");
        printIndent();
        println("do ");

        n.block.accept(this);

        n.whileNode.accept(this);

        println(";\n");
    }

    public void visit(WhileNode n)
    {
        print("while ");

        print("(");
        n.bool.accept(this);
        print(" )");

        if(!n.isDo)
        {
            println("");
            n.block.accept(this);
            println("\n");
        }
    }

    public void visit(ForNode n)
    {
        println("for (");

        
        indentUp();
        if(n.decl != null)
        {
            printIndent();
            indentDown();
            n.decl.accept(this);
            indentUp();
        }
        if(n.bool != null)
        {
            indentUp();
            printIndent();
            n.bool.accept(this);
            indentDown();
        }
        println("; ");
        if(n.stmt != null)
        {
            printIndent();
            indentDown();
            n.stmt.accept(this);
            indentUp();
        }
        printIndent();
        println(") ");
        indentDown();

        if(n.block != null)
        {
            n.block.accept(this);
        }
        println("");
    }

    public void visit(IfNode n)
    {
        if(n.bool != null)
        {  
            if(n.isElseIf)
            {
                printIndent();
                print("else ");
            }

            print("if ");
    
            print("(");
            n.bool.accept(this);
            print(") ");
            println("");
        }
        else
        {
            printIndent();
            print("else ");
            println("");
        }

        if(n.block != null)
        {
            n.block.accept(this);
            println("");
        }
        if(n.newIf != null)
        {
            n.newIf.accept(this);
        }
        else
        {
            println("");
        }
    }

    public void visit (BoolNode n) 
    {
        if(n.parenthesis)
        {
            print("( ");
        }
        if(n.right != null)
        {
            n.left.accept(this);
            print(" " + n.op.toString() + " ");
            n.right.accept(this);
        }
        else
        {
            n.left.accept(this);
        }
        if(n.parenthesis)
        {
            print(" )");
        }
    }

    public void visit (JoinNode n) 
    {
        if(n.right != null)
        {
            n.left.accept(this);
            print(" " + n.op.toString() + " ");
            n.right.accept(this);
        }
        else
        {
            n.left.accept(this);
        }
    }

    public void visit (EqualityNode n) 
    {
        if(n.right != null)
        {
            n.left.accept(this);
            print(" " + n.op.toString() + " ");
            n.right.accept(this);
        }
        else
        {
            n.left.accept(this);
        }
    }

    public void visit (RelationNode n) 
    {
        if(n.right != null)
        {
            n.left.accept(this);
            print(" " + n.op.toString() + " ");
            n.right.accept(this);
        }
        else
        {
            n.left.accept(this);
        }
    }

    public void visit (ExpressionNode n) 
    {
        if(n.right != null)
        {
            n.left.accept(this);
            print(" " + n.op.toString() + " ");
            n.right.accept(this);
        }
        else
        {
            n.left.accept(this);
        }
    }

    public void visit (TermNode n) 
    {
        if(n.right != null)
        {
            n.left.accept(this);
            print(" " + n.op.toString() + " ");
            n.right.accept(this);
        }
        else
        {
            n.left.accept(this);
        }
    }

    public void visit (UnaryNode n) 
    {
        if(n.unary != null)
        {
            print("" + n.op);
            n.unary.accept(this);
        }
        if(n.fact != null)
        {
            n.fact.accept(this);
        }
    }

    //This node is unused
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

    public void visit (FactorNode n)
    {
		if(	n.trueFalse != null || 
			n.assign    != null || 
			n.num   	!= null || 
			n.real  	!= null ||
            n.bool      != null )
		{
			if(n.trueFalse != null)
			{
				n.trueFalse.accept(this);
			}
			if(n.assign != null)
			{
				n.assign.accept(this);
			}
			if(n.num != null)
			{
				n.num.accept(this);
			}
			if(n.real != null)
			{
				n.real.accept(this);
			}
            if(n.bool != null)
			{
				n.bool.accept(this);
			}
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

    public void visit (TrueFalseNode n) 
    {
        print("" + n.value);
    }

    public void visit (IdentifierNode n) 
    {
        print(n.lexeme); 
    }
}
