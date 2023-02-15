package assign4.parser ;

import assign4.visitor.* ;
import assign4.lexer.* ;
import assign4.parser.IdentifierNode;

import java.io.* ;

public class Parser extends ASTVisitor 
{
    public CompilationUnit cu = null ;
    public Lexer lexer = null ; 
    public Token look = null;
    private int level = 0;

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

    private void dots()
	{
		System.out.print(new String(new char[level*4]).replace('\0', '.'));
	}

    //////////////////////////////////////////////////////////////////////
    /*             [ Grammars ]
        program 🡪 CompilationUnit
        CompilationUnit 🡪 BlockStatementNode
        BlockStatementNode 🡪 ‘{‘ [ StatementNode ] ‘}’
        StatementNode 🡪 AssignmentNode ‘;’
        AssignmentNode 🡪 ‘=’ TermNode op ExpressionNode | ‘=’ TermNode
        TermNode 🡪 UnaryNode | IdentifierNode | BinaryNode
        UnaryNode🡪 '-' IdentifierNode
        op 🡪  ( '+' | '-' | '*' | '/' | '%' )
        ExpressionNode 🡪 TermNode | BinaryNode
        BinaryNode 🡪 op ExpressionNode
    */
    //////////////////////////////////////////////////////////////////////


    //Compilation Unit: start of program
    public void visit (CompilationUnit n) 
    {
        System.out.println("CompilationUnit");
        
        level++;
        dots();

        n.block = new BlockStatementNode();
        n.block.accept(this);

        level--;
    }

    //Block Statement: child of compilation unit
    public void visit (BlockStatementNode n) 
    {
        System.out.println("BlockStatementNode");
        boolean head = true;

        /*
        if(look.tag == '{')
        {
            System.out.println("Matched with '{': " + look.tag);
        }
        */
        match('{');

        //Read in all statements until the end of the block
        while(look.tag != '}')
        {
            level++;
            dots();

            //if this is the first statment, make it the head then start a linked list
            if(head)
            {
                n.stmt = new StatementNode();
                n.head = n.stmt;
                head = false;
            }

            n.stmt.accept(this);

            //advance the statement to the next statement if it exists. Also link the list
            if(look.tag != '}')
            {
                n.stmt.nextStatement = new StatementNode();
                n.stmt = n.stmt.nextStatement;
            }
            
            level--;
        }

        /*
        if(look.tag == '}')
        {
            System.out.println("Matched with '}': " + look.tag);
        }
        */
        match('}');
    }

    //Statement: child of block
    public void visit (StatementNode n) 
    {
        System.out.println("StatementNode");
        //In a fully functional version, this should also accept a function call

        level++;
        dots();

        n.assign = new AssignmentNode();
        n.assign.accept(this);

        /*
        if(look.tag == ';')
        {
            System.out.println("Matched with ';': " + look.tag);
        }
        */
        match(';');

        level--;
    }

    //Assignment: child of expression
    public void visit(AssignmentNode n)
    {
        System.out.println("AssignmentNode");

        level++;
        dots();

        n.id = new IdentifierNode();
        n.id.accept(this);

        level--;

        level++;
        dots();

        if(look.tag == '=')
        {
            System.out.println("Op: =");
        }
        match('=');

        level--;

        level++;
        dots();

        n.right = new ExpressionNode();
        n.right.accept(this);

        level--;
    }

    //Expression: child of assignment
    public void visit (ExpressionNode n) 
    {
        System.out.println("ExpressionNode");

        level++;
        dots();

        //first operand or ID
        n.term = new TermNode();
        //This accept has to happen here or the token wont advance, causing the
        //wrong look.tag for checking if it is binary 
        n.term.accept(this); 

        level--;


        //If the next token isn't the end of the statement, it is binary
        if(look.tag != ';')
        {
            level++;
            dots();

            n.bin = new BinaryNode(n.term);
            n.bin.accept(this);

            level--;
        }
        else
        {
            //accept the id since it isn't binary
        }
    }

    //Binary: child of expression
    public void visit (BinaryNode n) 
    {
        System.out.println("BinaryNode");
        Word operator;

        //first operand
        //n.term.accept(this); Not needed accept done in expression   

        level++;
        dots();

        //operator
        switch(look.tag)
        {
            case '+' :
                System.out.println("Op: +");
                operator = new Word("+", look.tag);
                n.op = new IdentifierNode(operator);
                match('+');
                break;
            case '-' :
                System.out.println("Op: -");
                operator = new Word("-", look.tag);
                n.op = new IdentifierNode(operator);
                match('-');
                break;
            case '*' :
                System.out.println("Op: *");
                operator = new Word("*", look.tag);
                n.op = new IdentifierNode(operator);
                match('*');
                break;
            case '/' :
                System.out.println("Op: /");
                operator = new Word("/", look.tag);
                n.op = new IdentifierNode(operator);
                match('/');
                break;
            case '%' :
                System.out.println("Op: %");
                operator = new Word("%", look.tag);
                n.op = new IdentifierNode(operator);
                match('%');
                break;
            default :
                //System.out.println("\tThrowing an op Error!");
                error("Binary Expression Could Not Match Operator");
                break;
        }

        level--;

        //n.op.accept(this); don't use this because the match advances the tokens
        
        level++;
        dots();

        //second operand
        n.expr = new ExpressionNode();
        n.expr.accept(this);
        
        level--;
    }

    //Unary: child of term, always negative otherwise it is an identifier node
    public void visit (UnaryNode n)
    {
        System.out.println("UnaryNode");

        if(look.tag == '-')
        {
            //unary minus operator
            match('-');

            level++;
            dots();

            //first operand
            n.id = new IdentifierNode();
            n.id.accept(this);

            level--;
        }
        else
        {
            error("Unary Error");
        }
    }

    //Term: child of expression, binary
    public void visit (TermNode n) 
    {
        System.out.println("TermNode");

        level++;
        dots();

        if(look.tag == '-')
        {
            n.unary = new UnaryNode();
            n.unary.accept(this);
        }
        else
        {
            n.id = new IdentifierNode();
            n.id.accept(this);  
        }

        level--;
    }

    //this will assign an identifier a string
    public void visit (LiteralNode n) 
    {
        System.out.println("LiteralNode: " + n.literal);
        
        n.literal.toString();

        match(look.tag);
    }

    public void visit (IdentifierNode n) 
    {
        n.id = look.toString();
        System.out.println("IdentifierNode: " + n.id);

        n.id = look.toString();

        match(look.tag);  
    }
}
