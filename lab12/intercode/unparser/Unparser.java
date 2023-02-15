package intercode.unparser ;

import intercode.visitor.* ;
import intercode.ast.*;
import intercode.parser.* ;
import intercode.typechecker.*;
import intercode.inter.*;

import java.io.* ;
import java.util.* ;

import javax.lang.model.type.ArrayType;

public class Unparser extends ASTVisitor 
{
	public InterCode intercode = null;
	public TypeChecker typeChecker = null;
	public Parser parser = null;
	int indent = 0;
	public String bigString = "";
	private FileWriter output;
	private LabelNode parentLoopLabel;
	private LabelNode currentLoopLabel;


	public Unparser(Boolean x) //Bool value is just to select the default constructor
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

	public Unparser(InterCode intercode) throws IOException, FileNotFoundException
	{
		this.intercode = intercode;
		visit(this.intercode.cu);

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
		String s = "\t";
		print(s);
	}

	//Compilation Unit: start of program
	public void visit (CompilationUnit n) 
	{
		n.block.accept(this);
	}

	//Block Statement: child of compilation unit
	public void visit (BlockStatementNode n) 
	{
		if(n.headDecl != null)
		{
			n.headDecl.accept(this);
		}
		else
		{
			n.decls.accept(this);
		}

		if(n.headStmt != null)
		{
			n.headStmt.accept(this);
		}
		else
		{
			n.stmts.accept(this);
		}
	}

	public void visit (DeclarationsNode n)
	{
		//UNUSED
	}

	public void visit (DeclarationNode n)
	{
		DeclarationNode temp = n;
		while(temp != null)
		{
			if(temp.assign != null)
			{
				temp.assign.accept(this);
			}
			temp = temp.nextDecl;
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
		StatementNode temp = n;
		while(temp != null)
		{
			if(temp.node != null)
			{
				temp.node.accept(this);
			}
			temp = temp.nextStmt;
		}
	}

	public void visit(ForNode n)
	{

	}

	public void visit(IfNode n)
	{
		//println("Im in If");
		//boolean stmtIsBlock = (n.stmt instanceof BlockStatementNode);
		//boolean isElse = (n.elseStmt instanceof BlockStatementNode);
		//IdentifierNode temp = TempNode.newTemp();
		//ParenthesesNode cond = n.cond;
		//ExpressionNode expr = null;
		//AssignmentNode assign = null;

		if(n.cond != null)
		{
			if(n.cond != null)
			{
				n.assigns.head.accept(this);
				
				printIndent();
				print("ifFalse ");
				n.cond.accept(this);
				println(" goto " + n.elifLabel.w);
			}

			//println("before stmt");
			// When the condition is true, this will run
			n.stmt.accept(this); //this is any statement
			//println("after stmt");

			printIndent();
			println("goto " + n.falseLabel.w); // the end of the if statement
			

			// Else Statement
			if(n.elseStmt != null && !(n.elseStmt instanceof IfNode))
			{
				println(n.elifLabel.w + ":");

				n.elseStmt.accept(this);

				println(n.falseLabel.w + ":");
			}
			// If or Else If Statement
			else if(n.elseStmt !=  null)
			{

				println(n.elifLabel.w + ":");
				n.elseStmt.accept(this);
			}
			else
			{
				println(n.falseLabel.w + ":");
			}
		}
	}


	public void visit(WhileNode n)
	{
		parentLoopLabel = currentLoopLabel;
		currentLoopLabel = n.falseLabel;

		println(n.trueLabel.w + ":");

		n.assigns.head.accept(this);

		printIndent();
		print("ifFalse ");
		
		n.cond.accept(this);

		println(" goto " + n.falseLabel.w);

		n.stmt.accept(this);
		
		printIndent();
		println("goto " + n.trueLabel.w);

		println(n.falseLabel.w + ":");
		
		currentLoopLabel = parentLoopLabel;
	}

	public void visit(DoNode n)
	{
		parentLoopLabel = currentLoopLabel;
		currentLoopLabel = n.falseLabel;

		println(n.trueLabel.w + ":");

		n.stmt.accept(this); // run the block once no matter what

		n.assigns.head.accept(this); // determine if the condition is true

		printIndent();
		print("ifFalse ");
		n.cond.accept(this);
		println(" goto " + n.falseLabel.w);

		printIndent();
		println("goto " + n.trueLabel.w);
		
		println(n.falseLabel.w + ":");

		currentLoopLabel = parentLoopLabel;
	}

	public void visit (ParenthesesNode n) // Turn this into a temp node just like the binary expressions
	{
		if(n.expr != null)
		{
			n.expr.accept(this);
		}
		/*if(n.expr instanceof BinaryNode)
		{
			BinaryNode expr = (BinaryNode)n.expr;
			expr.assigns.head.accept(this);
			expr.accept(this);
		}
		else
		{
			n.expr.accept(this);
		}*/
	}

	public void visit(AssignmentNode n)
	{
		n.assigns.head.accept(this);
		if(n.right instanceof BinaryNode)
		{
			((BinaryNode)n.right).assigns.head.accept(this);    
		}
		else if(n.right instanceof ParenthesesNode)
		{
			((ParenthesesNode)n.right).assigns.head.accept(this); 
		}
		
		printIndent();
		if(n.right != null && !(n.right instanceof ParenthesesNode))
		{
			n.left.accept(this); // Should be an IdentifierNode
			
			print(" " + n.op.toString() + " ");
			
			n.right.accept(this);	
		}
		else if(n.right != null && n.right instanceof ParenthesesNode)
		{
			n.right.accept(this);
		}
		else
		{
			n.left.accept(this); 
		}
		println("");
	}

	public void visit (BinaryNode n) 
	{
		//each binary node has assigns. linked list of statements with nodes of assigns(id, binary)
		if(n.left != null && !(n.left instanceof BinaryNode))
		{
			if(n.left instanceof ParenthesesNode)
			{
				((ParenthesesNode)n.left).assigns.head.accept(this);
			}

			n.left.accept(this);
		}

		if(n.right != null && !(n.right instanceof BinaryNode))
		{
			print(" " + n.op + " ");

			if(n.right instanceof ParenthesesNode)
			{
				((ParenthesesNode)n.right).assigns.head.accept(this);
			}

			n.right.accept(this);	
		}
	}

	public void visit (ExpressionNode n) //Is this even used???
	{
		println("Expression Node:");
		if(n.right != null && n.left != null)
		{
			println("I am an expressionNode");
			print(" " + n.op.toString() + " ");

			if(n.left instanceof BinaryNode)
			{
				((BinaryNode)n.left).accept(this);
			}
			else if(n.left instanceof IdentifierNode)
			{
				((IdentifierNode)n.left).accept(this);;
			}

			if(n.right instanceof BinaryNode)
			{
				((BinaryNode)n.right).accept(this);
			}
			else if(n.right instanceof ParenthesesNode)
			{
				((ParenthesesNode)n.right).accept(this);
			}
			else if(n.right instanceof IdentifierNode)
			{
				((IdentifierNode)n.right).accept(this);
			}
		}
		else
		{
			if(n.left instanceof BinaryNode)
			{
				((BinaryNode)n.left).accept(this);
			}
			else if(n.left != null && n.left instanceof IdentifierNode)
			{
				((IdentifierNode)n.left).accept(this);
			}
		}
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
  
	public void visit (NumNode n) 
	{
		print(n.toString());
	}

	public void visit (RealNode n) 
	{
		print(n.toString());
	}

	public void visit (TrueNode n)
	{
		print("" + n.value);
	}

	public void visit (FalseNode n)
	{
		print("" + n.value);
	}

	public void visit (BreakStmtNode n)
	{
		printIndent();
		println("goto " + currentLoopLabel.w); 
	}

	public void visit (IdentifierNode n) 
	{	
		if(n.typeNode != null && n.typeNode.array != null)
		{
			print(n.typeNode.toString());
		}
		else
		{
			print(n.lexeme);
		}
	}
}
