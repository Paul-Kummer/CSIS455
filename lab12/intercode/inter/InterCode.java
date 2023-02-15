package intercode.inter;

import intercode.Env;
import intercode.visitor.*;
import intercode.lexer.*;
import intercode.ast.*;
import intercode.parser.*;
import intercode.typechecker.*;

public class InterCode extends ASTVisitor
{
	public TypeChecker typeChecker = null;
	public CompilationUnit cu = null;
	public Env top = null;
    public BlockStatementNode enclosingBlock = null;    // Keeps track of what block statement the program is in

	int indent = 0; 

	public InterCode()
	{
		if(this.cu != null)
		{
			visit(cu) ;
		}
	}

	public InterCode(TypeChecker typeChecker)
	{
		this.typeChecker = typeChecker;
		this.cu = typeChecker.cu;
		visit(this.cu);
	}

	///////////////////////////////////////////////////
	//              Utility Methods                  //
	///////////////////////////////////////////////////

	void error (String s)
	{
		if(typeChecker.parser.lexer != null)
		{
			println("near line ( " + typeChecker.parser.lexer.line + " ): " + s);
		}
		else
		{
			println(s);
		}
		
		exit(1);
	}


	void exit (int n)
	{
		System.exit(1);
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


	///////////////////////////////////////////////////
	//              Visit Methods                    //
	///////////////////////////////////////////////////

	public void visit (CompilationUnit n) 
    {
		top = new Env();
        n.block.accept(this) ;
    }

    public void visit (BlockStatementNode n)
    {
		top = new Env(top);
        n.sTable = top;
		enclosingBlock = n;

		n.stmts = n.headStmt;
		n.decls = n.headDecl;

		while(n.decls != null)
        {
			n.decls.accept(this);
			n.decls = n.decls.nextDecl;
		}
		
		while(n.stmts != null)
		{
			n.stmts.accept(this);
			n.stmts = n.stmts.nextStmt;
		}

		top = top.prev == null? top: top.prev;
		enclosingBlock = n.parent;
    }

	public void visit (DeclarationsNode n)
    {
		//UNUSED
    }

	public void visit (DeclarationNode n)
    {
		if(n.typeNode != null)
		{	
			n.typeNode.accept(this);
		}

		//This will initialize all the variables because the test code doesn't initialize any variables
		if(n.id != null)
		{
			n.id.accept(this);

			IdentifierNode temp = TempNode.newTemp();
			top.put(n.id.w, temp);
			if(n.id.typeNode.array != null)
			{
				/*
				this is done to keep track of what environment the array is in. Therefore
				a change in an array index within a nested environment will modify the
				array in the outter environment.
				*/
				n.assign = new AssignmentNode(temp, n.id);
			}
			else
			{
				if(n.id.type == Type.Bool || n.id.type == Type.Boolean)
				{
					n.assign = new AssignmentNode(temp, new FalseNode());
				}
				else
				{
					n.assign = new AssignmentNode(temp, new NumNode(new Num(0)));
				}
			}
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
			n.node.accept(this);
		}
    }

	public void visit (ForNode n)
    {
		//println("ForNode");
	}

	public void visit(IfNode n)
    {
        boolean stmtIsBlock = (n.stmt instanceof BlockStatementNode);
        boolean isElse = (n.elseStmt instanceof BlockStatementNode);
		IdentifierNode temp;// = TempNode.newTemp();
		ParenthesesNode cond = n.cond;
		ExpressionNode expr = null;
		AssignmentNode assign = null;

        if(n.cond != null)
        {
			temp = TempNode.newTemp();

            //If Statement
            if(!n.isElif)
            {
				n.falseLabel = LabelNode.newLabel();
            }

            if(n.cond != null)
            {
                n.cond.accept(this);
            }

			expr = cond.expr;

			assign = new AssignmentNode(temp, expr);
	
			n.assigns.addStmt(assign);
	
			n.cond.expr = temp;	

			//This is the expression following the condition. Either block or stmt
			n.stmt.accept(this); 

            // Else Statement
            if(n.elseStmt != null && !(n.elseStmt instanceof IfNode))
            {
				n.elifLabel = LabelNode.newLabel();
                n.elseStmt.accept(this);
            }
            // If or Else If Statement
            else if(n.elseStmt !=  null)
            {
				n.elifLabel = LabelNode.newLabel();
				((IfNode)n.elseStmt).falseLabel = n.falseLabel;
                n.elseStmt.accept(this);
            }
            // No More Statements
            else
            {
				n.elifLabel = n.falseLabel;
                //Done
            }
        }
    }

	public void visit (WhileNode n)
    {	
		IdentifierNode temp = TempNode.newTemp();
		ParenthesesNode cond = n.cond;
		ExpressionNode expr = null;
		AssignmentNode assign = null;

		n.trueLabel = LabelNode.newLabel();

		if(n.cond != null)
		{
			n.cond.accept(this);
		}

		expr = cond.expr;

		assign = new AssignmentNode(temp, expr);

		n.assigns.addStmt(assign);

		n.cond.expr = temp;

		
		n.falseLabel = LabelNode.newLabel();

		n.stmt.accept(this);
    }


	public void visit (DoNode n)
    {	
		IdentifierNode temp = TempNode.newTemp();
		ParenthesesNode cond = n.cond;
		ExpressionNode expr = null;
		AssignmentNode assign = null;

		n.trueLabel = LabelNode.newLabel();

		n.stmt.accept(this);

		if(n.cond != null)
		{
			n.cond.accept(this);
		}

		expr = cond.expr;

		assign = new AssignmentNode(temp, expr);

		n.assigns.addStmt(assign);

		n.cond.expr = temp;

		n.falseLabel = LabelNode.newLabel();
    }

	public void visit (ParenthesesNode n)
    {
		if(n.expr != null)
		{
			n.expr.accept(this);
		}
    }

	// Do not need to change assignment.  Id = Expr;
    public void visit (AssignmentNode n)
    {
		if(n.left != null && n.left instanceof IdentifierNode)
		{
			IdentifierNode left = null;
			IdentifierNode temp = null;
			AssignmentNode assign = null;
			Word leftsWord = null;

			if(n.left instanceof ArrayAccessNode) 
			{
				ArrayAccessNode arrLeft = (ArrayAccessNode)n.left;
				left = arrLeft;

				//this must be done because the n.left.w has a different signature than what is found in the symbol table
				//because with every arrayAccessNode a new word is created that will not match any other word
				String wordString = arrLeft.w.toString(); 	//turn lefts word into a string
				leftsWord = top.getWord(wordString); 		//get the word from symbol table that matches left's word
				if(leftsWord != null)	//the symbol table DOES have a value for the array index
				{
					temp = top.get(leftsWord); //get the temp label
				}
				else					//the symbol table does NOT have a value for the array index
				{
					leftsWord = arrLeft.w;
					temp = TempNode.newTemp();
				}

				//println("Attempting to add: " + arrLeft.w.toString() + " : " + temp);
				top.putArray(arrLeft, temp);
				//top.print(true);
			}
			else
			{
				left = (IdentifierNode)n.left;
				if(left != null)
				{
					leftsWord = left.w;
					temp = top.get(left.w);
				}

				if(temp == null)
				{
					temp = TempNode.newTemp();
					top.put(leftsWord, temp);
				}
			}

			assign = new AssignmentNode(temp, n.left);
	
			enclosingBlock.stmts.addStmt(assign);

			n.left = temp;

			n.left.accept(this);
			//top.print(true);
		}

		if(n.right != null && n.right instanceof BinaryNode)
		{
			BinaryNode right = (BinaryNode)n.right;
			right.accept(this);	
		}
		else if(n.right != null && n.right instanceof ParenthesesNode)
		{
			AssignmentNode assign;
			IdentifierNode temp;
			ParenthesesNode right = (ParenthesesNode)n.right;
			ExpressionNode expr = right.expr;
			right.accept(this);
			temp = TempNode.newTemp();
			assign = new AssignmentNode(temp, expr);
			n.assigns.addStmt(assign);
			n.right = temp;	
		}
		else if(n.right != null && n.right instanceof ArrayAccessNode) 
		{
			ArrayAccessNode arrRight = (ArrayAccessNode)n.right;
			IdentifierNode right = top.get(arrRight.w);

			if(right == null)
			{
				right = arrRight;
			}
			n.right = right;

			right.accept(this);
		}
		else if(n.right != null && n.right instanceof IdentifierNode)
		{
			IdentifierNode right = top.get(((IdentifierNode)n.right).w);
			if(right != null)
			{
				n.right = right;
				right.accept(this);
			}
			else
			{
				n.right.accept(this);
			}
		}
		else
		{
			n.right.accept(this);
		}

		//temp.value = n.right;
    }


	/*
												BinaryNode
			Left: if binary --> AssignmentNode				Right: if binary --> AssignmentNode
			Left --> temp: IdNode							Right --> temp: IdNode
	*/
	// Every binary node must be turned into an assignment node
	public void visit (BinaryNode n) 
    {
		AssignmentNode assign;
		IdentifierNode temp;

		if(n.left != null && n.left instanceof BinaryNode)
		{
			BinaryNode left = (BinaryNode)n.left;
			left.accept(this);
			temp = TempNode.newTemp();
			assign = new AssignmentNode(temp, left);
			n.assigns.addStmt(assign);

			//left.accept(this);			//visit the binary node
			n.left = temp;				//left become identifier node		
		}
		else if(n.left != null && n.left instanceof ParenthesesNode)
		{
			ParenthesesNode left = (ParenthesesNode)n.left;
			ExpressionNode expr = left.expr;
			left.accept(this);
			temp = TempNode.newTemp();
			assign = new AssignmentNode(temp, expr);
			n.assigns.addStmt(assign);
			n.left = temp;
		}
		else if(n.left != null && n.left instanceof ArrayAccessNode) 
		{
			ArrayAccessNode arrleft = (ArrayAccessNode)n.left;
			IdentifierNode left = top.get(arrleft.w);

			if(left == null)
			{
				left = arrleft;
			}
			n.left = left;

			left.accept(this);
		}
		else if(n.left != null && n.left instanceof IdentifierNode)
		{
			n.left = top.get(((IdentifierNode)n.left).w);
		}

		if(n.right != null && n.right instanceof BinaryNode)
		{
			BinaryNode right = (BinaryNode)n.right;
			right.accept(this);
			temp = TempNode.newTemp();
			assign = new AssignmentNode(temp, right);
			n.assigns.addStmt(assign);
			
			//n.right.accept(this);
			n.right = temp;	
		}
		else if(n.right != null && n.right instanceof ParenthesesNode)
		{
			ParenthesesNode right = (ParenthesesNode)n.right;
			ExpressionNode expr = right.expr;
			right.accept(this);
			temp = TempNode.newTemp();
			assign = new AssignmentNode(temp, expr);
			n.assigns.addStmt(assign);
			n.right = temp;		
		}
		else if(n.right != null && n.right instanceof ArrayAccessNode) 
		{
			ArrayAccessNode arrRight = (ArrayAccessNode)n.right;
			IdentifierNode right = top.get(arrRight.w);

			if(right == null)
			{
				right = arrRight;
			}
			n.right = right;

			right.accept(this);
		}
		else if(n.right != null && n.right instanceof IdentifierNode)
		{
			n.right = top.get(((IdentifierNode)n.right).w);
		}
    }

    public void visit (ExpressionNode n)
    {
		if(n.fact != null)
		{
			println("expr is id");
			n.fact.accept(this);	
		}
		if(n.expr != null)
		{
			println("expr is expr");
			n.expr.accept(this);
			n.type = n.expr.type;
		} 
		if(n.bin != null)
		{
			println("expr is bin");
			n.bin.accept(this);
			n.type = n.bin.type;
		}
    }

	public void visit (ArrayAccessNode n)
    {
		if(n.id instanceof IdentifierNode)
		{
			IdentifierNode temp = top.get(((IdentifierNode)n.id).w);
			if(temp != null)
			{
				n.id = temp;
			}
			n.id.accept(this);
		}
			
			n.index.accept(this); // location in array to access
    }

	public void visit (ArrayDimsNode n)
    {	
		if(n.size instanceof IdentifierNode)
		{
			IdentifierNode temp = top.get(((IdentifierNode)n.size).w);
			if(temp != null)
			{
				n.size = temp;
			}
			n.size.accept(this);
		}		
		
		if (n.dim != null)
		{
			n.dim.accept(this);
		}
    }

	public void visit (TypeNode n)
    {			
		if(n.array != null)
		{
			n.array.accept(this);
		}		
	}

	public void visit (ArrayTypeNode n)
    {		
		
	}

    public void visit (NumNode n) 
    {		

    }

	public void visit (RealNode n) 
    {		
  
    }

    public void visit (IdentifierNode n) 
    {

    }

	public void visit (TrueNode n)
    {      
 
    }


    public void visit (FalseNode n)
    {      

    }

	public void visit (BreakStmtNode n)
    {		
		
    }

    public void visit(Node n)
    {		

    }

	public void visit(GotoNode n)
    {

    }

    public void visit(LabelNode n)
    {

    }

    public void visit(TempNode n)
    {

    }
}