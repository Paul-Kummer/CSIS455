import java.io.*;
import java.util.*;

public class Lexer 
{
	public int line = 1;
	private char peek = ' ';
	
	private FileInputStream in;
	private BufferedInputStream bin;
	
	private Hashtable<String, Word> words = new Hashtable<String, Word>();
	
	public Lexer()
	{
		reserve(new Word("true", Tag.TRUE));
		reserve(new Word("false", Tag.FALSE));
		
		setupIOStream();
	}
	
	void reserve(Word w)
	{
		words.put(w.lexeme, w);
	}
	
	void setupIOStream()
	{
		try
		{
			in = new FileInputStream("input.txt");
			bin = new BufferedInputStream(in);
		}

		catch(IOException e)
		{
			System.out.println("IOException: ");
		}
	}
		
	void readch() throws IOException
	{
		peek = (char) bin.read();
	}
	
	public Token scan() throws IOException
	{
		for( ; ; readch())
		{
			if(peek == ' ' || peek == '\t')
			{
				continue;
			}
			
			else if(peek == '\n')
			{
				line++;
			}
			
			else
			{
				break;
			}
		}
			
		if(Character.isDigit(peek))
		{
			int v = 0;
			
			do
			{
				v = 10 * v + Character.digit(peek, 10);
				readch();
			} while(Character.isDigit(peek));
			
			return new Num(v);
		}
		
		if(Character.isLetter(peek))
		{
			StringBuffer b = new StringBuffer();
			
			do
			{
				b.append(peek);
				readch();
			} while(Character.isLetterOrDigit(peek));
			
			String s = b.toString();
			Word w = words.get(s);
			
			if (w != null)
			{
				return w;
			}
			
			w = new Word(s, Tag.ID);
			words.put(s, w);
			
			return w;
		}
			
		Token t = new Token(peek);
		peek = ' ';
		
		return t;
	}
}