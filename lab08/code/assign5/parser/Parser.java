package assign5.parser ;

import assign5.visitor.*;
import assign5.lexer.*;
import assign5.ast.*;
import assign5.parser.*;
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
                return 15;
            //case Tag.POSTINC :
            //case Tag.POSTDEC :
                //return 14;
            case '*' :
            case '/' :
            case '%' :
                return 13;
            case '+' :
            case '-' :
                return 12;
            case Tag.RL :
            case Tag.RR :
            case Tag.LRR :
                return 11;
            case '<' :
            case '>' :
            case Tag.GE :
            case Tag.LE :
                return 9;
            case Tag.EQ :
            case Tag.NE :
                return 8;
            case Tag.BITAND :
            case '&' :
                return 7;
            case '^' :
            case Tag.XOR :
                return 6;
            case Tag.IOR :
                return 5;
            case Tag.AND :
                return 4;
            case Tag.OR :
                return 3;
            case Tag.TERNARY :
                return 2;
            case '=' :
            case Tag.ADDEQ :
            case Tag.MINEQ :
            case Tag.MULEQ :
            case Tag.DIVEQ :
            case Tag.MODEQ :
            case Tag.ANDEQ :
            case Tag.XOREQ :
            case Tag.RLEQ :
            case Tag.RREQ :
            case Tag.LLREQ :
                return 1;
            default :
                return -1;
        }
    }

    
    Node parseBinaryNode (Node lhs, int precedence) //impossible to build a correct AST using this while it reads
    {
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
        n.block = new BlockStatementNode();
        n.block.accept(this);
        level--;
    }

    //Block Statement: child of compilation unit
    public void visit (BlockStatementNode n) 
    {
        dots();
        System.out.println("BlockStatementNode");

        match('{');

		level++;
        n.decls = new DeclarationsNode();
        n.decls.accept(this);
        level--;

		n.stmts = new StatementsNode();
		n.stmts.accept(this);
        

        match('}');
    }

    public void visit (DeclarationsNode n)
    {
        //check if the lookahead is a type, indicating a declaration vs a statement
        //      ---Examples---
        //Declaration:  int x;
        //Statement:    x = 2 + x;
        //after declaration, if lookahead is '=' instead of ';' then do a statement
        if(look.tag == Tag.BASIC)
        {
            dots();
            System.out.println("Declarations");

            level++;
            n.decl = new DeclarationNode();
            n.decl.accept(this);
            level--;

            n.decls = new DeclarationsNode();
            n.decls.accept(this);
        }
        //This will make the program more flexible, but doesn't follow the grammar
        if(look.tag == Tag.ID)
		{
            n.stmts = new StatementsNode();
            n.stmts.accept(this);
        }
    }

    public void visit (DeclarationNode n)
    {
        dots();
        System.out.println("DeclarationNode");

        level++;
        n.type = new TypeNode();
        n.type.accept(this);

        n.id = new FactorNode(); //This should be a IdentifierNode to follow the grammar
        n.id.accept(this);
        level--;

        //This will make the program more flexible, but doesn't follow the grammar
        if(look.tag == '=') //check if the declaration is assigned too
        {
            level++;
            n.assign = new AssignmentNode(n.id);
            n.assign.accept(this);
            level--;
        }
 
         match(';');
    }
	
	//Statement: child of block
    public void visit (StatementsNode n) 
    {
		if(look.tag == Tag.ID)
		{
            dots();
            System.out.println("Statements");

            level++;
            n.stmt = new StatementNode();
            n.stmt.accept(this);
            
            n.stmts = new StatementsNode();
            n.stmts.accept(this);
            level--;
		}
        if(look.tag == Tag.BASIC)
        {
            n.decls = new DeclarationsNode();
            n.decls.accept(this);
        }
    }

    public void visit (StatementNode n) 
    {  
        dots();
        System.out.println("StatementNode");

        //check if look.tag is ID | if | while | do | break | block | continue | ...
        level++;
        switch(look.tag)
        {
            //Tag.NUM and Tag.REAL are not options since it would not make sense
            //to change a terminal literal's value
            case Tag.ID :
                n.assign = new AssignmentNode();
                n.assign.accept(this);
                break;
            case Tag.DO :
            case Tag.WHILE :
            case Tag.FOR :
            case Tag.BREAK :
            case Tag.CONTINUE :
                //n.node = new LoopNode();
                //n.node.accept(this);
                break;
            case Tag.IF :
            case Tag.ELSE :
            case Tag.SWITCH :
                //n.node = new ConditionalNode();
                //n.node.accept(this);
                break;
            case '{' :
                n.node = new BlockStatementNode();
                n.node.accept(this);
                break;
            default :
                break;
        } 
        level--;

        match(';');
    }

    //Assignment: child of stmts
    public void visit(AssignmentNode n)
    {
        dots();
        System.out.println("AssignmentNode");

        //left-hand-side
        //factor node will derive IdentifierNode.
        //It will also accept some nodes that it shouldn't
        if(n.left == null)
        {
            level++;
            n.left = new FactorNode();
            n.left.accept(this);
            level--;
        }

        level++;
        dots();
        n.op = look;
        System.out.println("Op: =");
        match('=');
        level--;

        //right-hand-side
        level++;
        n.right = new ExpressionNode();
        n.right.accept(this);
        level--;
    }

    //Expression: 
    public void visit (ExpressionNode n) 
    {
        dots();
        System.out.println("ExpressionNode");

        level++;
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

            //This character must remain in parseBinaryNode to force the recursion to stop
            if(look.tag == ')') 
            {
                move(); //clear the end of expression character
            }
        }
    }

    //Binary:
    public void visit (BinaryNode n) 
    {
        dots();
        System.out.println("BinaryNode");

        /*
        ALL BINARY NODES ARE HANDLED WITH parseBinaryNode
        */
    }

    public void visit (FactorNode n) 
    {
        dots();
        System.out.println("FactorNode");

        level++;
        if(look.tag == '-')
        {
            n.unary = new UnaryNode();
            n.unary.accept(this);
        }
        else if(look.tag == Tag.ID)
        {
            n.id = new IdentifierNode((Word)look);
            n.id.accept(this);  
        }
        else if(look.tag == Tag.NUM)
        {
            n.num = new NumNode((Num)look);
            n.num.accept(this);
        }
        else if(look.tag == Tag.REAL)
        {
            n.real = new RealNode((Real)look);
            n.real.accept(this);
        }
        else if(look.tag == '{')
        {
            n.block = new BlockStatementNode();
            n.block.accept(this);
        }
        else if(look.tag == '(')
        {
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
        dots();
        System.out.println("UnaryNode");

        if(look.tag == '-')
        {
            //unary minus operator
            match('-');

            //first operand
            level++;
            n.fact = new FactorNode();
            n.fact.accept(this);
            level--;
        }
        else
        {
            error("UnaryNode there was no '-' before the term");
        }
    }

    public void visit (TypeNode n)
    {
        dots();
        System.out.println("TypeNode: " + look.toString());

        if(look.toString() == "int")
        {
            n.basic = Type.Int;
        }
        else if(look.toString() == "float")
        {
            n.basic = Type.Float;
        }
        else if(look.toString() == "boolean")
        {
            n.basic = Type.Bool;
        }
        else if(look.toString() == "char")
        {
            n.basic = Type.Char;
        }

        match(look.tag);

        if(look.tag == '[')
        {
            level++;
            n.array = new ArrayTypeNode();
            n.array.accept(this);
            level--;
        }
    }

    public void visit (ArrayTypeNode n)
    {
        dots();
        System.out.println("ArrayTypeNode");

        match('[');

        n.size = ((Num)look).value;

        dots();
        System.out.println("Array Dimension: " + n.size);

        match(Tag.NUM);
        match(']');

        //check if it is a multidimensional array
        if(look.tag == '[')
        {
            level++;
            n.type = new ArrayTypeNode();
            n.type.accept(this);
            level--;
        }
    }

    public void visit (NumNode n) 
    {
        dots();
        n.printNode();
        match(Tag.NUM);
    }

    public void visit (RealNode n) 
    {
        dots();
        n.printNode();
        match(Tag.REAL);
    }

    public void visit (IdentifierNode n) 
    {
        dots();
        n.printNode();
        match(Tag.ID);  
    }
}





