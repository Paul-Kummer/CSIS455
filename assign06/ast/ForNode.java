package assign6.ast;

import assign6.visitor.* ;
import assign6.lexer.*;
import assign6.Env;
import assign6.ast.*;

public class ForNode extends StatementNode
{
	// for (int x = 0; x == Num; x = x + 1) { Do Stuff }
	public DeclarationNode decl; // can be declaration or just assignment
	public ExpressionNode cond; // must be something that derives to Boolean
	public AssignmentNode assign; // optional statement beformed after every block

	public BlockStatementNode block; // This executes on every iteration
	
	public ForNode ()
	{
		super();
	}

	public ForNode (BlockStatementNode block)
	{
		this.block = block;
	}

	public ForNode (DeclarationNode decl)
	{
		this.decl = decl;
	}

	public ForNode (ExpressionNode cond)
	{
		this.cond = cond;
	}

	public ForNode (AssignmentNode assign)
	{
		this.assign = assign;
	}

	public String toString()
	{
        return "for(stmt; cond; stmt){decls; stmts;}";
	}
	
	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}