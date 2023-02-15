package intercode.ast;

import intercode.visitor.* ;
import intercode.lexer.*;
import intercode.Env;
import intercode.ast.*;
import intercode.inter.*;

public class DoNode extends StatementNode
{
	public Boolean Break = false;
	public StatementNode stmt = null;
	public ParenthesesNode cond = null;

	//Intermediate code use
	public StatementNode assigns = null; // linked list of nodes to add
	public LabelNode falseLabel;
	public LabelNode trueLabel;

	public DoNode ()
	{
		super();
		assigns = new StatementNode();
	}

	public DoNode (StatementNode stmt, ParenthesesNode cond)
	{
		this.stmt = stmt;
		this.cond = cond;
		assigns = new StatementNode();
	}

	public String toString()
	{
        return "do{decls; stmts;} while(cond)";
	}
	
	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
}