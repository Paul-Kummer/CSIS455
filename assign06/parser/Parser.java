package assign6.parser ;

import assign6.Env;
import assign6.visitor.*;
import assign6.lexer.*;
import assign6.ast.*;
import assign6.parser.*;
import assign6.unparser.*;

import java.io.* ;

public class Parser extends ASTVisitor 
{
    //////////////////////////////////////////////////////////////////////
    //                      Main Parser                                 //
    //////////////////////////////////////////////////////////////////////

    //Declarations for Parser
    public CompilationUnit cu = null ;
    public Lexer lexer = null ; 
    public Token look = null;
    public Env top = null;
    private int level = 0;
    public BlockStatementNode enclosingBlock = null;


    //Default Constructor for Parser
    public Parser () 
    {
        cu = new CompilationUnit() ;

        move();

        visit(cu) ;
    }

    //Parameterized Constructor accepting a lexer object
    public Parser (Lexer lexer) 
    { 
        this.lexer = lexer ;
        cu = new CompilationUnit() ;

        move();

        //Not using this because the tables store different data
        top = new Env(lexer.reserved);

        visit(cu) ;
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////   
//                                                                                                         //
//                                                                                                         //
//    //////////////////////////////////////////////////////////////////////                               //
//    //                      Utility Methods                             //                               //
//    //////////////////////////////////////////////////////////////////////                               //
//                                                                                                         //
//                                                                                                         //
/////////////////////////////////////////////////////////////////////////////////////////////////////////////   


    void addStmt(Node node)
    {
        System.out.println("Adding a statement");

        StatementNode newStmt; //The statement to inject into the list
        StatementNode previousStmt = enclosingBlock.headStmt; //The statement before the injection
        StatementNode curStmt = enclosingBlock.headStmt; //current statement in loop
        while(curStmt.node != null) //if the node is null, the parse statement has not written to it
        {
            previousStmt = curStmt; //the statement just before the current statement
            curStmt = curStmt.nextStmt; //advance the statement
        }

        newStmt = new StatementNode(enclosingBlock.headStmt, node);

        if(enclosingBlock.headStmt == null) //injecting as the head statement
        {
            enclosingBlock.headStmt = newStmt;
            newStmt.head = newStmt;
        }
        else //injecting anywhere else
        {
            previousStmt.nextStmt = newStmt; 
        }

        newStmt.nextStmt = curStmt;
    }


    boolean containsStmt(Node node)
    {
        System.out.println("Contains a statement?");

        StatementNode curStmt = enclosingBlock.headStmt;
        while(curStmt != null)
        {
            if(curStmt.node == node)
            {
                return true;
            }

            curStmt = curStmt.nextStmt;
        }

        return false;
    }


    void replaceStmt(Node oldNode, Node newNode)
    {
        System.out.println("Replace a statement");

        StatementNode curStmt = enclosingBlock.headStmt;
        while(curStmt != null)
        {
            if(curStmt.node == oldNode)
            {
                curStmt.node = newNode;
                break;
            }

            curStmt = curStmt.nextStmt;
        }
    }


    int indexOfStmt(Node node)
    {
        System.out.println("Index of a statement");

        int position = 0;
        StatementNode curStmt = enclosingBlock.headStmt;
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


    void showBlockStmts()
    {
        stars(20);
        System.out.println("\n\tPRINTING BLOCKS STMTS\n");
        stars(20);
        System.out.println("");
        Unparser showNodes = new Unparser(true);
        StatementNode temp = enclosingBlock.headStmt;
		while(temp != null)
		{
            showNodes.visit(temp);
            //System.out.println(temp.toString());
			temp = temp.nextStmt;
		}
        System.out.println("");
        stars(20);
        System.out.println("");
    }


    //Skip over a token because it is not important
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


    //Throw a nice error message and exit the program
    void error (String s) 
    {
        System.out.println("\n(near line: " + lexer.line + " ) " + s) ;
        exit(1);
    }


    //This will compare an expected tag.id with an actual tag.id and throw an error if it is different
    void match (int t)
    {
        String errorMessage;

        try 
        {
            if(look.tag == Tag.EOF) //must check for EOF because the EOF isn't expected
            {
                errorMessage = "\t---Syntax Error---\nexpected closing symbol of ';' or '}'";
                error(errorMessage);
            }
            else if(look.tag != t)
            {
                switch (t) 
                {
                    case Tag.BREAK:
                        errorMessage = "\t---Syntax Error--- \nmissing keyword 'break'";
                        break;
                    case Tag.NUM:
                        errorMessage = "\t---Syntax Error---\nmissing an integer value";
                        break;
                    case Tag.REAL:
                        errorMessage = "\t---Syntax Error---\nmissing a float value";
                        break;
                    case Tag.TRUE:
                        errorMessage = "\t---Syntax Error---\nmissing keyword 'true'";
                        break;
                    case Tag.FALSE:
                        errorMessage = "\t---Syntax Error---\nmissing keyword 'false'";
                        break;
                    case Tag.ID:
                        errorMessage = "\t---Syntax Error---\nmissing a variable name or identifier";
                        break;
                    default:
                        errorMessage = "\t---Syntax Error--- \nTag mismatch: current tag [ '" + (char)look.tag + "' : " + look.tag + " ] expected tag [ '" + (char)t + "' : " + t + " ]\n";
                        break;
                }

                error(errorMessage);	
            }

            move();
        }
        catch(Error e) 
        {
            error(e.toString());
        }	
    }


    // print the dots for displaying the abstract syntax tree (AST)
    private void dots()
	{
		System.out.print(new String(new char[level*4]).replace('\0', '.'));
	}

    // print the dots for displaying the abstract syntax tree (AST)
    private void stars(int howMany)
    {
        System.out.print(new String(new char[level*howMany]).replace('\0', '*'));
    }


    // exit the program nicely
    void exit (int value)
    {
        System.exit(value);
    }


    // returns the precedence value from order of operations for an operator
    int getPrecedence (int op)
    {
        switch(op)
        {
            case Tag.POSTINC : // x++
            case Tag.POSTDEC : // x--
                return 14;
            case '*' :
            case '/' :
            case '%' :
                return 13;
            case '+' :
            case '-' :
                return 12;
            case Tag.RL : // <<
            case Tag.RR : // >>
            case Tag.LRR : // >>>
                return 11;
            case '<' :
            case '>' :
            case Tag.GE : // >=
            case Tag.LE : // <=
                return 9;
            case Tag.EQ : // ==
            case Tag.NE : // !=
                return 8;
            //case Tag.BITAND : // and
            //case '&' :
            //    return 7;
            //case '^' :
            //case Tag.XOR : // or
            //    return 6;
            //case Tag.IOR : // |
            //    return 5;
            case Tag.AND : // &&
                return 4;
            case Tag.OR : // ||
                return 3;
            //case Tag.TERNARY :  // comparison? x: y
            //    return 2;
            case '=' :
            case Tag.ADDEQ : // +=
            case Tag.MINEQ : // -=
            case Tag.MULEQ : // *=
            case Tag.DIVEQ : // /=
            case Tag.MODEQ : // %=
            //case Tag.ANDEQ : // &=
            //case Tag.XOREQ : // ^=
            //case Tag.RLEQ : // <<=
            //case Tag.RREQ : // >>=
            //case Tag.LLREQ : // >>>=
                return 1;
            case Tag.BREAK:
                error("\t---Syntax Error---\nbreak is not an operator");
            case ')' : // DO NOT consume these tokens in parser, which is why precdence is set low
            case '(' :
            default :
                return -1;
        }
    }

    private boolean opt(int... tags)
    {
        for(int tag : tags)
        {
            if(look.tag == tag)
            {
                return true;
            }
        }

        return false;
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////   
//                                                                                                         //
//                                                                                                         //
//    //////////////////////////////////////////////////////////////////////                               //
//    //                      Parsers                                     //                               //
//    //////////////////////////////////////////////////////////////////////                               //
//                                                                                                         //
//                                                                                                         //
/////////////////////////////////////////////////////////////////////////////////////////////////////////////  


    // Create an expression of binary nodes and rotate values depending on the operator precedence
    ExpressionNode parseBinaryNode (ExpressionNode lhs, int precedence)
    {
        dots();
        System.out.println("---parseBinaryNode---");

        // If the current op's precedence is higher than that of
        // the previous, then keep traversing down to create a new
        // BinaryNode for a binary expression with higher level precedence
        // Otherwise, create a new BinaryNode for the current lhs and rhs
        Token opTok = null;
        ExpressionNode rhs = null;
        int op = 0;

        //let the ')' signal an end to the recursion, but it must be cleared after
        //the recursive calls complete. The lhs is returned if ')' is encountered
        while(getPrecedence(look.tag) >= precedence) 
        {
            opTok = look;
            if (look.tag == '=')
            {
                error("There cannot be another equal symbol in an assignment node");
            }

            op = getPrecedence(look.tag);

            level++;
            dots();
            System.out.println("Op: " + look);
            level--;

            move();

            rhs = null;

            level++;
            switch(look.tag)
            {
                case '(' :
                    rhs = new ParenthesesNode();
                    ((ParenthesesNode)rhs).accept(this);
                    break;
                case Tag.ID :
                    rhs = top.get((Word)look);
                    if(rhs == null)
                    {
                        error("parseBinaryNode(RHS): the variable or location [ " + (Word)look + " ] has not be declared");
                    }
                    ((IdentifierNode)rhs).accept(this);

                    if(look.tag == '[')
                    {
                        level--;
                        rhs = parseArrayAccessNode((IdentifierNode)rhs);
                        level++;
                    }
                    break;
                case Tag.NUM :
                    rhs = new NumNode((Num)look);
                    ((NumNode)rhs).accept(this);
                    break;
                case Tag.REAL :
                    rhs = new RealNode((Real)look);
                    ((RealNode)rhs).accept(this);
                    break;
                case Tag.TRUE :
                    rhs = new TrueNode();
                    ((TrueNode)rhs).accept(this);
                    break;
                case Tag.FALSE :
                    rhs = new FalseNode();
                    ((FalseNode)rhs).accept(this);
                    break;
                case Tag.BREAK:
                    error("\t---Syntax Error---\nbreak is not allowed in an expression");
                default :
                    break;
            }
            level--;

            //System.out.println("op = " + op);
            //System.out.println("token_op = " + opTok);
            //System.out.println("next_op = " + getPrecedence(look.tag));

            while(getPrecedence(look.tag) > op)
            {
                rhs = parseBinaryNode(rhs, getPrecedence(look.tag)); 
            }

            lhs = new BinaryNode(lhs, opTok, rhs);
        }

        return lhs;
    }


    // create a multidimensional array if there are many dimensions
    public ExpressionNode parseArrayAccessNode (IdentifierNode id)
    {
        dots();
        System.out.println("---parseArrayAccessNode---");
        
        id = top.get(id.w);

        //System.out.println("I'm before instance check: " + (char)look.tag);
        if(id.typeNode.array == null)
        {
            error("parseArrayAccessNode: The variable [ " + id.toString() + " ] is not an array");
        }

        level++;
        //System.out.println("I'm before dims node: " + (char)look.tag);
        ArrayDimsNode index = new ArrayDimsNode();
        //System.out.println("I'm before id type: " + (char)look.tag);
        index.type = id.type;
        index.accept(this);
        level--;

        return new ArrayAccessNode(id, index); // type will be assigned by constructor
    }


    // determine what path a statment is going to take
    public StatementNode parseStatementNode (Boolean isLoop)
    {
        StatementNode stmt = null;

        dots();
        System.out.println("---parseStatementNode--- Tag: " + look.tag);


        switch(look.tag)
        {
            //Tag.NUM and Tag.REAL are not options since it would not make sense
            //to change a terminal literal's value
            case Tag.ID :
                stmt = new AssignmentNode(); // Word = bool; | Word [ bool ] | Word
                ((AssignmentNode)stmt).accept(this);
                break;
            case Tag.IF :
                stmt = new IfNode(); // if ( bool ) blockstmt | if ( bool ) blockstmt else blockstmt
                ((IfNode)stmt).isConditional = true;
                ((IfNode)stmt).accept(this);
                break;
            case Tag.WHILE :
                stmt = new WhileNode(); // while ( bool ) blockstmt
                ((WhileNode)stmt).isConditional = true;
                ((WhileNode)stmt).accept(this);
                break;
            case Tag.DO :
                stmt = new DoNode(); // do blockstmt while ( bool );
                ((DoNode)stmt).accept(this);
                ((DoNode)stmt).isConditional = true;
                break;
            case Tag.BREAK : // break ;
                stmt = new BreakStmtNode();
                ((BreakStmtNode)stmt).accept(this);
                break;
            case Tag.FOR :              // for ( AssignmentNode; BinaryNode; ExpressionNode ) BlockStatementNode
                stmt = new ForNode(); 
                ((ForNode)stmt).accept(this);
                break;
            case '{' :
                stmt = new BlockStatementNode(enclosingBlock); // blockstmt
                ((BlockStatementNode)stmt).isLoop = isLoop;
                ((BlockStatementNode)stmt).accept(this);
                break;
            case ';': // prevent infinite loops and for statments that do nothing, POTENTIAL ERROR IN CODE: considering making a warning
                match(';');
                stmt = null;
                break;
            default :
                error("Unable to parse Statment: token is [ " + (char)look.tag + " | " + look.tag + " ]");
                break;
            //The break and continue tags should be handled in the statment
        }
        
        return stmt;
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////   
//                                                                                                         //
//                                                                                                         //
//    //////////////////////////////////////////////////////////////////////                               //
//    //                      Visit  Nodes                                //                               //
//    //////////////////////////////////////////////////////////////////////                               //
//                                                                                                         //
//                                                                                                         //
/////////////////////////////////////////////////////////////////////////////////////////////////////////////  


    // program --> BlockStatementNode
    public void visit (CompilationUnit n) 
    {
        System.out.println("CompilationUnit");
        
        level++;
        n.block = new BlockStatementNode();
        n.block.accept(this);
        level--;
    }


    // BlockStatementNode --> { DeclarationsNode }
    public void visit (BlockStatementNode n) 
    {
        dots();
        System.out.println("BlockStatementNode");

        n.sTable = top; // preserves the current enviornment to be restored at the end of the block
        top = new Env(top); // create a new enviornment that also has the previous environment
        enclosingBlock = n;

        match('{');

		level++;
        n.decls = new DeclarationNode(n.decls);
        n.headDecl = n.decls;
        while(n.decls != null)
        {
            n.decls.accept(this);  
            n.decls = n.decls.nextDecl;  
        }
        
		n.stmts = new StatementNode(n.stmts);
        n.headStmt = n.stmts;
        while(n.stmts != null)
        {
            n.stmts.accept(this);

            //Stop break statement from happening outside loops
            if( (!(n.isLoop || (n.parent != null && n.parent.isLoop))) && 
                ((StatementNode)n.stmts).node instanceof BreakStmtNode)
            {
                error("\t---Syntax Error--- \nbreak statements are only allowed in loops");
            }

            n.stmts = n.stmts.nextStmt;   
        }
        level--;  

        match('}');

        // restore the previous environment before block start
        top = n.sTable; 
        enclosingBlock = n.parent;
    }


    /*
    UNUSED
    */
    public void visit (DeclarationsNode n)
    {
        //UNUSED
    }


    /*
    DeclarationNode --> TypeNode IdentifierNode ;
                        | TypeNode IdentifierNode = AssignmentNode
    */
    public void visit (DeclarationNode n)
    {
        if ( look.tag == Tag.BASIC )
        {
            dots();
            System.out.println("DeclarationNode");
    
            level++;
            n.typeNode = new TypeNode(); //should be able to resolve to null
            n.typeNode.accept(this);
    
            if(look.tag == Tag.ID) // look is an IdentifierNode
            {
                n.id = new IdentifierNode((Word)look, (TypeNode)n.typeNode);
                n.id.accept(this);
    
                //update the symbol table for the token of n.id
                //The symbol table will not allow a null entry to veryify id isn't null
                if(n.id.w != null && top.get(n.id.w) == null)
                {
                    top.put(n.id.w, n.id);
                }
                else
                {
                    error("DeclarationNode: The variable [ " + look + " ] HAS already been declared");
                }
            }
            else
            {
                error("DeclarationNode: only a variable name or location is accepted");
            }
            level--;
    
            match(';');

            n.nextDecl = new DeclarationNode(n.head);
        }
    }

    /*
    UNUSED
    */
    public void visit (StatementsNode n) 
    {
        //UNUSED
    }


    /*
        StatementNode --> AssignmentNode ;
                        | DoNode
                        | WhileNode
                        | ForNode
                        | IfNode
                        | BreakStmtNode
                        | BlockStatementNode
                        | ε
    */
    public void visit (StatementNode n) 
    { 
        if (opt(Tag.ID, Tag.IF, Tag.WHILE, Tag.DO, Tag.BREAK, Tag.FOR))
        {
            level++;
            n.node = parseStatementNode(false);
            level--;

            n.nextStmt = new StatementNode(n.head);
        }
    }
      
    
    /*
    TypeNode    --> Basic 
                | Basic ArrayTypeNode 
    Basic       --> Type
    Type        --> Float | Int | Bool | Char
    */
    public void visit (TypeNode n) //This should be linked to an IdentifierNode in the top Env
    {
        dots();
        System.out.println("TypeNode: " + look.toString());

        // Could use (look == Tag.BASIC), but this is more descriptive
        if( look == Type.Float ||
            look == Type.Int ||
            look == Type.Bool ||
            look == Type.Char)
        {
            n.basic = (Type)look;
        }
        else
        {
            error("TypeNode: could not match type");
        }

        match(Tag.BASIC);

        if(look.tag == '[')
        {
            level++;
            n.array = new ArrayTypeNode(n.basic);
            n.array.accept(this);
            level--;
        }
    }


    /* 
    AssignmentNode  --> IdentifierNode parseArrayAccessNode = ExpressionNode ;
                    | IdentifierNode = ExpressionNode ;
    */
    public void visit(AssignmentNode n)
    {
        dots();
        System.out.println("AssignmentNode");

        level++;
        if(n.left == null)
        {
            n.left = top.get((Word)look);
            if(n.left == null)
            {
                error("AssignmentNode(LHS): the variable or location [ " + (Word)look + " ] has not be declared");
            }
            ((IdentifierNode)n.left).accept(this);
        }
        else
        {
            n.left = top.get(((IdentifierNode)n.left).w);
            if(n.left == null)
            {
                error("AssignmentNode(LHS): the variable or location [ " + (Word)look + " ] has not be declared");
            }
            //DO NOT ACCEPT, because this came from a declartion statement and was already accepted
            //If this is accepted, the next token will not be '=', which is expected
        }
        level--;

        //The array, n.left.array needs to be indexed
        if (look.tag == '[') // loc [ bool ] | loc [ bool ] = bool
        {
            n.left = parseArrayAccessNode((IdentifierNode)n.left);
            // n.left is now and ArrayAccessNode
            // n.left.index is a ArrayDimNode
            // n.left.index.toString() should print [n][n] for each dimension and its index
            
            //n.left = top.getValue();
        }

        if(look.tag == '=')
        {
            level++;
            dots();
            n.op = look;
            System.out.println("Op: " + n.op);
            match('=');

            ExpressionNode rhsAssign = null;

            switch (look.tag) 
            {
                case '(':
                    rhsAssign = new ParenthesesNode();
                    ((ParenthesesNode)rhsAssign).accept(this);
                    break;
                case Tag.ID:
                    rhsAssign = top.get((Word)look);
                    if(rhsAssign == null)
                    {
                        error("AssignmentNode(RHS): the variable or location [ " + (Word)look + " ] has not be declared");
                    }

                    rhsAssign.accept(this);

                    if (look.tag == '[')
                    {
                        level--;
                        rhsAssign = parseArrayAccessNode((IdentifierNode)rhsAssign);
                        level++;
                    }
                    break;
                case Tag.NUM:
                    rhsAssign = new NumNode((Num)look);
                    ((NumNode)rhsAssign).accept(this);
                    break;
                case Tag.REAL:
                    rhsAssign = new RealNode((Real)look);
                    ((RealNode)rhsAssign).accept(this);
                    break;
                case Tag.TRUE:
                    rhsAssign = new TrueNode();
                    ((TrueNode)rhsAssign).accept(this);
                    break;
                case Tag.FALSE:
                    rhsAssign = new FalseNode();
                    ((FalseNode)rhsAssign).accept(this);
                    break;
                case Tag.BREAK:
                    error("\t---Syntax Error---\nbreak is not allowed outside conditional loops");
                default:
                    break;
            }
            level--;

            if(look.tag == ';')
            {
                n.right = rhsAssign;
            }
            else
            { 
                level++;
                n.right = (BinaryNode)parseBinaryNode(rhsAssign, 0);
                level--;
            }

            match(';');
        }
    }


    /*
    ExpressionNode  --> IdentiferNode
                    | BinaryNode
    */
    public void visit (ExpressionNode n) 
    {
        dots();
        System.out.println("ExpressionNode");

        level++;
        ExpressionNode rhs_assign = new IdentifierNode((Word)look);
        rhs_assign.accept(this);
        level--;

        if(look.tag == ';') //the expression is unary
        {
            n.fact = top.get((Word)look);
            if(n.fact == null)
            {
                error("ExpressionNode: the variable or location [ " + (Word)look + " ] has not be declared");
            }
        }
        else //the expression is binary
        {
            level++;
            dots();
            System.out.println("operator: " + look);
            n.op = look;
            level--;

            //n.bin is rhs of assign now
            n.bin = (BinaryNode)parseBinaryNode(rhs_assign, 0); //0 is the default level for operator precedence

            //This character must remain in parseBinaryNode to force the recursion to stop
            //May Not be needed
            /*
            if(look.tag == ')') 
            {
                move(); //clear the end of expression character
            }
            */
        }
    }


    /*
    BinaryNode  --> ParenthesesNode
                | IdentifierNode
                | NumNode
                | RealNode
                | TrueNode | FalseNode
                | BinaryNode
    */
    public void visit (BinaryNode n) 
    {
        //UNUSED
    }


    /*
    ParenthesesNode --> ParenthesesNode
                    | IdentifierNode
                    | IdentifierNode parseArrayAccessNode
                    | NumNode
                    | RealNode
                    | TrueNode | FalseNode
                    | BinaryNode
    */
    public void visit (ParenthesesNode n)
    {
        dots();
        System.out.println("ParenthesesNode");

        match('(');

        level++;
        switch(look.tag)
        {
            case '(' :
                n.expr = new ParenthesesNode();
                ((ParenthesesNode)n.expr).accept(this);
                n.type = n.expr.type;
                break;
            case Tag.ID :
                n.expr = top.get((Word)look);
                if(n.expr == null)
                {
                    error("ParenthesesNode: the variable or location [ " + (Word)look + " ] has not be declared");
                }
                ((IdentifierNode)n.expr).accept(this);
                n.type = ((IdentifierNode)n.expr).type;

                if(look.tag == '[')
                {
                    level--;
                    n.expr = parseArrayAccessNode((IdentifierNode)n.expr);
                    level++;
                }
                break;
            case Tag.NUM :
                n.expr = new NumNode((Num)look);
                ((NumNode)n.expr).accept(this);
                n.type = Type.Int;
                break;
            case Tag.REAL :
                n.expr = new RealNode((Real)look);
                ((RealNode)n.expr).accept(this);
                n.type = Type.Float;
                break;
            case Tag.TRUE :
                n.expr = new TrueNode();
                ((TrueNode)n.expr).accept(this);
                n.type = Type.Bool;
                break;
            case Tag.FALSE :
                n.expr = new FalseNode();
                ((FalseNode)n.expr).accept(this);
                n.type = Type.Bool;
                break;
            case Tag.BREAK:
                error("\t---Syntax Error---\nbreak is not allowed outside conditional loops");
            default :
                System.out.println("Null Expression");
                break;
        }
        level--;

        if(look.tag != ')')
        {
            n.expr = parseBinaryNode(n.expr, 0);

            // if n.expr is only an identifier node, the type is already assigned
            // casting to a BinaryNode will cause an error
            if(n.expr instanceof BinaryNode)
            {
                n.type = ((BinaryNode)n.expr).left.type;
            }
        }

        match(')');
    }
    
/////////////////////////////////////////////////////////////////////////////////////////////////////////////   
//                                                                                                         //
//                                                                                                         //
//    //////////////////////////////////////////////////////////////////////                               //
//    //                      Array Nodes                                 //                               //
//    //////////////////////////////////////////////////////////////////////                               //
//                                                                                                         //
//                                                                                                         //
///////////////////////////////////////////////////////////////////////////////////////////////////////////// 


    /*
    ArrayTypeNode --> [ Num ]
                    | [ Num ] ArrayTypeNode
    */
    public void visit (ArrayTypeNode n) // <--------------------------------------------Modify this for storing type too
    {
        dots();
        System.out.println("ArrayTypeNode");

        match('[');
        

        if(!(look instanceof Num))
        {
            error("ArrayTypeNode: array sizes must be integers");
        }
        n.size = (Num)look; //This is Num token, DO NOT ALLOW Real

        dots();
        System.out.println("Array Dimension: " + n.size);

        match(Tag.NUM);
        match(']');

        n.arrayStr = "[" + n.size + "]";

        //check if it is a multidimensional array
        if(look.tag == '[')
        {
            level++;
            n.typeNode = new ArrayTypeNode(n.type);
            n.typeNode.accept(this);
            level--;

            //add its string to previous string
            n.arrayStr = n.arrayStr + ((ArrayTypeNode)n.typeNode).arrayStr;
        }

        n.init(); 
    }


    /*
    ArrayAccessNode --> epsilon
    */
    public void visit (ArrayAccessNode n) 
    {
        // The parsing function takes place of this
        dots();
        System.out.println("ArrayAccessNode");
    }


    /*
    ArrayDimsNode --> [ ParenthesesNode ]
                    | [ ParenthesesNode ] ArrayDimsNode
                    | [ IdentifierNode ]
                    | [ IdentifierNode ] ArrayDimsNode
                    | [ NumNode ] 
                    | [ NumNode ] ArrayDimsNode
                    | [ BinaryNode ] 
                    | [ BinaryNode ] ArrayDimsNode
    */
    public void visit (ArrayDimsNode n)
    {
        dots();
        System.out.println("ArrayDimsNode");

        match('[');

        ExpressionNode index = null;

        level++;
        switch (look.tag) 
        {
            case '(':
                index = new ParenthesesNode();
                n.type = ((ParenthesesNode)index).type;
                break;
            case Tag.ID:
                index = top.get((Word)look);
                if(index == null)
                {
                    error("ArrayDimsNode(index): the variable or location [ " + (Word)look + " ] has not be declared");
                }
                n.type = ((IdentifierNode)index).type;
                break;
            case Tag.NUM:
                index = new NumNode((Num)look);
                n.type = Type.Int;
                break;
            case Tag.BREAK:
                error("\t---Syntax Error---\nbreak is not allowed outside conditional loops");
            default:
                break;
        }
        index.accept(this);
        level--;

        if (look.tag != ']')
        {
            level++;
            index = parseBinaryNode(index, 0);
            level--;
        }

        match(']');

        n.arrayStr = "[" + index.toString() + "]";

        n.size = index;

        if (look.tag == '[')
        {
            level++;
            n.dim = new ArrayDimsNode();
            n.dim.accept(this);
            level--;
            
            //add its string to previous string
            n.arrayStr = n.arrayStr + n.dim.arrayStr;
        }
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////   
//                                                                                                         //
//                                                                                                         //
//    //////////////////////////////////////////////////////////////////////                               //
//    //              Loops and Conditional Operations                    //                               //
//    //////////////////////////////////////////////////////////////////////                               //
//                                                                                                         //
//                                                                                                         //
///////////////////////////////////////////////////////////////////////////////////////////////////////////// 


    /*
    DoNode --> do BlockStatementNode while ParenthesesNode ;
            || do StatementsNode
    */
    public void visit(DoNode n)
    {
        dots();
        System.out.println("DoNode");

        match(Tag.DO);

        n.stmt = parseStatementNode(true);

        //This will allow the block statement be be exited with a break statement
        if(n.stmt instanceof BlockStatementNode)
        {
            ((BlockStatementNode)n.stmt).isLoop = true;
        }

        match(Tag.WHILE);

        level++;
        n.cond = new ParenthesesNode();
        n.cond.accept(this);   
        level--;

        match(';');
    }


    /*
    WhileNode   --> while ParenthesesNode StatementNode
    */
    public void visit(WhileNode n)
    {
        dots();
        System.out.println("WhileNode");

        match(Tag.WHILE);

        level++;
        n.cond = new ParenthesesNode();
        n.cond.accept(this);
        level--;

        //Check that operator is a relational operator

        n.stmt = parseStatementNode(true);
    }


    //UNUSED
    public void visit (ForNode n)
    {

    }


    /*
    IfNode   --> if ParenthesesNode StatementNode
    */
    public void visit(IfNode n)
    {
        dots();
        System.out.println("IfNode");

        level++;

        if(look.tag == Tag.IF)
        {
            match(Tag.IF);

            n.cond = new ParenthesesNode();
            n.cond.accept(this);

            n.stmt = parseStatementNode(false);
        }
        if(look.tag == Tag.ELSE)
        {
            match(Tag.ELSE);
            
            n.elseStmt = parseStatementNode(false);
            if(n.elseStmt instanceof IfNode)
            {
                ((IfNode)n.elseStmt).isElif = true;
            }
        }

        level--;
    }
    
/////////////////////////////////////////////////////////////////////////////////////////////////////////////   
//                                                                                                         //
//                                                                                                         //
//    //////////////////////////////////////////////////////////////////////                               //
//    //                      Leaf Nodes                                  //                               //
//    //////////////////////////////////////////////////////////////////////                               //
//                                                                                                         //
//                                                                                                         //
/////////////////////////////////////////////////////////////////////////////////////////////////////////////  
 
    /*
    All Error checking is performed by the match method
    */    


    public void visit (BreakStmtNode n) // value is Word.Break
    {
        dots();
        match(Tag.BREAK);
        n.printNode();
        match(';');
    }


    // NumNode --> Num
    public void visit (NumNode n) // Pass a Num into constructor of NumNode
    {
        dots();
        match(Tag.NUM);
        n.printNode();
    }


    // RealNode --> Real
    public void visit (RealNode n) // Pass a Real into constructor of RealNode
    {
        dots();
        match(Tag.REAL);
        n.printNode();
    }


    //This could replace TrueNode and FalseNode
    public void visit (BoolNode n)
    {
        dots();
        if(look.tag == Tag.TRUE)
        {
            match(Tag.TRUE);
        }
        else
        {
            match(Tag.FALSE);
        }
        n.printNode();
    }


    public void visit (TrueNode n) // value is Word.True
    {
        dots();
        match(Tag.TRUE);
        n.printNode();
    }


    public void visit (FalseNode n) // value is Word.False
    {
        dots();
        match(Tag.FALSE);
        n.printNode();
    }


    // IdentifierNode --> Word
    public void visit (IdentifierNode n) // Pass a word into constructor of IdentifierNode
    {
        dots();
        match(Tag.ID);
        n.printNode();
    }
}