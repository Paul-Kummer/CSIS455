package intercode.ast;

import intercode.visitor.* ;
import intercode.lexer.*;
import intercode.Env;
import intercode.ast.*;

public class StatementNode extends Node
{
	/*
	node can be AssignmentNode, DoNode, WhileNode, ForNode, IfNode, or BlockStatementNode
	*/
	public StatementNode head = null;
	public StatementNode nextStmt = null;
	public Boolean isConditional = false;
	public Node	node;
	public Type type;

	public StatementNode ()
	{
        this.head = this;
	}

	public StatementNode(StatementNode head)
	{
		this.head = head;
	}

	public StatementNode(StatementNode head, Node node)
	{
		this.head = head;
		this.node = node;
	}

	public StatementNode(Node node)
	{
		this.node = node;
	}

	public String toString()
	{
		if(this.node != null)
		{
			return node.toString();
		}
		else if(this.type != null)
		{
			return this.type.toString();
		}
		else
		{
			return "Statement";
		}
	}
	
	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}

 

	//////////////////////////////////////////////////////////////////////                               
	//                      Utility Methods                             //                              
	//////////////////////////////////////////////////////////////////////                              
	                                                                                                      
    //  addStmt(Node)
    //  containsStmt(Node)
    //  replaceStmt(Node, Node)
    //  indexOfStmt(Node)

	/*
    Adds a StatementNode at the current end of the StatementNode linked-list

    ************************ List before Insertion ******************************
    headStmt            stmt0.nextStmt      stmt1.nextStmt      stmt2.nextStmt
    stmt0 -->           stmt1 -->           stmt2 -->           null

    *********************** List after Insertion ********************************
    headStmt            stmt0.nextStmt      stmt1.nextStmt      stmt2.nextStmt      newStmt.nextStmt
    stmt0 -->           stmt1 -->           stmt2 -->           newStmt -->         stmtBeingProcessed

    */
    public void addStmt(Node node)
    {
        System.out.println("Adding a statement");

        StatementNode newStmt;                    //The statement to inject into the list
        StatementNode previousStmt = null;        //The statement before the injection
        StatementNode curStmt = this.head;        //current statement in loop

        // Iterate over all StatementNodes in the linked-list
        while(curStmt != null && curStmt.node != null)     //if the node is null, the parse statement has not written to it
        {
            previousStmt = curStmt;     //the statement just before the current statement
            curStmt = curStmt.nextStmt; //advance the statement
        }

        newStmt = new StatementNode(this.head, node);

        if(previousStmt == null) //injecting as the head statement
        {
            this.head = newStmt;
            newStmt.head = newStmt;
        }
        else //injecting anywhere else
        {
            previousStmt.nextStmt = newStmt; 
        }

        newStmt.nextStmt = curStmt;
    }


    /*
    Returns Boolean of whether or not a StatementNode currently exists in the 
    StatementNode linked-list
    */
    public boolean containsStmt(Node node)
    {
        System.out.println("Contains a statement?");

        // iterate over all StatemetnNodes in the linked-list
        StatementNode curStmt = this.head;
        while(curStmt != null)
        {
            // the statement is found, return true
            if(curStmt.node == node)
            {
                return true;
            }

            // advance to next statemnt
            curStmt = curStmt.nextStmt;
        }

        // the statment wasn't found, return false
        return false;
    }


    /*
    If a supplied StatementNode (oldNode) exists in the current StatementNode linked-list it is
    replaced with a new StatementNode (newNode)
    */
    public void replaceStmt(Node oldNode, Node newNode)
    {
        System.out.println("Replace a statement");

        // iterate throug all StatemendNodes in the linked-list
        StatementNode curStmt = this.head;
        while(curStmt != null)
        {
            // StatementNode is found and going to be replaced
            if(curStmt.node == oldNode)
            {
                curStmt.node = newNode;
                break;
            }

            // advance to next statement
            curStmt = curStmt.nextStmt;
        }
    }


    /*
    Returns the position of the supplied argument (node) in the linked-list
    of StatementNodes
    */
    public int indexOfStmt(Node node)
    {
        System.out.println("Index of a statement");

        int position = 0;
        StatementNode curStmt = this.head;
        while(curStmt != null)
        {
            if(curStmt.node == node)
            {
                return position;
            }
            
            curStmt = curStmt.nextStmt;
            position++;
        }

        return -1;
    }
}
