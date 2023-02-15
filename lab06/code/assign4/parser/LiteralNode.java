package assign4.parser ;

import assign4.lexer.*;
import assign4.visitor.* ;

//This is for number or string and a terminal
public class LiteralNode extends Node 
{
	public Num v;
    public int literal;
    public String string;

    public LiteralNode () 
    {

    }
    
	public LiteralNode (Num v) 
    {
        this.v = v ;
        this.literal = v.value;
        this.string = "" + v.value;
    }
	
    public LiteralNode (String string) 
    {
        this.string = string ;
    }

    public void accept(ASTVisitor v) 
    {
        v.visit(this);
    }

    public void printNode () 
    {
        System.out.println("LiteralNode: " + literal) ;
    }
}
