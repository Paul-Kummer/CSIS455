package intercode.ast;

import intercode.visitor.* ;
import intercode.lexer.*;
import intercode.ast.*;
import intercode.inter.*;

public class IfNode extends StatementNode
{
	public ParenthesesNode cond;
	public StatementNode stmt;
	public StatementNode elseStmt;
	public boolean isElif = false;

	public StatementNode assigns = null; // linked list of nodes to add
	public LabelNode falseLabel; // will go to else statement if it exists
	public LabelNode elifLabel;
	
	public IfNode ()
	{
		super();
		assigns = new StatementNode();
	}

	public IfNode (ParenthesesNode cond, BlockStatementNode stmt, StatementNode elseStmt)
	{
		assigns = new StatementNode();
		this.cond = cond;
		this.stmt = stmt;
		this.elseStmt = elseStmt;
	}

	public String toString()
	{
        return "if(cond){decls; stmts;}\nelse if(cond){decls; stmts;}\nelse{decls; stmts;}";
	}

	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}
