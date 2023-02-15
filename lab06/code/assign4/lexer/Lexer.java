package assign4.lexer ;

import java.io.* ;
import java.util.* ;

public class Lexer
{
    public int line = 1 ;
    private char peek = ' ' ;
    private File file = new File("input.txt");
    private BufferedReader br;
    //private Hashtable<String, Word> words = new Hashtable<String, Word>() ;
    private Hashtable words = new Hashtable() ;

    public Lexer () throws IOException, FileNotFoundException
    {
        br = new BufferedReader(new FileReader(file));
        reserve(new Word("true",  Tag.TRUE)) ;
        reserve(new Word("false", Tag.FALSE)) ;
    }

    void reserve (Word w)
    {
        words.put(w.lexeme, w) ;
    }

    void readch() throws IOException
    { 
        //peek = (char)System.in.read() ;
        peek = (char)br.read();
    }

    public Token scan() throws IOException, FileNotFoundException
    {
        //System.out.println("scan() in Lexer") ;

        for ( ; ; readch()) 
        {
            if (peek == ' ' || peek == '\t')
            {
                continue ;
            }
                
            else if (peek == '\n')
            {
                line = line + 1 ;
            }
                
            else
            {
                break ;
            }     
        }

        if (Character.isDigit(peek)) 
        {
            int v = 0 ;

            do 
            {
                v = 10 * v + Character.digit(peek, 10) ;
                readch() ;

            } while (Character.isDigit(peek)) ;

            //System.out.println("number: " + v) ;

            return new Num(v) ;
        }

        else if (Character.isLetter(peek)) 
        {
            StringBuffer b = new StringBuffer() ;

            do 
            {
                b.append(peek) ;
                readch();

            } while (Character.isLetterOrDigit(peek)) ;

            String s = b.toString() ;
            //System.out.println("s: " + s) ;
            Word w = (Word) words.get(s) ;

            if (w != null)
            {
                return w ;
            } 
            
            w = new Word(s, Tag.ID) ;
            words.put(s, w) ;

            //System.out.println("word: " + w.toString()) ;

            return w ;
        }

        Token t = new Token(peek) ; 
        //System.out.println("token: " + t.toString()) ;
        peek = ' ' ;

        return t ;
    }
}
