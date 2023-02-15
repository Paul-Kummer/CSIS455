package assign5.lexer ;

public class Word extends Token 
{    
    public String lexeme = "" ;
    
    //boolean
    public static final Word True       = new Word("true",      Tag.TRUE);
    public static final Word False      = new Word("false",     Tag.FALSE);

    //loops
    public static final Word Do         = new Word("do",        Tag.DO);
    public static final Word While      = new Word("while",     Tag.WHILE);
    public static final Word For        = new Word("for",       Tag.FOR);
    public static final Word Break      = new Word("break",     Tag.BREAK);
    public static final Word Continue   = new Word("continue",  Tag.CONTINUE);

    //conditional
    public static final Word If         = new Word("if",        Tag.IF);
    public static final Word Else       = new Word("else",      Tag.ELSE);
    public static final Word Switch     = new Word("switch",    Tag.SWITCH);

    //comparision
    public static final Word And        = new Word("&&",        Tag.AND);
    public static final Word Or         = new Word ("||",       Tag.OR);
    public static final Word Eq         = new Word ("==",       Tag.EQ);
    public static final Word Ne         = new Word ("!=",       Tag.NE);
    public static final Word Ge         = new Word (">=",       Tag.GE);
    public static final Word Le         = new Word ("<=",       Tag.LE);
    public static final Word Lt         = new Word ("<",        Tag.LT);
    public static final Word Gt         = new Word (">",        Tag.GT);


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
