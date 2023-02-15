package assign5.lexer ;

public class Word extends Token {
    
    public String lexeme = "" ;
   
    public static final Word True   = new Word("true",  Tag.TRUE) ;
    public static final Word False  = new Word("false", Tag.FALSE) ;
    public static final Word If     = new Word("if", Tag.IF);
    public static final Word Else   = new Word("else", Tag.ELSE);
    public static final Word Do     = new Word("do", Tag.DO);
    public static final Word While  = new Word("while", Tag.WHILE);
    public static final Word Break  = new Word("break", Tag.BREAK);

    public Word (String s, int tag) { 
        
        super(tag) ; 
        lexeme = s ;
    }

    public String toString() { 
        return lexeme ; 
    }
}
