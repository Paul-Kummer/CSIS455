package assign5.lexer ;

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
        //boolean
        reserve(Word.True);
        reserve(Word.False);

        //loops
        reserve(Word.Do);
        reserve(Word.While);
        reserve(Word.For);
        reserve(Word.Break);
        reserve(Word.Continue);

        //conditional
        reserve(Word.If);
        reserve(Word.Else);
        reserve(Word.Switch);

        //comparison
        reserve(Word.And);
        reserve(Word.Or);
        reserve(Word.Eq);
        reserve(Word.Ne);
        reserve(Word.Ge);
        reserve(Word.Le);
        reserve(Word.Lt);
        reserve(Word.Gt);

        //basic types
        reserve(Type.Int);
        reserve(Type.Float);
        reserve(Type.Char);
        reserve(Type.Bool);
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

    boolean readch(char c) throws IOException
    {
        readch();

        if(peek != c)
        {
            return false;
        }

        peek = ' ';
        return true;
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

        switch(peek)
        {
            case '&' : // logical and
                if(readch('&'))
                {
                    return Word.And;
                }
                else
                {
                    return new Token('&');
                }
            case '|' : // logical or
                if(readch('|'))
                {
                    return Word.Or;
                }
                else
                {
                    return new Token('|');
                }
            case '=' : // comaprision equal
                if(readch('='))
                {
                    return Word.Eq;
                }
                else
                {
                    return new Token('=');
                }
            case '>' : // comparision greater than or equal, or greater than
                if(readch('='))
                {
                    return Word.Ge;
                }
                else
                {
                    //return Word.Gt;
                    return new Token('>');
                }
            case '<' : // comparision less than or equal, or less than
                if(readch('='))
                {
                    return Word.Le;
                }
                else
                {
                    //return Word.Lt;
                    return new Token('<');
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
            if( peek != '.')
            {
                return new Num(v);
            }
            
            float x = v;
            float d = 10;

            while(true)
            {
                readch();

                if(!Character.isDigit(peek))
                {
                    break;
                }

                x = x + Character.digit(peek, 10) / d;
                d = d * 10;
            }

            return new Real(x);
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

            //if the word is in the hashtable return the word
            if (w != null)
            {
                return w ;
            } 
            
            //the word isn't in the table, so add it
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
