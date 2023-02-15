package intercode.parser ;

import intercode.Env;
import intercode.visitor.*;
import intercode.lexer.*;
import intercode.ast.*;
import intercode.parser.*;
import intercode.unparser.*;

import java.io.* ;


public class Parser extends ASTVisitor 
{
    //////////////////////////////////////////////////////////////////////
    //                      Main Parser                                 //
    //////////////////////////////////////////////////////////////////////

    //Declarations for Parser
    public CompilationUnit cu = null ;                  // The start of the program
    public Lexer lexer = null ;                         // contains the tokens from lexemes previously analyzed
    public Token look = null;                           // The current token being analyzed
    public Env top = null;                              // Symbol table that stores variable information <token, IdentifierNode>
    private int level = 0;                              // Keeps track of depth of children nodes
    public BlockStatementNode enclosingBlock = null;    // Keeps track of what block statement the program is in


    //Default Constructor for Parser
    public Parser () 
    {
        cu = new CompilationUnit() ;

        move();

        visit(cu) ;
    }

    //Parameterized Constructor accepting a lexer object, Starts the parsing process
    public Parser (Lexer lexer) 
    { 
        this.lexer = lexer ;
        cu = new CompilationUnit() ;

        move();

        //Brings in reserved words from the lexer to prevent them from being redeclared
        top = lexer.reserved;

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


    /*
    Adds a StatementNode at the current end of the StatementNode linked-list

    ************************ List before Insertion ******************************
    headStmt            stmt0.nextStmt      stmt1.nextStmt      stmt2.nextStmt
    stmt0 -->           stmt1 -->           stmt2 -->           null

    *********************** List after Insertion ********************************
    headStmt            stmt0.nextStmt      stmt1.nextStmt      stmt2.nextStmt      newStmt.nextStmt
    stmt0 -->           stmt1 -->           stmt2 -->           newStmt -->         stmtBeingProcessed

    */
    void addStmt(Node node)
    {
        System.out.println("Adding a statement");

        StatementNode newStmt;                                  //The statement to inject into the list
        StatementNode previousStmt = enclosingBlock.headStmt;   //The statement before the injection
        StatementNode curStmt = enclosingBlock.headStmt;        //current statement in loop

        // Iterate over all StatementNodes in the linked-list
        while(curStmt.node != null)     //if the node is null, the parse statement has not written to it
        {
            previousStmt = curStmt;     //the statement just before the current statement
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


    /*
    Returns Boolean of whether or not a StatementNode currently exists in the 
    StatementNode linked-list
    */
    boolean containsStmt(Node node)
    {
        System.out.println("Contains a statement?");

        // iterate over all StatemetnNodes in the linked-list
        StatementNode curStmt = enclosingBlock.headStmt;
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
    void replaceStmt(Node oldNode, Node newNode)
    {
        System.out.println("Replace a statement");

        // iterate throug all StatemendNodes in the linked-list
        StatementNode curStmt = enclosingBlock.headStmt;
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


    /*
    Prints off all the StatementNodes in the current linked-list. It uses
    unparser to print it off nicely.
    */
    void showBlockStmts()
    {
        stars(20);
        System.out.println("\n\tPRINTING BLOCKS STMTS\n");
        stars(20);
        System.out.println("");

        // Use the unparser to print nicely, The true parameter is only to use a different constructor
        Unparser showNodes = new Unparser(true);
        StatementNode temp = enclosingBlock.headStmt; // Start from the beginning of the linked-list
		while(temp != null)
		{
            showNodes.visit(temp); // Display the current Statement
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

    // print asteriks for displaying purposes
    private void stars(int howMany)
    {
        System.out.print(new String(new char[howMany]).replace('\0', '*'));
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


    /*
    returns boolean value of whether or not a tag is present within a range of tag values
    */
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
    /*
                x = 1 + 2 * 3 + 4

                        BinaryNode
                    /      op(=)     \
    IdentifierNode(x)                BinaryNode
                                    /    op(+)  \
                            NumNode(1)            BinaryNode
                                                /   op(+)   \
                                        BinaryNode             NumNode(4)
                                       /    op(*)  \
                            NumNode(2)           NumNode(3)
    */
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

            // create a binarynode as the right-hand-side if the precedence is higher
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

        if(id.typeNode.array == null)
        {
            error("parseArrayAccessNode: The variable [ " + id.toString() + " ] is not an array");
        }

        level++;
        ArrayDimsNode index = new ArrayDimsNode();
        index.type = id.type;
        index.accept(this);
        level--;

        return new ArrayAccessNode(id, index); // type will be assigned by constructor
    }


    // determine what path a statement is going to take
    public StatementNode parseStatementNode (Boolean isLoop)
    {
        StatementNode stmt = null;

        dots();
        System.out.println("---parseStatementNode--- Tag: " + look.tag);


        switch(look.tag)
        {
            //Tag.NUM and Tag.REAL are not options since it would not make sense IE: 3 = 4 is not correct
            case Tag.ID :
                stmt = new AssignmentNode(); // Word = bool; | Word [ bool ] | Word
                ((AssignmentNode)stmt).accept(this);
                break;
            case Tag.IF :
                stmt = new IfNode(); // if ( bool ) blockstmt | if ( bool ) blockstmt else blockstmt
                ((IfNode)stmt).accept(this);
                ((IfNode)stmt).isConditional = true;
                break;
            case Tag.WHILE :
                stmt = new WhileNode(); // while ( bool ) blockstmt
                ((WhileNode)stmt).accept(this);
                ((WhileNode)stmt).isConditional = true;
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
            // UNUSED
            case Tag.FOR : // for ( AssignmentNode; BinaryNode; ExpressionNode ) BlockStatementNode
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

        top = n.block.sTable == null? top: n.block.sTable;
    }


    // BlockStatementNode -->   { DeclarationNode StatementNode } |
    //                          { DeclarationNode } |
    //                          { StatementNode } |
    //                          {}
    public void visit (BlockStatementNode n) 
    {
        dots();
        System.out.println("BlockStatementNode");

        top = new Env(top); // create a new enviornment that also has the previous environment
        n.sTable = top;
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

        //This will print of the current symbols avaialable in a block
        //top.print(false);

        // restore the previous environment before block start
        top = top.prev == null? top: top.prev; 
        enclosingBlock = n.parent;
    }


    /*
    UNUSED
    */
    public void visit (DeclarationsNode n)
    {

    }


    /*
    DeclarationNode --> TypeNode IdentifierNode ; |
                        TypeNode IdentifierNode = AssignmentNode
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
                    System.out.println("\t---ADDING AN ENTRY TO SYMBOL TABLE--- : " + n.id.w + " | " + n.id);
                    if(n.id instanceof ArrayAccessNode)
                    {
                        top.putArray((ArrayAccessNode)n.id, (ArrayAccessNode)n.id);
                    }
                    else
                    {
                        top.put(n.id.w, n.id);
                    } 
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
    AssignmentNode  --> IdentifierNode parseArrayAccessNode = ExpressionNode ; |
                        IdentifierNode = ExpressionNode ;
    */
    public void visit(AssignmentNode n)
    {
        dots();
        System.out.println("AssignmentNode");

        level++;
        if(n.left == null) // This was used when assignment was used after delcarations. It is not longer used
        {
            n.left = top.get((Word)look);
            if(n.left == null)
            {
                error("AssignmentNode(LHS): the variable or location [ " + (Word)look + " ] has not be declared");
            }
            ((IdentifierNode)n.left).accept(this);
        }
        else // Currently used
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
            if(!(((ArrayAccessNode)n.left).isValidIndex()))
            {
                error("ArrayAccessNode: the index is out of range: [ " + ((ArrayAccessNode)n.left).id.lexeme + "[" + ((ArrayAccessNode)n.left).id.storedVal + "] ]" );
            }
        }

        if(opt('=', Tag.ADDEQ, Tag.DIVEQ, Tag.MINEQ, Tag.MODEQ, Tag.MULEQ))
        {
            level++;
            dots();
            n.op = look;
            System.out.println("Op: " + n.op);
            match(look.tag);

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
                    else if(((IdentifierNode)rhsAssign).storedVal == null && 
                            ((IdentifierNode)rhsAssign).typeNode.array == null)
                    {
                        error("AssignmentNode(RHS): the variable or location [ " + (Word)look + " ] has not been initialized");
                    }

                    rhsAssign.accept(this);

                    if (look.tag == '[')
                    {
                        level--;
                        rhsAssign = parseArrayAccessNode((IdentifierNode)rhsAssign);
                        level++;
                    }
                    else
                    {
                        //rhsAssign = ((IdentifierNode)rhsAssign).storedVal;
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

            if(look.tag == ';') // Assign to a leaf node, Unary expression
            {
                n.right = rhsAssign;
            }
            else // Assign to a binary expression
            { 
                level++;
                n.right = (BinaryNode)parseBinaryNode(rhsAssign, 0);
                level--;
            }

            
            if(n.left instanceof ArrayAccessNode)
            {
                ((ArrayAccessNode)n.left).storedVal = n.right;
                top.putArray(((ArrayAccessNode)n.left), (IdentifierNode)n.left);
                System.out.println("\t---ADDING AN ARRAY ENTRY TO SYMBOL TABLE--- : " + ((ArrayAccessNode)n.left).w + " | " + (ArrayAccessNode)n.left);
            }
            else
            {
                System.out.println("\t---ADDING AN ENTRY TO SYMBOL TABLE--- : " + ((IdentifierNode)n.left).w + " | " + (IdentifierNode)n.left);
                ((IdentifierNode)n.left).storedVal = n.right;
                top.put(((IdentifierNode)n.left).w, (IdentifierNode)n.left);

                if(n.right instanceof NumNode)
                {

                }
                else if(n.right instanceof RealNode)
                {

                }
                else if(n.right instanceof BoolNode)
                {

                }
                else if(n.right instanceof ParenthesesNode)
                {

                }
                else if(n.right instanceof BinaryNode)
                {

                }
                else if(n.right instanceof ArrayAccessNode)
                {

                }
            }
            //top.print(true);

            match(';');
        }
    }


    /*
    ExpressionNode  --> IdentiferNode |
                        BinaryNode
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
            n.bin.isConditional = n.isConditional;
        }
    }


    /*
    BinaryNode  --> ParenthesesNode |
                    IdentifierNode |
                    NumNode |
                    RealNode |
                    TrueNode | FalseNode |
                    BinaryNode
    */
    public void visit (BinaryNode n) 
    {
        //UNUSED
    }


    /*
    ParenthesesNode --> ParenthesesNode |
                        IdentifierNode |
                        IdentifierNode parseArrayAccessNode |
                        NumNode |
                        RealNode |
                        TrueNode | FalseNode |
                        BinaryNode
    */
    public void visit (ParenthesesNode n)
    {
        dots();
        System.out.println("ParenthesesNode");

        match('(');

        level++;
        switch(look.tag)
        {
            case '(' : // Nested ParenthesesNode
                n.expr = new ParenthesesNode();
                ((ParenthesesNode)n.expr).accept(this);
                n.type = n.expr.type;
                break;
            case Tag.ID : // Only and IdentifierNode
                n.expr = top.get((Word)look);
                if(n.expr == null)
                {
                    error("ParenthesesNode: the variable or location [ " + (Word)look + " ] has not be declared");
                }
                ((IdentifierNode)n.expr).accept(this);
                n.type = ((IdentifierNode)n.expr).type;
                
                if(look.tag == '[')
                {
                    //Consider using the n.type = new Array(n.type);
                    level--;
                    n.expr = parseArrayAccessNode((IdentifierNode)n.expr);
                    level++;
                }
                break;
            case Tag.NUM : // Only an Integer
                n.expr = new NumNode((Num)look);
                ((NumNode)n.expr).accept(this);
                n.type = Type.Int;
                break;
            case Tag.REAL : // Only a Float
                n.expr = new RealNode((Real)look);
                ((RealNode)n.expr).accept(this);
                n.type = Type.Float;
                break;
            case Tag.TRUE : // Only True boolean
                n.expr = new TrueNode();
                ((TrueNode)n.expr).accept(this);
                n.type = Type.Bool;
                break;
            case Tag.FALSE : // Only False boolean
                n.expr = new FalseNode();
                ((FalseNode)n.expr).accept(this);
                n.type = Type.Bool;
                break;
            case Tag.BREAK: // Do not allow Break statement
                error("\t---Syntax Error---\nbreak is not allowed outside conditional loops");
            default :
                System.out.println("Null Expression");
                break;
        }
        level--;

        if(look.tag != ')') // Not the end of the expression, create Binary Expression
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
    ArrayTypeNode -->   [ Num ] |
                        [ Num ] ArrayTypeNode
    */
    public void visit (ArrayTypeNode n)
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

            //add its string to previous string, This is for display purpose only
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
    ArrayDimsNode -->   [ ParenthesesNode ] |
                        [ ParenthesesNode ] ArrayDimsNode |
                        [ IdentifierNode ] |
                        [ IdentifierNode ] ArrayDimsNode |
                        [ NumNode ] |
                        [ NumNode ] ArrayDimsNode |
                        [ BinaryNode ] |
                        [ BinaryNode ] ArrayDimsNode
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
                //n.index = ((Num)((ParenthesesNode)index).value).value;
                n.arrayStr = "[" + ((ParenthesesNode)index).value.toString() + "]";
                break;
            case Tag.ID:
                index = top.get((Word)look);
                //System.out.println("index is: " + index.toString());
                if(index == null)
                {
                    error("AssignmentNode(RHS): the variable or location [ " + (Word)look + " ] has not be declared");
                }
                else if(((IdentifierNode)index).storedVal == null && 
                        !(index instanceof ArrayAccessNode))
                {
                    error("AssignmentNode(RHS): the variable or location [ " + (Word)look + " ] has not been initialized");
                }
                else if(((IdentifierNode)index).storedVal instanceof RealNode)
                {
                    error("\t--Syntax Error---\nArray indicies must derive to integers");
                }
                n.type = ((IdentifierNode)index).type;
                n.arrayStr = "[" + ((IdentifierNode)index).storedVal.value + "]";
                //((IdentifierNode)index).storedVal should be a NumNode
                break;
            case Tag.NUM:
                index = new NumNode((Num)look);
                n.type = Type.Int;
                n.index = ((NumNode)index).intValue;
                n.arrayStr = "[" + index.toString() + "]";
                break;
            case Tag.REAL:
                error("\t--Syntax Error---\nArray indicies must derive to integers");
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
        
        n.size = index;

        if (look.tag == '[') // Multidimensional array
        {
            level++;
            n.dim = new ArrayDimsNode();
            n.dim.accept(this);
            level--;
            
            //add its string to previous string, display purpose only
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
    DoNode -->  do BlockStatementNode while ParenthesesNode ; |
                do StatementsNode
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

            // Do not allow break statements in an if statement unless it is in a loop
            if( (enclosingBlock != null && enclosingBlock.isLoop) || 
                (enclosingBlock != null && 
                enclosingBlock.parent != null && 
                enclosingBlock.parent.isLoop))
            {
                n.stmt = parseStatementNode(true);
            }
            else
            {
                n.stmt = parseStatementNode(false);
            }
        }
        if(look.tag == Tag.ELSE)
        {
            match(Tag.ELSE);
            
            // Do not allow break statements in an if statement unless it is in a loop
            if( (enclosingBlock != null && enclosingBlock.isLoop) || 
                (enclosingBlock != null && 
                enclosingBlock.parent != null && 
                enclosingBlock.parent.isLoop))
            {
                n.elseStmt = parseStatementNode(true);
            }
            else
            {
                n.elseStmt = parseStatementNode(false);
            }

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