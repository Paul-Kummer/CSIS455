package intercode.ast;

import intercode.visitor.* ;
import intercode.lexer.*;
import intercode.Env;
import intercode.ast.*;
import intercode.inter.*;

public class WhileNode extends StatementNode
{
	public Boolean Break = false;
	public ParenthesesNode	cond = null;
	public StatementNode stmt = null;

	//Intermediate code use
	public StatementNode assigns = null; // linked list of nodes to add
	public LabelNode falseLabel;
	public LabelNode trueLabel;

	public WhileNode ()
	{
		super();
		assigns = new StatementNode();
	}

	public WhileNode (ParenthesesNode cond, StatementNode stmt)
	{
		this.cond = cond;
		this.stmt = stmt;
		assigns = new StatementNode();
	}

	public String toString()
	{
        return "While(cond){decls; stmts;}";
	}
	
	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}