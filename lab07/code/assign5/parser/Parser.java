package assign5.parser ;

import assign5.visitor.* ;
import assign5.lexer.* ;
import assign5.parser.*;
import java.io.* ;

import javax.lang.model.util.ElementScanner6;


public class Parser extends ASTVisitor 
{
    public CompilationUnit cu = null ;
    public Lexer lexer = null ; 
    public Token look = null;
    private int level = 0; //timestamp - 34:39

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
    


    //////////////////////////////////////////////////////////////////////
    //                      Utility Methods                             //
    //////////////////////////////////////////////////////////////////////

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

    /*
            [ Precedence Chart ]
        01. assignment =, +=, -=, *=, /=, %=, &=, ^=, \=, <<=, >>=, >>>=
        02. tenary ? :
        03. logical OR ||
        04. logical AND &&
        05. bitwise inclusive OR |
        06. bitwise exclusive OR ^
        07. bitwise AND &
        08. equality ==, !=
        09. relational <, >, <=, >=
        11. shift <<, >>, >>>
        12. additive +, -
        13. multiplicative *, /, %
        14. Postfix expr++, expr--
    */
    int getPrecedence (int op)
    {
        switch(op)
        {
            case ')' :
            case '(' :
                return 1024; //big number that will allways supercede
            case '*' :
            case '/' :
            case '%' :
                return 12;
            case '+' :
            case '-' :
                return 11;
            case '<' :
                move();
                if(look.tag == '=')
                {
                    match('=');
                }
                else
                {
                    return -1;
                }
                return 9;
            case '>' :
                move();
                if(look.tag == '=')
                {
                    match('=');
                }
                else
                {
                    return -1;
                }
                return 9;
            case '=' :
                move();
                if(look.tag == '=')
                {
                    match('=');
                }
                else
                {
                    //could be assignment node
                    return -1;
                }
                return 8;
            case '!' :
                move();
                if(look.tag == '=')
                {
                    match('=');
                }
                else
                {
                    //could be negate operation
                    return -1;
                }
                return 8;
            default :
                return -1;
        }
    }

    
    Node parseBinaryNode (Node lhs, int precedence) //impossible to build a correct AST using this while it reads
    {
        //timestamp - 52:44, 47:56
        // If the current op's precedence is higher than that of
        // the previous, then keep traversing down to create a new
        // BinaryNode for a binary expression with higher level precedence
        // Otherwise, create a new BinaryNode for the current lhs and rhs
        int levelDown = 0;
        Token opTok = null;
        Node rhs = null;
        int op = 0;
        lhs = lhs == null? new BinaryNode(): lhs;

        /*
        let the ')' signal an end to the recursion, but it must be cleared after
        the recursive calls complete. The lhs is returned if ')' is encountered
        */
        while(getPrecedence(look.tag) >= precedence && look.tag != ')') 
        {
            opTok = look;
            op = getPrecedence(look.tag);

            level++;
            levelDown++;

            //must check before move for end of expression symbol ')'
            if(look.tag != ')')
            {
                move();//advance over the operator

                dots();
    
                rhs = new FactorNode(); //factor could be the end of the expression IE: a = 2 + ();
                rhs.accept(this);
            }
 
            if(look.tag != ';' && look.tag != ')')
            {
                if(getPrecedence(look.tag) > op)
                {
                    level++;
                    dots();
                    System.out.println("operator: " + look);
        
                    /*
                    while the precedence of the current operator is greater than the previous
                    operator, move the greater precedence down the AST by moving it from the
                    right hand side to the left hand side and creating a new right hand side.
                    */
                    while(getPrecedence(look.tag) > op)
                    {
                        //move the left hand side to right hand side
                        rhs = parseBinaryNode(rhs, getPrecedence(look.tag));

                        if(look.tag == ')') //end of expression
                        {
                            level--;
                            break;
                        }
                    }
        
                    level--;
                }
                else
                {
                    dots();
                    System.out.println("operator: " + look);

                    //level -= levelDown;
                    //return lhs;
                }
            }

            lhs = new BinaryNode(lhs, opTok, rhs);
        }

        level -= levelDown;
        return lhs;
    }
    

    // print the dots for displaying the abstract syntax tree (AST)
    private void dots()
	{
		System.out.print(new String(new char[level*4]).replace('\0', '.'));
	}

    //////////////////////////////////////////////////////////////////////
    //                        Visit Methods                             //
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
        //timestamp - 37:14
        System.out.println("BlockStatementNode");
        //boolean head = true;

        /*
        if(look.tag == '{')
        {
            System.out.println("Matched with '{': " + look.tag);
        }
        */
        match('{');

        //Read in all statements until the end of the block

		level++;

		n.stmts = new StatementsNode();
		n.stmts.accept(this);
		
		level--;

        /*
        if(look.tag == '}')
        {
            System.out.println("Matched with '}': " + look.tag);
        }
        */
        match('}');
    }
	
	//Statement: child of block
    public void visit (StatementsNode n) 
    {
        //timestamp - 38:27
       
		if(look.tag != '}')
		{
            dots();
            System.out.println("StatementsNode");

            level++;
	    	dots();
            
            //This may need to change from assignment node
            n.assign = new AssignmentNode();
            n.assign.accept(this);
            match(';');

            level--;
            
            n.stmts = new StatementsNode();
            n.stmts.accept(this);
		}
    }

    //Assignment: child of stmts
    public void visit(AssignmentNode n)
    {
        //timestamp - 38:49
        System.out.println("AssignmentNode");

        level++;
        dots();

        //left-hand-side
        n.left = new FactorNode();
        n.left.accept(this);

        level--;

        level++;
        dots();

        if(look.tag == '=')
        {
            n.op = look;
            System.out.println("Op: =");
        }
        else
        {
            error("AssignmentNode missing '=' operator");
        }
        match('=');

        level--;

        level++;
        dots();

        //Does this really need to test for identifier or literal
        //That happens in the termnode. The expressionNode also
        //checks for binary or unary
        n.right = new ExpressionNode();
        n.right.accept(this);

        level--;
    }

    //Expression: child of assignment | factor
    public void visit (ExpressionNode n) 
    {
        //timestamp - 41:59 lecture video uses binary expression only
        System.out.println("ExpressionNode");

        level++;
        dots();

        FactorNode rhs_assign = new FactorNode();
        rhs_assign.accept(this);

        level--;

        if(look.tag == ';') //the expression is unary
        {
            //n.fact is rhs of assign now
            n.fact = rhs_assign;
        }
        else //the expression is binary
        {
            level++;
            dots();

            System.out.println("operator: " + look);

            level--;

            //n.bin is rhs of assign now
            n.bin = (BinaryNode)parseBinaryNode(rhs_assign, 0); //0 is the default level for operator precedence
            //System.out.println("\t---- Root Node Operator: " + n.bin.op + " ----");

            //This character must remain in parseBinaryNode to force the recursion to stop
            if(look.tag == ')') 
            {
                move(); //clear the end of expression character
            }
        }
    }

    //Binary: child of expression | parseBinNode
    public void visit (BinaryNode n) 
    {
        /*
        ALL BINARY NODES ARE HANDLED WITH parseBinaryNode

        */
    }

    public void visit (FactorNode n) 
    {
        System.out.println("FactorNode");

        level++;

        if(look.tag == '-')
        {
            dots();
            n.unary = new UnaryNode();
            n.unary.accept(this);
        }
        else if(look.tag == Tag.ID)
        {
            dots();
            n.id = new IdentifierNode((Word)look);
            n.id.accept(this);  
        }
        else if(look.tag == Tag.NUM)
        {
            dots();
            n.lit = new LiteralNode((Num)look);
            n.lit.accept(this);
        }
        else if(look.tag == '(')
        {
            dots();
            match('(');
            n.expr = new ExpressionNode();
            n.expr.parenthesis = true;
            n.expr.accept(this);
        }
        else if(look.tag == ')')
        {
            level--;
            return;
        }
        else
        {
            error("FactorNode could not recognize the token");
        }

        level--;
    }

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
            n.fact = new FactorNode();
            n.fact.accept(this);

            level--;
        }
        else
        {
            error("UnaryNode there was no '-' before the term");
        }
    }

    public void visit (LiteralNode n) 
    {   
        n.printNode();
        match(Tag.NUM);
    }

    public void visit (IdentifierNode n) 
    {
        n.printNode();
        match(Tag.ID);  
    }
}





