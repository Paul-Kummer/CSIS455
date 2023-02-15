package assign6.lexer ;

public class Token 
{
    public final int tag;
    
    public Token()
    {
        this.tag = 0;
    }

    public Token (int t) 
    {

        this.tag = t ;
    }

    public String toString() {

        return "" + (char)tag ;
    }
}
