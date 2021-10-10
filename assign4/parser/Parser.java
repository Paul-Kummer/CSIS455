package assign4.parser ;

import assign4.visitor.* ;
import assign4.lexer.* ;
import java.io.* ;

public class Parser extends ASTVisitor 
{

    public CompilationUnit cu = null ;
    public Lexer lexer        = null ; 
    public Token look = null;

    public Parser () 
    {
        cu = new CompilationUnit() ;

        move();

        visit(cu) ;
    }

    public Parser (Lexer lexer) 
    { 
        this.lexer = lexer ;
        cu = new CompilationUnit() ;

        move();

        visit(cu) ;
    }
    


    ////////////////////////////////////////
    //  Utility mothods
    ////////////////////////////////////////

    void move () 
    {
        try 
        {
            look = lexer.scan() ;
        }
        catch(IOException e) 
        {  
            System.out.println("IOException") ;
        }
    }

    void error (String s) 
    {
        throw new Error ("near line " + lexer.line + ": " + s) ;
    }

    void match (int t) 
    {
        try 
        {
            if (look.tag == t)
            {
                move() ;
            }
            
            else
            {
                error("Syntax error") ;
            }	
        }
        catch(Error e) 
        {
            System.out.println("Error") ;
        }	
    }

    ////////////////////////////////////////
    
    public void visit (CompilationUnit n) 
    {
        System.out.println("CompilationUnit");

        n.block = new BlockStatementNode();
        n.block.accept(this);
    }

    public void visit (AdditionNode n) 
    {
        n.left = new LiteralNode() ;
        n.left.accept(this) ;
        
        n.right = new LiteralNode() ;
        n.right.accept(this) ;
    }

    public void visit (LiteralNode n) 
    {
        // What should visit(LiteralNode) do? 
        // One part of the next assignment.
    }

    public void visit (BlockStatementNode n) 
    {
        System.out.println("BlockStatementNode");

        if(look.tag == '{')
        {
            System.out.println("Matched with '{': " + look.tag);
        }
        match('{');

        n.assign = new AssignmentNode();
        n.assign.accept(this);

        if(look.tag == '}')
        {
            System.out.println("Matched with '}': " + look.tag);
        }
        match('}');
    }

    public void visit(AssignmentNode n)
    {
        n.id = new IdentifierNode();
        n.id.accept(this);

        if(look.tag == '=')
        {
            System.out.println("Matched with '=': " + look.tag);
        }
        match('=');

        n.right = new IdentifierNode();
        n.right.accept(this);

        if(look.tag == ';')
        {
            System.out.println("Matched with ';': " + look.tag);
        }
        match(';');
    }

    public void visit (IdentifierNode n) 
    {
        n.id = look.toString();

        match(look.tag);

        System.out.println("IdentifierNode: " + n.id);
    }
}
