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
    /*
    int getPrecedence (int op) // UNUSED because parseBinaryNode isn't used
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
            case Tag.BITAND : // and
            case '&' :
                return 7;
            case '^' :
            case Tag.XOR : // or
                return 6;
            case Tag.IOR : // |
                return 5;
            case Tag.AND : // &&
                return 4;
            case Tag.OR : // ||
                return 3;
            case Tag.TERNARY :  // comparison? x: y
                return 2;
            case '=' :
            case Tag.ADDEQ : // +=
            case Tag.MINEQ : // -=
            case Tag.MULEQ : // *=
            case Tag.DIVEQ : // /=
            case Tag.MODEQ : // %=
            case Tag.ANDEQ : // &=
            case Tag.XOREQ : // ^=
            case Tag.RLEQ : // <<=
            case Tag.RREQ : // >>=
            case Tag.LLREQ : // >>>=
                return 1;
            default :
                return -1;
        }
    }

    
    Node parseBinaryNode (Node lhs, int precedence) // Unused because it doesn't fit the grammar
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

        //let the ')' signal an end to the recursion, but it must be cleared after
        //the recursive calls complete. The lhs is returned if ')' is encountered
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
        
                    //while the precedence of the current operator is greater than the previous
                    //operator, move the greater precedence down the AST by moving it from the
                    //right hand side to the left hand side and creating a new right hand side.
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
    }*/
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
        else if(look.tag == Tag.ID) //NOT Part of the Grammar, allows statements and declarations to alternate
		{
            n.stmts = new StatementsNode();
            n.stmts.accept(this);
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
	
    // StatementsNode --> StatementNode StatementsNode DeclarationsNode | DeclarationsNode | null
    public void visit (StatementsNode n) 
    {
		if( look.tag == Tag.ID || 
            look.tag == Tag.DO ||
            look.tag == Tag.WHILE ||
            look.tag == Tag.FOR ||
            look.tag == Tag.IF)
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
        if(look.tag == Tag.BASIC) //NOT Part of the Grammar, allows statements and declarations to alternate
        {
            n.decls = new DeclarationsNode();
            n.decls.accept(this);
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
                n.node.accept(this);
                //System.out.println("Leave Assign: " + look.tag);
                break;
            case Tag.DO :
                //System.out.println("Do Loop: " + look.tag);
                n.node = new DoNode(); // do blockstmt while ( bool );
                n.node.accept(this);
                //System.out.println("Leave Do: " + look.tag);
                break;
            case Tag.WHILE :
                //System.out.println("While Loop: " + look.tag);
                n.node = new WhileNode(); // while ( bool ) blockstmt
                n.node.accept(this);
                //System.out.println("Leave While: " + look.tag);
                break;
            case Tag.FOR :              // for ( AssignmentNode; BinaryNode; ExpressionNode ) BlockStatementNode
                //System.out.println("For Loop: " + look.tag);
                n.node = new ForNode(); 
                n.node.accept(this);
                //System.out.println("Leave For: " + look.tag);
                break;
            case Tag.IF :
                //System.out.println("If Condition: " + look.tag);
                n.node = new IfNode(); // if ( bool ) blockstmt | if ( bool ) blockstmt else blockstmt
                n.node.accept(this);
                //System.out.println("Leave If: " + look.tag);
                break;
            case Tag.BREAK : // break ;
                // What needs to be done for a break ???
                break;
            case '{' :
                //System.out.println("New Block: " + look.tag);
                n.node = new BlockStatementNode(); // blockstmt
                n.node.accept(this);
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

        //left-hand-side
        //if n.left is null, then the value was passed from another node
        if(n.left == null) // id
        {
            level++;
            n.left = new IdentifierNode((Word)look);
            n.left.accept(this);
            level--;
        }
        /*else //It has already been visited
        {
            n.left.accept(this);
        }*/

        //The array, n.left.array needs to be indexed
        while(look.tag == '[') // loc [ bool ] | loc [ bool ] = bool
        {
            match('[');
            // This is not the correct way to do this
            n.index = new BoolNode();
            n.index.accept(this);
            match(']');
        }

        if(look.tag == '=') // loc = bool
        {
            level++;
            dots();
            n.op = look;
            System.out.println("Op: " + n.op);
            match('=');
            level--;
    
            //right-hand-side
            level++;
            n.right = new BoolNode();
            n.right.accept(this);
            level--;

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
        level++;

        match(Tag.DO);

        //execute code in the block
        n.block = new BlockStatementNode();
        n.block.accept(this);

        n.whileNode = new WhileNode(n.block);
        n.whileNode.isDo = true;
        n.whileNode.accept(this);
        match(';');
        
        level--;
    }

    //while ( bool ) block
    public void visit(WhileNode n)
    {
        dots();
        System.out.println("WhileNode");
        level++;

        match(Tag.WHILE);

        match('(');

        n.bool = new BoolNode();
        n.bool.accept(this);

        match(')');

        //Needed for the unparser
        //if n.block is null, it is a While loop. Else it is a Do While loop
        if(n.block == null)
        {
            n.block = new BlockStatementNode();
            n.block.accept(this);
        }

        // execute the block statement
        /*if(n.bool.result != null && n.bool.result.value == "true")
        {
            //if n.block is null, it is a While loop. Else it is a Do While loop
            if(n.block == null)
            {
                n.block = new BlockStatementNode();
                n.block.accept(this);
            }

            while(n.bool.result.value == "true")
            {
                //n.block.rerun();
                //n.bool.rerun();
            }
        }
        else //ignore the entire block statement because the bool is false
        {
            System.out.println("look is: " + look.tag);
            if(look.tag == '{' && look.tag != ';') //This will be true for While statement but not Do While statements
            {
                skipBlock();
            }
        }*/
        level--;
    }

    // ForNode --> for ( decl | id ; bool ; ) block
    //  Will probably need to be the grammer below
    //ForNode --> for ( decl | id ; bool ; stmt;) block
    public void visit(ForNode n)
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

        /*if(n.bool.result.value == "true")
        {
            n.block = new BlockStatementNode();
            n.block.accept(this);
            while(n.bool.result.value == "true")
            {
                //rerun everything in n.block
                //then update the n.bool.value
                //possibly rerun the statement part of if statement
            }
        }
        else //clear the block expression
        {
            skipBlock();
        }*/
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

            match('(');

            n.bool = new BoolNode();
            n.bool.accept(this);
    
            match(')');

            //This is needed so the unparser can rebuild the code
            n.block = new BlockStatementNode();
            n.block.accept(this);

            //if(look.tag == Tag.ELSE)
            //{
            //    n.newIf = new IfNode();
            //    n.newIf.accept(this);
            //}   

            /*if(n.bool.result != null && n.bool.result.value == "true")
            {
                n.block = new BlockStatementNode();
                n.block.accept(this);

                //The bool was true so clear the rest of the else if or else statements
                while(look.tag == Tag.IF || look.tag == Tag.ELSE)
                {
                    skipBlock();
                }
            }
            else
            {
                if(look.tag == Tag.ELSE)
                {
                    n.newIf = new IfNode();
                    n.newIf.accept(this);
                }     
            }*/
        }
        if(look.tag == Tag.ELSE)
        {
            match(Tag.ELSE);

            if(look.tag == Tag.IF)
            {
                n.newIf = new IfNode();
                n.newIf.isElseIf = true;
                n.newIf.accept(this);
            }
            else
            {
                n.newIf = new IfNode();
                n.newIf.block = new BlockStatementNode();
                n.newIf.block.accept(this);
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


    /*
    ExpressionNode  --> ExpressionNode + TermNode | ExpressionNode - TermNode | TermNode
    */
    public void visit (ExpressionNode n)
    {
        dots();
        System.out.println("ExpressionNode");

        level++;
        n.left = new TermNode();
        n.left.accept(this);
        
        if(look.tag == '+')
        {
            //n.right = (TermNode)n.left;
            n.op = look;
            match('+');  
            dots(); 
            System.out.println("Op: " + n.op);
            n.right = new ExpressionNode();
            n.right.accept(this);

            //set the expression value
            //n.result = n.left.result.value + n.right.result.value
        }
        else if(look.tag == '-')
        {
            n.op = look;
            match('-');
            dots(); 
            System.out.println("Op: " + n.op);
            n.right = new ExpressionNode();
            n.right.accept(this);

            //set the expression value
            //n.result = n.left.result.value - n.right.result.value
        }

        //perform the operation on the left and right based on op and set the Token result
        //n.result = Token(n.left + n.right) | Token(n.left - n.right);
        level--;
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

    // UnaryNode --> ! UnaryNode | - UnaryNode | FactorNode
    public void visit (UnaryNode n)
    {
        dots();
        System.out.println("UnaryNode");

        level++;
        if(look.tag == '-')
        {
            //unary minus operator
            n.op = look;
            match('-');
            dots(); 
            System.out.println("Op: " + n.op);
            n.unary = new UnaryNode();
            n.unary.accept(this);
        }
        else if(look.tag == '!')
        {
            //negate operator
            n.op = look;
            match('!');
            dots(); 
            System.out.println("Op: " + n.op);
            n.unary = new UnaryNode();
            n.unary.accept(this);
        }
        else
        {
            n.fact = new FactorNode();
            n.fact.accept(this);
        }

        //perform the operation on the FactorNode
        //n.result = -1 * FactorNode.result | ! FactorNode  "True becomes False, False becomes True"
        level--;
    }

    /*
    FactorNode  --> TrueFalseNode
                | AssignmentNode
                | NumNode
                | RealNode
                | BoolNode
    */
    public void visit (FactorNode n) 
    {
        dots();
        System.out.println("FactorNode");

        level++;
        if(look.tag == Tag.TRUE || look.tag == Tag.FALSE)
        {
            n.trueFalse = new TrueFalseNode((Word)look); // true | false
            n.trueFalse.accept(this);
            n.result = n.trueFalse.v;
        }
        else if(look.tag == Tag.ID)
        {
            //IdentifierNode id = new IdentifierNode((Word)look);
            n.assign = new AssignmentNode(); // loc
            n.assign.accept(this); 
            n.result = n.assign.result;
        }
        else if(look.tag == Tag.NUM)
        {
            n.num = new NumNode((Num)look); // num
            n.num.accept(this);
            n.result = n.num.v;
        }
        else if(look.tag == Tag.REAL)
        {
            n.real = new RealNode((Real)look); // real
            n.real.accept(this);
            n.result = n.real.v;
        }
        else if(look.tag == '(')
        {
            match('(');
            n.bool = new BoolNode();
            n.bool.parenthesis = true;   
            n.bool.accept(this);
            match(')');
        }
        else
        {
            error("FactorNode could not recognize the token");
        }

        level--;
    }

    ///////////////////////////////////////////////
    //These Nodes should all be leaves in the AST//
    ///////////////////////////////////////////////

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





