package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.*;
import assign5.ast.*;

public class TrueFalseNode  extends Node
{
	public Word v; // Word("true", Tag.TRUE) | Word("false", Tag.FALSE)
    public String value;

    public TrueFalseNode () 
    {

    }
    
	public TrueFalseNode (Word v) 
    {
        this.v = v ;
        this.value = v.toString();
    }

    public void accept(ASTVisitor v) 
    {
        v.visit(this);
    }

    public void printNode () 
    {
        System.out.println("TrueFalseNode: " + value) ;
    }
}
