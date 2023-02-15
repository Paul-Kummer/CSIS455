package assign5.parser ;

import assign5.visitor.*;
import assign5.lexer.*;
import assign5.ast.*;
import assign5.parser.*;
import java.io.* ;

import javax.lang.model.type.ArrayType;

public class Parser extends ASTVisitor 
{
    public CompilationUnit cu = null ;
    public Lexer lexer = null ; 
    public Token look = null;
    private int level = 0;
    public Env top;

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
        //top = new Env(lexer.words);

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

    void skipBlock()
    {
        while(look.tag != '}')
        {
            move();
        }
        match('}');
        //System.out.println("Skip Block End: " + look.tag);
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

/////////////////////// UNUSED: DOES NOT FIT GRAMMAR //////////////////////////////
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
            case ')' :
            case '(' :
            default :
                return -1;
        }
    }

    //time 46:01
    ExpressionNode parseBinaryNode (ExpressionNode lhs, int precedence)
    {
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
                    rhs = new IdentifierNode((Word)look);
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
                default :
                    break;
            }
            level--;
        }

        while(getPrecedence(look.tag) > op)
        {
           rhs = parseBinaryNode(rhs, getPrecedence(look.tag)); 
        }

        lhs = new BinaryNode(lhs, opTok, rhs);

        return lhs;
    }
/////////////////////////////// End UNUSED ////////////////////////////////////  

    // print the dots for displaying the abstract syntax tree (AST)
    private void dots()
	{
		System.out.print(new String(new char[level*4]).replace('\0', '.'));
	}

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

    // BlockStatementNode --> { DeclarationsNode StatementsNode }
    public void visit (BlockStatementNode n) 
    {
        dots();
        System.out.println("BlockStatementNode");

        if(top == null)
        {
            top = new Env();
        }
        else
        {
            top = new Env(top);
        }

        match('{');

		level++;
        n.decls = new DeclarationsNode();
        n.decls.accept(this);
        level--;

		n.stmts = new StatementsNode();
		n.stmts.accept(this);  

        match('}');
        //System.out.println("Block End: " + look.tag);
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
    }

    // DeclarationNode --> TypeNode IdentifierNode ; | TypeNode IdentifierNode AssignmentNode ;
    public void visit (DeclarationNode n)
    {
        dots();
        System.out.println("DeclarationNode");

        level++;
        n.type = new TypeNode(); //should be able to resolve to null
        n.type.accept(this);

        if(look.tag == Tag.ID)
        {
            n.id = new IdentifierNode((Word)look);
            n.id.accept(this);
            n.id.type = n.type;

            //update the symbol table for the token of n.id
            if(n.id != null && top.get(n.id) == null)
            {
                top.put(n.id, new IdentifierNode(Word.Null)); // the hashtable does not allow a null entry
            }
            else
            {
                error("The variable HAS already been declared");
            }
        }
        else
        {
            error("DeclarationNode: only an variable name is accepted");
        }
        level--;

        if(look.tag == '=') //NOT Part of the Grammar, allows assignment after declaration
        {
            level++;
            n.assign = new AssignmentNode(n.id);
            n.assign.accept(this);

            level--;
        }
        if(look.tag == ';')
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

        match(look.tag);

        if(look.tag == '[')
        {
            level++;
            n.array = new ArrayTypeNode(n.basic); // it isn't necessay to pass the type because typenode derives arraytypenode
            n.array.accept(this);
            level--;
        }
    }

    // ArrayTypeNode --> [ Num ]
    public void visit (ArrayTypeNode n)
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
                index = new IdentifierNode((Word)look);
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
	
    // StatementsNode --> StatementNode StatementsNode DeclarationsNode | DeclarationsNode | null
    public void visit (StatementsNode n) 
    {
        if (look.tag != '}')
        {
            dots();
            System.out.println("StatementsNode");

            level++;
            n.stmt = parseStatementNode(n.stmt);
            //n.stmt.accept(this);

            n.stmts = new StatementsNode();
            n.stmts.accept(this);
            level--;
        }
    }

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
                break;
            case Tag.ID :
                n.expr = new IdentifierNode((Word)look);
                ((IdentifierNode)n.expr).accept(this);
                
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
                break;
            case Tag.REAL :
                n.expr = new RealNode((Real)look);
                ((RealNode)n.expr).accept(this);
                break;
            case Tag.TRUE :
                n.expr = new TrueNode();
                ((TrueNode)n.expr).accept(this);
                break;
            case Tag.FALSE :
                n.expr = new FalseNode();
                ((FalseNode)n.expr).accept(this);
                break;
            default :
                break;
        }
        level--;

        if(look.tag != ')')
        {
            n.expr = parseBinaryNode(n.expr, 0);
        }

        match(')');
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
        n.left = new IdentifierNode((Word)look);
        n.left.accept(this);
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
                rhsAssign = new IdentifierNode((Word)look);
                ((IdentifierNode)rhsAssign).accept(this);

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

            if(n.left instanceof IdentifierNode && n.left != null)
            {
                IdentifierNode tmpId = (IdentifierNode)n.left;
                Node tempNode = top.get(tmpId);
                if(tempNode != null)
                {
                    top.put((IdentifierNode)n.left, n.right);
                }
                else
                {
                    error("The variable HAS NOT been declared");
                } 
            }
        }
    }

    
///////////////////////////////////////////////////////////////////////////////////////////////
    //These are all children of stmt             //
    ///////////////////////////////////////////////

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
        dots();
        System.out.println("ForNode");

        level++;

        match(Tag.FOR);

        match('(');

        n.decl = new DeclarationNode();
        n.decl.accept(this);

        if(look.tag == ';')
        {
            match(';');
        }

        n.bool = new BoolNode();
        n.bool.accept(this);

        if(look.tag == ';')
        {
            match(';');
        }

        //This isn't part of the grammar, but might be needed
        n.stmt = new StatementNode();
        n.stmt.accept(this);
        
        if(look.tag == ';')
        {
            match(';');
        }
        
        match(')');

        //This is needed so the unparser can rebuild the code
        n.block = new BlockStatementNode();
        n.block.accept(this);

        level--;
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
        else
        {
            error("IfNode: unrecognized command");
        }

        level--;
    }
/////////////////////////////////// End stmt Children ///////////////////////////////////////

    // BoolNode --> BoolNode || JoinNode | JoinNode
    public void visit (BoolNode n)
    {
        dots();
        System.out.println("BoolNode");

        level++;
        n.left = new JoinNode();
        n.left.accept(this);
        
        if(look.tag == Tag.OR)
        {
            n.op = look;
            //n.right = (JoinNode)n.left;
            match(Tag.OR);  
            dots(); 
            System.out.println("Op: " + n.op);
            n.right = new BoolNode();
            n.right.accept(this);
        }

        //compare the left and right based on op and set the TrueFalseNode to Word.True or Word.False
        //n.result = TrueFalseNode(Word.True | Word.False)

        level--; 
    }

    // JoinNode --> JoinNode && EqualityNode | EqualityNode
    public void visit (JoinNode n)
    {
        dots();
        System.out.println("JoinNode");

        level++;
        n.left = new EqualityNode();
        n.left.accept(this);
        
        if(look.tag == Tag.AND)
        {
            n.op = look;
            //n.right = (EqualityNode)n.left;
            match(Tag.AND);
            dots(); 
            System.out.println("Op: " + n.op);
            n.right = new JoinNode();
            n.right.accept(this);
        }

        //compare the left and right based on op and set the TrueFalseNode to Word.True or Word.False
        //n.result = TrueFalseNode(Word.True | Word.False)

        level--;
    }

    // EqualityNode --> EqualityNode == RelationNode | EqualityNode != RelationNode | RelationNode
    public void visit (EqualityNode n)
    {
        dots();
        System.out.println("EqualityNode");

        level++;
        n.left = new RelationNode();
        n.left.accept(this);
        
        if(look.tag == Tag.NE)
        {
            n.op = look;
            //n.right = (RelationNode)n.left;
            match(Tag.NE);  
            dots(); 
            System.out.println("Op: " + n.op);
            n.right = new EqualityNode();
            n.right.accept(this);
        }
        else if(look.tag == Tag.EQ)
        {
            n.op = look;
            //n.right = (RelationNode)n.left;
            match(Tag.EQ);
            dots(); 
            System.out.println("Op: " + n.op);
            n.right = new EqualityNode();
            n.right.accept(this);
        }

        //compare the left and right based on op and set the TrueFalseNode to Word.True or Word.False
        //n.result = TrueFalseNode(Word.True | Word.False)

        level--;
    }

    /*
    RelationNode    --> ExpressionNode
                    | ExpressionNode < ExpressionNode
                    | ExpressionNode > ExpressionNode
                    | ExpressionNode <= ExpressionNode
                    | ExpressionNode >= ExpressionNdoe
    */
    public void visit (RelationNode n)
    {
        dots();
        System.out.println("RelationNode");

        level++;
        n.left = new ExpressionNode();
        n.left.accept(this);
        
        switch (look.tag) 
        {
            case '<':
            case '>':
            case Tag.LE:
            case Tag.GE:
                n.op = look;
                match(look.tag);
                dots(); 
                System.out.println("Op: " + n.op);
                n.right = new ExpressionNode();
                n.right.accept(this);
                break;
            default:
                //The node is just an ExpressionNode
                break;
        }

        //compare the left and right based on op and set the TrueFalseNode to Word.True or Word.False
        //n.result = TrueFalseNode(Word.True | Word.False)

        level--;
    }

    //Expression: 
    public void visit (ExpressionNode n) 
    {
        dots();
        System.out.println("ExpressionNode");

        level++;
        ExpressionNode rhs_assign = new IdentifierNode();
        rhs_assign.accept(this);
        level--;

        if(look.tag == ';') //the expression is unary
        {
            //n.fact is rhs of assign now
            n.fact = (IdentifierNode)rhs_assign;
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

    // TermNode --> UnaryNode | TermNode * UnaryNode | TermNode / UnaryNode
    public void visit (TermNode n)
    {
        dots();
        System.out.println("TermNode");

        level++;
        n.left = new UnaryNode();
        n.left.accept(this);
        
        if(look.tag == '*')
        {
            n.op = look;
            //n.right = (UnaryNode)n.left;
            match('*');
            dots(); 
            System.out.println("Op: " + n.op);
            n.right = new TermNode();
            n.right.accept(this);
        }
        else if(look.tag == '/')
        {
            n.op = look;
            //n.right = (UnaryNode)n.left;
            match('/');
            dots();
            System.out.println("Op: " + n.op);
            n.right = new TermNode();
            n.right.accept(this);
        }

        //perform the operation on the left and right based on op and set the Token result
        //n.result = Token(n.left * n.right) | Token(n.left / n.right);
        level--;
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
                break;
            case Tag.ID:
                n.left = new IdentifierNode((Word)look);
                n.left.accept(this);

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
                break;
            case Tag.REAL:
                n.left = new RealNode((Real)look);
                n.left.accept(this);
            default:
                break;
        }

        ExpressionNode binary = parseBinaryNode(n.left, 0);
        n.op = binary.op;
        n.right = binary.right;
        level--;
    }

    public void visit (FactorNode n) 
    {
        /*dots();
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

        level--;*/
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

    ///////////////////////////////////////////////
    //These Nodes should all be leaves in the AST//
    ///////////////////////////////////////////////

    public void visit (BreakStmtNode n)
    {
        dots();
        n.printNode();
        match(Tag.BREAK);
        match(';');
    }

    // NumNode --> Num
    public void visit (NumNode n) 
    {
        dots();
        n.printNode();
        match(Tag.NUM);
    }

    // RealNode --> Real
    public void visit (RealNode n) 
    {
        dots();
        n.printNode();
        match(Tag.REAL);
    }

    public void visit (TrueNode n) 
    {
        dots();
        n.printNode();
        match(Tag.TRUE);
    }

    public void visit (FalseNode n) 
    {
        dots();
        n.printNode();
        match(Tag.FALSE);
    }

    // TrueFalseNode --> Word.True | Word.False
    public void visit (TrueFalseNode n)
    {
        dots();
        n.printNode();
        if(n.v.tag == Tag.TRUE)
        {
            match(Tag.TRUE);
        }
        else
        {
            match(Tag.FALSE);
        }
    }

    // IdentifierNode --> Word
    public void visit (IdentifierNode n) 
    {
        dots();
        n.printNode();
        match(Tag.ID);  
    }
}





