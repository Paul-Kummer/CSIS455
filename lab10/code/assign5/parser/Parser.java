package assign5.parser ;

import assign5.Env;
import assign5.visitor.*;
import assign5.lexer.*;
import assign5.ast.*;
import assign5.parser.*;
import java.io.* ;

public class Parser extends ASTVisitor 
{
    //////////////////////////////////////////////////////////////////////
    //                      Main Parser                                 //
    //////////////////////////////////////////////////////////////////////

    public CompilationUnit cu = null ;
    public Lexer lexer = null ; 
    public Token look = null;
    public Env top = null;
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

        //Not using this because the tables store different data
        top = new Env(lexer.reserved);

        visit(cu) ;
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////


    
    

    //////////////////////////////////////////////////////////////////////
    //                      Utility Methods                             //
    //////////////////////////////////////////////////////////////////////

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

        }	
    }


    // print the dots for displaying the abstract syntax tree (AST)
    private void dots()
	{
		System.out.print(new String(new char[level*4]).replace('\0', '.'));
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
            case ')' :
            case '(' :
                return 15;
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
            default :
                return -1;
        }
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////








    //////////////////////////////////////////////////////////////////////
    //                      Parsers                                     //
    //////////////////////////////////////////////////////////////////////

    //time 46:01
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
                        exit(1);
                    }
                    ((IdentifierNode)rhs).accept(this);
                    
                    if(look.tag == '[')
                    {
                        level--;
                        rhs = parseArrayAccessNode((IdentifierNode)rhs.expr);
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

        level++;
        ExpressionNode index = new ArrayDimsNode();
        index.accept(this);
        level--;

        return new ArrayAccessNode(id, index);
    }


    // determine what path a statment is going to take
    public StatementNode parseStatementNode (StatementNode stmt)
    {
        dots();
        System.out.println("---parseStatementNode---");

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
                ((IfNode)stmt).accept(this);
                break;
            case Tag.WHILE :
                stmt = new WhileNode(); // while ( bool ) blockstmt
                ((WhileNode)stmt).accept(this);
                break;
            case Tag.DO :
                stmt = new DoNode(); // do blockstmt while ( bool );
                ((DoNode)stmt).accept(this);
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
                stmt = new BlockStatementNode(); // blockstmt
                ((BlockStatementNode)stmt).accept(this);
                break;
            default :
                break;
            //The break and continue tags should be handled in the statment
        }
        
        return stmt;
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////








    //////////////////////////////////////////////////////////////////////
    //                        Visit Methods                             //
    //////////////////////////////////////////////////////////////////////

    // program --> BlockStatementNode
    public void visit (CompilationUnit n) 
    {
        System.out.println("CompilationUnit");
        
        level++;
        n.block = new BlockStatementNode();
        n.block.accept(this);
        level--;
    }


    //Timestamp (23:26)
    // BlockStatementNode --> { DeclarationsNode StatementsNode }
    public void visit (BlockStatementNode n) 
    {
        dots();
        System.out.println("BlockStatementNode");

        n.sTable = top; // preserves the current enviornment to be restored at the end of the block
        top = new Env(top); // create a new enviornment that also has the previous environment

        match('{');

		level++;
        n.decls = new DeclarationsNode();
        n.decls.accept(this);
        
		//n.stmts = new StatementsNode();
		//n.stmts.accept(this);
        level--;  

        match('}');

        top = n.sTable; // restores the previous environment before block start
    }


    // DeclarationsNode --> DeclarationNode DeclarationsNode StatementsNode | StatementsNode | null
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


    // StatementsNode --> StatementNode StatementsNode DeclarationsNode | DeclarationsNode | null
    public void visit (StatementsNode n) 
    {
        if (look.tag != '}' && look.tag != Tag.EOF)
        {
            dots();
            System.out.println("StatementsNode");

            level++;
            n.stmt = parseStatementNode(n.stmt);

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


    // DeclarationNode --> TypeNode IdentifierNode ; | TypeNode IdentifierNode AssignmentNode ;
    public void visit (DeclarationNode n)
    {
        dots();
        System.out.println("DeclarationNode");

        level++;
        n.typeNode = new TypeNode(); //should be able to resolve to null
        n.typeNode.accept(this);

        if(look.tag == Tag.ID)
        {
            n.id = new IdentifierNode((Word)look, (Type)(n.typeNode.basic));
            n.id.accept(this);

            //update the symbol table for the token of n.id
            if(n.id.w != null && top.get(n.id.w) == null)
            {
                top.put(n.id.w, n.id); // the hashtable does not allow a null entry
            }
            else
            {
                error("DeclarationNode: The variable [ " + (Word)look + " ] HAS already been declared");
            }
        }
        else
        {
            error("DeclarationNode: only a variable name or location is accepted");
        }
        level--;

        if(look.tag == '=') //NOT Part of the Grammar, allows assignment after declaration
        {
            level++;
            n.assign = new AssignmentNode(n.id);
            n.assign.accept(this);

            level--;
        }
        else
        {
            match(';'); 
        }
    }


    // TypeNode --> TypeNode ArrayTypeNode | Type
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
        else if(look.toString() == "boolean" || look.toString() == "bool")
        {
            n.basic = Type.Bool;
        }
        else if(look.toString() == "char")
        {
            n.basic = Type.Char;
        }
        else
        {
            error("TypeNode: could not match type");
        }

        match(Tag.BASIC);

        if(look.tag == '[')
        {
            level++;
            n.array = new ArrayTypeNode(n.basic); // it isn't necessay to pass the type because typenode derives arraytypenode
            n.array.accept(this);
            level--;
        }
    }


    // ArrayTypeNode --> [ Num ]
    public void visit (ArrayTypeNode n) // <--------------------------------------------Modify this for storing type too
    {
        dots();
        System.out.println("ArrayTypeNode");

        match('[');

        n.size = (Num)look; //This is Num token, DO NOT ALLOW Real

        dots();
        System.out.println("Array Dimension: " + n.size);

        match(Tag.NUM);
        match(']');

        //check if it is a multidimensional array
        if(look.tag == '[')
        {
            level++;
            n.typeNode = new ArrayTypeNode();
            n.typeNode.accept(this);
            level--;
        }
    }


    //Uses parseArrayAccessNode instead
    public void visit (ArrayAccessNode n)
    {
        dots();
        System.out.println("ArrayAccessNode");
    }


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
                break;
            case Tag.ID:
                index = top.get((Word)look);
                if(n.left == null)
                {
                    error("ArrayDimsNode(index): the variable or location [ " + (Word)look + " ] has not be declared");
                    exit(1);
                }
                break;
            case Tag.NUM:
                index = new NumNode((Num)look);
                break;
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

        n.size = index;

        if (look.tag == '[')
        {
            level++;
            n.dim = new ArrayDimsNode();
            n.dim.accept(this);
            level--;
        }
    }


    /*
        StatementNode --> AssignmentNode ;       "this can derive to a FactorNode"
                        | DoNode
                        | WhileNode
                        | ForNode
                        | IfNode
                        | break
                        | BlockStatementNode
    */
    //UNUSED: parseStatementNode is used instead
    public void visit (StatementNode n) 
    {  
        dots();
        System.out.println("StatementNode");

        level++;
        switch(look.tag)
        {
            //Tag.NUM and Tag.REAL are not options since it would not make sense
            //to change a terminal literal's value
            case Tag.ID :
                //System.out.println("New Assign: " + look.tag);
                n.node = new AssignmentNode(); // Word = bool; | Word [ bool ] | Word
                ((AssignmentNode)n.node).accept(this);
                //System.out.println("Leave Assign: " + look.tag);
                break;
            case Tag.DO :
                //System.out.println("Do Loop: " + look.tag);
                n.node = new DoNode(); // do blockstmt while ( bool );
                ((DoNode)n.node).accept(this);
                //System.out.println("Leave Do: " + look.tag);
                break;
            case Tag.WHILE :
                //System.out.println("While Loop: " + look.tag);
                n.node = new WhileNode(); // while ( bool ) blockstmt
                ((WhileNode)n.node).accept(this);
                //System.out.println("Leave While: " + look.tag);
                break;
            case Tag.FOR :              // for ( AssignmentNode; BinaryNode; ExpressionNode ) BlockStatementNode
                //System.out.println("For Loop: " + look.tag);
                n.node = new ForNode(); 
                ((ForNode)n.node).accept(this);
                //System.out.println("Leave For: " + look.tag);
                break;
            case Tag.IF :
                //System.out.println("If Condition: " + look.tag);
                n.node = new IfNode(); // if ( bool ) blockstmt | if ( bool ) blockstmt else blockstmt
                ((IfNode)n.node).accept(this);
                //System.out.println("Leave If: " + look.tag);
                break;
            case Tag.BREAK : // break ;
                n.node = new BreakStmtNode();
                ((BreakStmtNode)n.node).accept(this);
                // What needs to be done for a break ???
                break;
            case '{' :
                //System.out.println("New Block: " + look.tag);
                n.node = new BlockStatementNode(); // blockstmt
                ((BlockStatementNode)n.node).accept(this);
                //System.out.println("Leave block: " + look.tag);
                break;
            default :
                break;
            //The break and continue tags should be handled in the statment
        } 
        level--;
    }
        

    //Parenthesized expressions
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
                if(n.left == null)
                {
                    error("ParenthesesNode: the variable or location [ " + (Word)look + " ] has not be declared");
                    exit(1);
                }
                n.type = n.left.type;
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
            default :
                break;
        }
        level--;

        if(look.tag != ')')
        {
            n.expr = parseBinaryNode(n.expr, 0);
            n.type = ((BinaryNode)n.expr).left.type;
        }

        match(')');
    }


    //do block While ( bool )
    public void visit(DoNode n)
    {
        dots();
        System.out.println("DoNode");

        match(Tag.DO);

        if(look.tag == '{')
        {
            level++;
            n.stmt = new BlockStatementNode();
            n.stmt.accept(this);
            level--;
        }  
        else
        {
            n.stmt = parseStatementNode(n.stmt);
        }

        match(Tag.WHILE);

        level++;
        n.cond = new ParenthesesNode();
        n.cond.accept(this);   
        level--;

        match(';');
    }


    //while ( bool ) block
    public void visit(WhileNode n)
    {
        dots();
        System.out.println("WhileNode");

        match(Tag.WHILE);

        level++;
        n.cond = new ParenthesesNode();
        n.cond.accept(this);
        level--;

        if(look.tag == '{')
        {
            level++;
            n.stmt = new BlockStatementNode();
            n.stmt.accept(this);
            level--;
        }
        else
        {
            n.stmt = parseStatementNode(n.stmt);
        }
    }


    // ForNode --> for ( decl | id ; bool ; ) block
    //  Will probably need to be the grammer below
    //ForNode --> for ( decl | id ; bool ; stmt;) block
    public void visit (ForNode n)
    {

    }


    // IfNode --> if ( bool ) blockstmt | if ( bool ) blockstmt else blockstmt
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

            if(look.tag == '{')
            {
                n.stmt = new BlockStatementNode();
                n.stmt.accept(this);
            }
            else
            {
                n.stmt = parseStatementNode(n.stmt);
            }
        }
        if(look.tag == Tag.ELSE)
        {
            n.isElif = true;
            match(Tag.ELSE);

            if(look.tag == '{')
            {
                n.elseStmt = new BlockStatementNode();
                n.elseStmt.accept(this);
            }
            else
            {
                n.elseStmt = parseStatementNode(n.elseStmt);
            }
        }

        level--;
    }


    /* This is loc in the grammar
    AssignmentNode --> IdentifierNode = BoolNode ;
                    | IdentifierNode [ BoolNode ]
                    |  IdentifierNode [ BoolNode ] = BoolNode
                    | IdentifierNode
    */
    public void visit(AssignmentNode n)
    {
        dots();
        System.out.println("AssignmentNode");

        level++;
        n.left = n.left == null? top.get((Word)look): top.get(((IdentifierNode)n.left).w);
        if(n.left == null)
        {
            error("AssignmentNode(LHS): the variable or location [ " + (Word)look + " ] has not be declared");
            exit(1);
        }
        ((IdentifierNode)n.left).accept(this);
        level--;

        //The array, n.left.array needs to be indexed
        if (look.tag == '[') // loc [ bool ] | loc [ bool ] = bool
        {
            n.left = parseArrayAccessNode((IdentifierNode)n.left);
        }

        if(look.tag == '=') // loc = bool
        {
            level++;
            dots();
            n.op = look;
            System.out.println("Op: " + n.op);
            match('=');

            //System.out.println("Look is: " + look );
    
            ExpressionNode rhsAssign = null;

            if(look.tag == '(')
            {
                rhsAssign = new ParenthesesNode();
                ((ParenthesesNode)rhsAssign).accept(this);
            }
            else if (look.tag == Tag.ID)
            {
                rhsAssign = top.get((Word)look);
                if(rhsAssign == null)
                {
                    error("AssignmentNode(RHS): the variable or location [ " + (Word)look + " ] has not be declared");
                    exit(1);
                }

                rhsAssign.accept(this);

                if (look.tag == '[')
                {
                    level--;
                    rhsAssign = parseArrayAccessNode((IdentifierNode)rhsAssign);
                    level++;
                }
            }
            else if (look.tag == Tag.NUM)
            {
                rhsAssign = new NumNode((Num)look);
                ((NumNode)rhsAssign).accept(this);
            }
            else if (look.tag == Tag.REAL)
            {
                rhsAssign = new RealNode((Real)look);
                ((RealNode)rhsAssign).accept(this);
            }
            else if (look.tag == Tag.TRUE)
            {
                rhsAssign = new TrueNode();
                ((TrueNode)rhsAssign).accept(this);
            }
            else if (look.tag == Tag.FALSE)
            {
                rhsAssign = new FalseNode();
                ((FalseNode)rhsAssign).accept(this);
            }
            level--;


            if(look.tag == ';')
            {
                n.right = rhsAssign;
            }
            else
            {
                //dots();
                //System.out.println("Op: " + n.op);
                
                level++;
                n.right = (BinaryNode)parseBinaryNode(rhsAssign, 0);
                level--;
            }

            match(';');
        }
    }


    //Expression: 
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
                exit(1);
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
            if(look.tag == ')') 
            {
                move(); //clear the end of expression character
            }
        }
    }


    public void visit (BinaryNode n) 
    {
        dots();
        System.out.println("BinaryNode");

        level++;
        switch (look.tag) 
        {
            case '(':
                n.left = new ParenthesesNode();
                n.left.accept(this);
                n.type = n.left.type;
                break;
            case Tag.ID:
                n.left = top.get((Word)look);
                if(n.fact == null)
                {
                    error("BinaryNode: the variable or location [ " + (Word)look + " ] has not be declared");
                    exit(1);
                }
                n.left.accept(this);
                n.type = n.left.type;

                if (look.tag == '[')
                {
                    level--;
                    n.left = parseArrayAccessNode((IdentifierNode)n.left);
                    level++;
                }
                break;
            case Tag.NUM:
                n.left = new NumNode((Num)look);
                n.left.accept(this);
                n.type = Type.Int;
                break;
            case Tag.REAL:
                n.left = new RealNode((Real)look);
                n.left.accept(this);
                n.type = Type.Float;
                break;
            case Tag.TRUE:
                n.left = new TrueNode();
                n.left.accept(this);
                n.type = Type.Bool;
                break;
            case Tag.FALSE:
                n.left = new FalseNode();
                n.left.accept(this);
                n.type = Type.Bool;
                break;
            default:
                break;
        }

        ExpressionNode binary = parseBinaryNode(n.left, 0);
        n.op = binary.op;
        n.right = binary.right;
        level--;
    }
    
/////////////////////////////////////////////////////////////////////////////////////////////////////////////








    //////////////////////////////////////////////////////////////////////
    //                    leaf Nodes                                    //
    //////////////////////////////////////////////////////////////////////
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