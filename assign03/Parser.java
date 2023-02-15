
import java.io.*;
import java.util.*;


class Parser
{
	static int lookahead;
	public boolean notDone = true;

	public Parser() throws IOException 
	{
		lookahead = System.in.read();
	}

	//expr -> expr + term | expr - term | term
	void expr() throws IOException 
	{
		term();

		while(notDone && lookahead != -1) // if exit is entered, end the program
		{
			if( (char)lookahead == '+' ) 
			{
				match('+');
				//System.out.println("matched +");
				term(); 
				System.out.print('+');
			}

			else if( lookahead == '-' ) 
			{
				match('-');
				//System.out.println("matched -");
				term(); 
				System.out.print('-');
			}
			
			else if( lookahead == '(' ) 
			{
				match('(');
				//System.out.println("matched (");
				expr(); 
			}

			else if( lookahead == ')' ) 
			{
				match(')');
				//System.out.println("matched )");
			}

			else if((char)lookahead == ' ' || 
					(char)lookahead == '\t')
			{
				//System.out.println("calling term");
				factor();
			}

			else 
			{
				//System.out.println("lookahead: " +(char)lookahead);
				return;
			}
		}
	}

	void term() throws IOException 
	{
		factor();

		while(notDone && lookahead != -1) //-1 is end
		{
			if( (char)lookahead == '*' ) 
			{
				match('*');
				//System.out.println("matched *");
				factor();
				System.out.print('*');
			}
	
			else if( (char)lookahead == '/' ) 
			{
				match('/');
				//System.out.println("matched /");
				factor(); 
				System.out.print('/');
			}

			else if( (char)lookahead == '%' ) 
			{
				match('%');
				//System.out.println("matched %");
				factor(); 
				System.out.print('%');
			}
	
			else
			{
				return;
			}
		}
	}

	void factor() throws IOException
	{
		//System.out.println("Starting factor");
		if((char)lookahead == 'e')
		{
			String word = "";

			while(Character.isLetterOrDigit((char)lookahead))
			{
				word += (char)lookahead;
				match(lookahead);
			}

			if(word.compareToIgnoreCase("exit") == 0)
			{
				notDone = false;
				return;
			}

			else //the word wasn't exit and is a variable
			{
				System.out.print(word);
			}
		}

		if((char)lookahead == ' ' || (char)lookahead == '\t')
		{
			while((char)lookahead == ' ' || (char)lookahead == '\t')
			{
				lookahead = System.in.read();
			}

			//Now that spaces are removed, read in the value
			factor();
		}

		if( Character.isDigit((char)lookahead) ) 
		{
			String num = "";
			while(Character.isDigit((char)lookahead))
			{
				num += (char)lookahead;
				match(lookahead);
			}
			System.out.print(num); 
		}

		else if(Character.isLetter((char)lookahead))
		{
			String word = "";
			while(Character.isLetterOrDigit((char)lookahead))
			{
				word += (char)lookahead;
				match(lookahead);
			}
			System.out.print(word);
		}

		else if((char)lookahead == '(')
		{
			match('(');
			//System.out.print("(");
			expr();	
		}

		else if((char)lookahead == ')')
		{
			match(')');
			//System.out.print(")");
		}

		else //it is probably an operator or error
		{
			return;
		}
	}

	void match(int t) throws IOException 
	{
		if( (char)lookahead == t ) 
		{
			lookahead = System.in.read();
		}

		else
		{
			throw new Error("syntax error");
		}
	}

	boolean isFinished()
	{
		return notDone;
	}
}
