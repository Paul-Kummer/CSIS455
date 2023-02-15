package assign5.lexer ;

public class Word extends Token 
{    
    public String lexeme = "" ;
    
    //boolean
    public static final Word True       = new Word ("true",      Tag.TRUE);
    public static final Word False      = new Word ("false",     Tag.FALSE);

    //loops
    public static final Word Do         = new Word ("do",        Tag.DO);
    public static final Word While      = new Word ("while",     Tag.WHILE);
    public static final Word For        = new Word ("for",       Tag.FOR);
    public static final Word Break      = new Word ("break",     Tag.BREAK);
    public static final Word Continue   = new Word ("continue",  Tag.CONTINUE);

    //conditional
    public static final Word If         = new Word ("if",        Tag.IF);
    public static final Word Else       = new Word ("else",      Tag.ELSE);
    public static final Word Switch     = new Word ("switch",    Tag.SWITCH);

    //comparision
    public static final Word And        = new Word ("&&",        Tag.AND);
    public static final Word Or         = new Word ("||",        Tag.OR);
    public static final Word Eq         = new Word ("==",        Tag.EQ);
    public static final Word Ne         = new Word ("!=",        Tag.NE);
    public static final Word Ge         = new Word (">=",        Tag.GE);
    public static final Word Le         = new Word ("<=",        Tag.LE);
    public static final Word Lt         = new Word ("<",         Tag.LT);
    public static final Word Gt         = new Word (">",         Tag.GT);

    //operational
    public static final Word Pp         = new Word ("++",        Tag.POSTINC);
    public static final Word Mm         = new Word ("--",        Tag.POSTDEC);
    public static final Word AddEq      = new Word ("+=",        Tag.ADDEQ);
    public static final Word MinEq      = new Word ("-=",        Tag.MINEQ);
    public static final Word DivEq      = new Word ("/=",        Tag.DIVEQ);
    public static final Word MulEq      = new Word ("*=",        Tag.MULEQ);
    public static final Word ModEq      = new Word ("%=",        Tag.MODEQ);
    public static final Word RotR       = new Word (">>",        Tag.RR);
    public static final Word RotL       = new Word ("<<",        Tag.RL);
    public static final Word LRotR      = new Word (">>>",       Tag.LRR);

    //null
    public static final Word Null       = new Word ("",          Tag.NULL);


    public Word (String s, int tag) 
    {   
        super(tag); 
        lexeme = s;
    }

    public String toString() 
    { 
        return lexeme; 
    }
}
