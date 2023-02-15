package assign6.unparser ;

import assign6.visitor.* ;
import assign6.ast.*;
import assign6.parser.* ;
import assign6.typechecker.*;

import java.io.* ;
import java.util.* ;

public class Unparser extends ASTVisitor 
{
    public TypeChecker typeChecker = null;
	public Parser parser = null;
	int indent = 0;
    public String bigString = "";
    private FileWriter output;


	public Unparser(Boolean x)
	{

	}

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

        n.decls = n.headDecl;
		while(n.decls != null)
        {
            indentUp();
			n.decls.accept(this);
			n.decls = n.decls.nextDecl;
            indentDown();
		}

		n.stmts = n.headStmt;
		while(n.stmts != null)
		{
            indentUp();
			n.stmts.accept(this);
			n.stmts = n.stmts.nextStmt;
            indentDown();
		}
    
		printIndent();
		print("}");
    }

    public void visit (DeclarationsNode n)
    {
        //UNUSED
    }

	public void visit (DeclarationNode n)
    {
		if(n.typeNode != null)
		{
            printIndent();
			n.typeNode.accept(this); //Change typeNode to no display type

            if(n.id != null)
            {
                n.id.accept(this);
                println(";");
            }            
		}

        //if(n.nextDecl != null)
		//{
		//	n.nextDecl.accept(this);
		//}
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
        if(n.size != null)
        {
            print(n.toString());
            //print("" + n.size);
        }
    }

    public void visit (StatementsNode n)
    {
        //UNUSED
    }

    public void visit (StatementNode n)
    { 
        if(n.node != null)
        {
            printIndent();
            n.node.accept(this);
        }

        //if(n.nextStmt != null)
		//{
		//	n.nextStmt.accept(this);
		//}
    }

    public void visit (ParenthesesNode n)
    {
        print("(");
        n.expr.accept(this);
        print(")");
    }

    public void visit(IfNode n)
    {
        boolean stmtIsBlock = (n.stmt instanceof BlockStatementNode);
        boolean isElse = (n.elseStmt instanceof BlockStatementNode);

        if(n.cond != null)
        {
            //Else If Statement
            if(n.isElif)
            {
                //println("");
                printIndent();
                print("else if ");
            }
            //If Statement
            else
            {
                println("");
                printIndent();
                print("if ");
            }

            if(n.cond != null)
            {
                n.cond.accept(this);
            }

            // if (cond)
            // {
            //     stmt;
            // }
            if(stmtIsBlock)
            {
                println("");
                n.stmt.accept(this);
                println("");
            }
            // if (cond) stmt;
            else if(n.stmt != null)
            {
                printSpace();
                n.stmt.accept(this);
            }
            // if (cond) ;
            else
            {
                println(";");
            }


            // Else Statement
            if(n.elseStmt != null && !(n.elseStmt instanceof IfNode))
            {
                //String val = stmtIsBlock? " Yes": " No";
                //print("I'm Here" + val);
                printIndent();
                print("else ");
                
                // else
                // {
                //     stmt;
                // }
                if(isElse)
                {
                    println("");
                    n.elseStmt.accept(this);
                    println("");
                }
                // else stmt;
                else
                {
                    n.elseStmt.accept(this);
                }  
            }
            // If or Else If Statement
            else if(n.elseStmt !=  null)
            {
                n.elseStmt.accept(this);
            }
            // No More Statements
            else
            {
                //Done
            }
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
            printIndent();
        }

        n.stmt.accept(this);

        if(n.stmt instanceof BlockStatementNode)
        {
            println("");
        }
    }

    public void visit(DoNode n)
    {
        boolean stmtIsBlock = (n.stmt instanceof BlockStatementNode);
        println("");
        printIndent();

        if(!stmtIsBlock)
        {
            print("do ");
        }
        else
        {
            println("do ");
        }

        n.stmt.accept(this);

        if(!stmtIsBlock)
        {
            printIndent();
            print("while");
        }
        else
        {
            print(" while");
        }

        n.cond.accept(this);

        println(";");
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

            print(" " + n.op.toString());

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
		println("" + n.value + ";");
    }

    public void visit (IdentifierNode n) 
    {
        print(n.lexeme); 
    }
}
