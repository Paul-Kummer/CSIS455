package assign5.unparser ;

import assign5.visitor.* ;
import assign5.ast.*;
import assign5.parser.* ;
import assign5.typechecker.*;

import java.io.* ;
import java.util.* ;

public class Unparser extends ASTVisitor 
{
    public TypeChecker typeChecker = null;
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
	}

    public Unparser(TypeChecker typeChecker) throws IOException, FileNotFoundException
	{
		this.typeChecker = typeChecker;
		visit(this.typeChecker.cu);

        output = new FileWriter("output.txt");
        output.write(bigString);
        output.close();
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

        if(n.decls != null)
        {
            indentUp();
            n.decls.accept(this);
            indentDown();
        }
        if(n.stmts != null)
        {
            indentUp();
            n.stmts.accept(this);
            indentDown();
        }
    
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
		if(n.typeNode != null)
		{	
			n.typeNode.accept(this);

            printSpace();

			if(n.id != null) //assignment after declaration
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
            println(n.type.toString());
        }
    }

    public void visit (StatementsNode n)
    {
        //indentUp();
        if(n.stmts != null)
        {
            printIndent();
            n.stmt.accept(this);
            n.stmts.accept(this);
        }
        if(n.decls != null)
        {
            //printIndent();
            n.decls.accept(this);
        }
        //indentDown();
    }

    public void visit (StatementNode n)
    {
        printIndent();
    }

    public void visit (ParenthesesNode n)
    {
        print("(");
        n.expr.accept(this);
        print(")");
    }

    public void visit(IfNode n)
    { 
        //When this is true, it is only an else statement
        if(!(n.elseStmt instanceof BlockStatementNode))
        {
            println("");
            printIndent();
        }

        print("if ");

        n.cond.accept(this);
    
        
        println("");
        n.stmt.accept(this);

        if(n.elseStmt != null)
        {
            println("");
            printIndent();
            print("else ");

            //When this is true, it is only an else statement
            if(n.elseStmt instanceof BlockStatementNode)
            {
                println("");
            }
    
            n.elseStmt.accept(this);
    
            println("");
        }
        else
        {
            println("");
        }
    }

    public void visit(WhileNode n)
    {
        println("");
        printIndent();
        print("while ");

        n.cond.accept(this);
        println("");

        if(!(n.stmt instanceof BlockStatementNode))
        {
            //indentUp();
        }

        n.stmt.accept(this);

        if(!(n.stmt instanceof BlockStatementNode))
        {
            //indentDown();
        }
        println("\n");
    }

    public void visit(DoNode n)
    {
        println("");
        printIndent();
        println("do ");

        if(!(n.stmt instanceof BlockStatementNode))
        {
            //indentUp();
        }

        n.stmt.accept(this);

        if(!(n.stmt instanceof BlockStatementNode))
        {
            //indentDown();
        }

        print(" while");
        n.cond.accept(this);

        println(";");
        println("");
    }

    public void visit(ArrayAccessNode n)
    {
        n.id.accept(this);
        n.index.accept(this);
    }

    public void visit(ArrayDimsNode n)
    {
        print("[");
        n.size.accept(this);
        print("]");

        if(n.dim != null)
        {
            n.dim.accept(this);
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

    public void visit(ForNode n)
    {

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

  
    public void visit (NumNode n) 
    {
        print("" + n.value);
    }

    public void visit (RealNode n) 
    {
        print("" + n.value);
    }

	public void visit (TrueNode n) // value is Word.True
    {
        print("" + n.value);
    }

    public void visit (FalseNode n) // value is Word.False
    {
        print("" + n.value);
    }

	public void visit (BreakStmtNode n)
    {
		print("" + n.value);
    }

    public void visit (IdentifierNode n) 
    {
        print(n.lexeme); 
    }
}
