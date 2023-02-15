package assign6.typechecker;

import assign6.Env;
import assign6.visitor.*;
import assign6.lexer.*;
import assign6.ast.*;
import assign6.parser.*;

//Timestamp: 47:15

//Timestamp: 52:34
public class TypeChecker extends ASTVisitor
{
	public Parser parser = null;
	public CompilationUnit cu = null ;

	int line = 0;

	public TypeChecker ()
	{
		visit(this.parser.cu);
	}

	public TypeChecker (Parser parser)
	{
		this.parser = parser;
		cu = parser.cu;
		visit(cu);
	}

	///////////////////////////////////////////////////
	//              Utility Methods                  //
	///////////////////////////////////////////////////

	void error (String s)
	{
		if(parser.lexer != null)
		{
			println("near line ( " + parser.lexer.line + " ): " + s);
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
		line++;
		System.out.println(s);
	}


	public Type compareTypes(Type lhs, Type rhs)
	{
		if(lhs == null && rhs == null)
		{
			error("Missing types");
		}
		else if(lhs != null && rhs == null)
		{
			println("\t[Compatible]\t(LHS: " + lhs + " | " + rhs + " :RHS)");
			return lhs;
		}
		else if(lhs == null && rhs != null)
		{
			println("\t[Compatible]\t(LHS: " + lhs + " | " + rhs + " :RHS)");
			return rhs;
		}
		// !!! USE THIS TO DETECT TYPE ERROR IN EXPRESSIONS !!! (currently used, detect error in assignment manually)
		// This will not detect Float to Int mismatch for assignment
		else if(lhs == Type.Float || lhs == Type.Int) 
		{
			if(rhs != Type.Int && rhs != Type.Float)
			{
				error("Type mismatch: the types must be numeric (bad Type) --> " + rhs);
			}

			println("\t[Compatible]\t(LHS: " + lhs + " | " + rhs + " :RHS)");
			Type returnType = ((lhs == Type.Float) || (rhs == Type.Float))? Type.Float: Type.Int;
			return returnType;
		}
		// !!! USE THIS TO DETECT ERRORS IN ASSIGNMENT !!! (unused and impossible to reach)
		// This will detect Float to Int mismatch in assignment, but also throws a bad error in expressions
		/*else if(lhs == Type.Int) 
		{
			if(rhs != Type.Int)
			{
				error("Type mismatch: the types must be Integers (bad Type) --> " + rhs);
			}

			println("LHS's Type: " + lhs);
			println("RHS's Type: " + rhs);
			return Type.Int;
		}*/	
		else if(lhs == Type.Bool)
		{
			if(rhs != Type.Bool)
			{
				error("Type mismatch: the types must be Boolean (bad Type) --> " + rhs);
			}

			println("\t[Compatible]\t(LHS: " + lhs + " | " + rhs + " :RHS)");
			return Type.Bool;
		}
		else if(lhs instanceof Array)
		{
			if(!(rhs instanceof Array))
			{
				error("Type mismatch: the types must be an Array (bad Type) --> " + rhs);
			}
			else
			{
				compareTypes(((Array)lhs).type, ((Array)rhs).type);
			}
		}
		else
		{
			error("Could not compare types");
		}
		return null;
	}

	///////////////////////////////////////////////////
	//              Visit Methods                    //
	///////////////////////////////////////////////////

	public void visit (CompilationUnit n) 
    {
		//println("CompilationUnit");
        n.block.accept(this) ;
    }

    public void visit (BlockStatementNode n)
    {
		//println("BlockStatementNode");

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
    }

	public void visit (DeclarationsNode n)
    {
		//UNUSED
    }

	public void visit (DeclarationNode n)
    {
		//println("DeclarationNode");

		if(n.typeNode != null)
		{	
			n.typeNode.accept(this);
			n.id.accept(this);

			if(n.assign != null) //assignment after declaration
			{
				n.assign.accept(this);	
			}
		}	
    }
    
    public void visit (StatementsNode n)
    {
		//UNUSED
    }

	public void visit (StatementNode n)
    {
		//println("StatementNode");

		if(n.node != null)
		{	
			n.node.accept(this);

			if(n.node instanceof AssignmentNode)
			{
				n.type = ((AssignmentNode)n.node).type;
			}
		}
    }

	public void visit (ParenthesesNode n)
    {
		//println("ParenthesesNode");

		if(n.expr != null)
		{	
			n.expr.accept(this);
			n.type = n.expr.type;
		}
    }

	public void visit (IfNode n)
    {
		//println("IfNode");
		if(n.cond != null)
		{
			/*if(n.cond.type != Type.Bool)
			{
				System.out.println("IfNode type is: " + n.cond.type);
				error("The condition must be boolean");
			}*/
			n.cond.accept(this);
		}

		if(n.stmt != null)
		{
			n.stmt.accept(this);
		}
		
		if(n.elseStmt != null)
		{					
			n.elseStmt.accept(this);			
		}
    }

	public void visit (WhileNode n)
    {
		//println("WhileNode");		
		if(n.cond != null)
		{
			/*if(n.cond.type != Type.Bool)
			{
				System.out.println("WhileNode type is: " + n.cond.type);
				error("The condition must be boolean");
			}*/
			n.cond.accept(this);
		}

		n.stmt.accept(this);		
    }

	public void visit (DoNode n)
    {
		//println("DoNode");		
		n.stmt.accept(this);

		if(n.cond != null)
		{
			/*if(n.cond.type != Type.Bool)
			{
				System.out.println("DoNode type is: " + n.cond.type);
				error("The condition must be boolean");
			}*/
			n.cond.accept(this);
		}
    }

	public void visit (ForNode n)
    {
		//println("ForNode");
	}

	public void visit (ArrayAccessNode n)
    {		
		println("ArrayAccessNode");	
		n.id.accept(this); // identifier holding the array
		n.index.accept(this); // location in array to access
		if(n.index.type != Type.Int)
		{
			error("An index must be an integer, current type [ " + n.index.type + " ]");
		}
    }

	public void visit (ArrayDimsNode n)
    {		
		println("ArrayDimsNode");		
		n.size.accept(this);
		
		if (n.dim != null)
		{
			n.dim.accept(this);
		}		
    }

    public void visit (AssignmentNode n)
    {
		println("AssignmentNode: =");
		Type leftType = null;
		Type rightType = null;
		
		if(n.left instanceof IdentifierNode)
		{
			n.left.accept(this);
			if(	((IdentifierNode)n.left).typeNode != null && 
				((IdentifierNode)n.left).typeNode.array != null)
			{
				leftType = new Array(((IdentifierNode)n.left).type);
			}
			else
			{
				leftType = ((IdentifierNode)n.left).type;
			}
			//println("LHS's Type: " + leftType);
		}

		else
		{
			error("Could not get LHS type");
		}
    
		if(n.right != null)
		{
			n.right.accept(this);
			if(n.right instanceof NumNode)
			{
				rightType = Type.Int;
			}
			else if(n.right instanceof RealNode)
			{
				rightType = Type.Float;
			}
			else if(n.right instanceof TrueNode || n.right instanceof FalseNode)
			{
				rightType = Type.Bool;
			}
			else if(n.right instanceof IdentifierNode)
			{
				if(	((IdentifierNode)n.right).typeNode != null && 
					((IdentifierNode)n.right).typeNode.array != null)
				{
					rightType =  new Array(((IdentifierNode)n.right).type);

					//check array types for float to in mismatch
					if(	((Array)leftType).type == Type.Int && 
						((Array)rightType).type == Type.Float)
					{
						error("Type mismatch: the array type must be an Integer (bad Type) --> " + rightType);
					}
				}
				else
				{
					rightType = ((IdentifierNode)n.right).type;
				}	
			}
			else if(n.right instanceof BinaryNode)
			{
				rightType = ((BinaryNode)n.right).type;
			}
			else if (n.right instanceof ParenthesesNode)
			{
				rightType = ((ParenthesesNode)n.right).type;
			}	
			else
			{
				error("Could not get RHS type");
			}
		}

		// The error detection is done manually here because it cannot be detected in the compareTypes() method
		if(leftType == Type.Int && rightType == Type.Float)
		{
			error("Type mismatch: the type must be an Integer (bad Type) --> " + rightType);
		}
		Type assignType = compareTypes(leftType, rightType);
    }


	// Time: 56:50
	public void visit (BinaryNode n) 
    {
		println("BinaryNode: " + n.op);
		n.type = null;
		Type leftType = null;
		Type rightType = null;

		if(n.left != null)
		{
			n.left.accept(this);

			if(n.left instanceof NumNode)
			{
				leftType = Type.Int;
			}
			else if(n.left instanceof RealNode)
			{
				leftType = Type.Float;
			}
			else if(n.left instanceof TrueNode || n.right instanceof FalseNode)
			{
				leftType = Type.Bool;
			}
			else if(n.left instanceof IdentifierNode)
			{
				if(	((IdentifierNode)n.left).typeNode != null && 
				((IdentifierNode)n.left).typeNode.array != null)
				{
					leftType = new Array(((IdentifierNode)n.left).type);
				}
				else
				{
					leftType = ((IdentifierNode)n.left).type;
				}
			}
			else if(n.left instanceof BinaryNode)
			{
				leftType = ((BinaryNode)n.left).type;
			}
			else if(n.left instanceof ParenthesesNode)
			{
				leftType = ((ParenthesesNode)n.left).type;
			}
			else
			{
				error("Could not get LHS type");
			}
			n.type = leftType;
		}
		
		if(n.right != null)
		{
			n.right.accept(this);
			if(n.right instanceof NumNode)
			{
				rightType = Type.Int;
			}
			else if(n.right instanceof RealNode)
			{
				rightType = Type.Float;
			}
			else if(n.right instanceof TrueNode || n.right instanceof FalseNode)
			{
				rightType = Type.Bool;
			}
			else if(n.right instanceof IdentifierNode)
			{
				if(	((IdentifierNode)n.right).typeNode != null && 
				((IdentifierNode)n.right).typeNode.array != null)
				{
					rightType = new Array(((IdentifierNode)n.right).type);
				}
				else
				{
					rightType = ((IdentifierNode)n.right).type;
				}
			}
			else if(n.right instanceof BinaryNode)
			{
				rightType = ((BinaryNode)n.right).type;
			}
			else if(n.right instanceof ParenthesesNode)
			{
				rightType = ((ParenthesesNode)n.right).type;
			}
			else
			{
				error("Could not get RHS type");
			}
			n.type = compareTypes(leftType, rightType);
		}

		if(n.isConditional && n.type != null && n.type == Type.Bool) //check if the condition expr and that the operator is comparision
		{
			switch(n.op.tag)
			{
				// Allowed operators
				case Tag.AND:
				case Tag.OR:
				case Tag.EQ:
				case Tag.NE:
				case Tag.GE:
				case Tag.LE:
				case Tag.LT:
				case Tag.GT:
				case '>':
				case '<':
					break;
				default:
					error("The conditional statement requires a conditional operator");
			}
		}
    }

    public void visit (ExpressionNode n)
    {		
		println("ExpressionNode");

		if(n.fact != null)
		{
			n.fact.accept(this);	
		}
		if(n.expr != null)
		{
			n.expr.accept(this);
			n.type = n.expr.type;
		} 
		if(n.bin != null)
		{
			n.bin.accept(this);
			n.type = n.bin.type;
		}
    }

	public void visit (TypeNode n)
    {		
		//println("TypeNode: " + n.basic.lexeme);
		
		if(n.array != null)
		{
			n.array.accept(this);
		}		
	}

	public void visit (ArrayTypeNode n)
    {		
		//println("ArrayTypeNode: size[ " + n.size + " ]");
		
		//compare the types of id and array
		if(n.type != null)
		{
			println(n.type.toString());
		}		
	}

    public void visit (NumNode n) 
    {		
    	n.printNode();
    }

	public void visit (RealNode n) 
    {		
        n.printNode();
    }

    public void visit (IdentifierNode n) 
    {		
        n.printNode();
    }

	public void visit (TrueNode n) // value is Word.True
    {      
        n.printNode();
    }


    public void visit (FalseNode n) // value is Word.False
    {      
        n.printNode();
    }

	public void visit (BreakStmtNode n)
    {		
		n.printNode();
    }

    public void visit(Node n)
    {		
		println("Node");
    }
}
