import java.io.*;
import java.util.*;

class Parser
{
	public Parser(Lexer lex) throws IOException 
	{
		Queue<Token> tokens = new LinkedList<Token>();
		Token tok = lex.scan();
		int lineNum = lex.line;

		
		while(tok.tag != Tag.EOF)
		{
			if(tok != null)
			{
				tokens.add(tok);
			}

			tok = lex.scan();

			//This will preserve original newlines
			while(lex.line > lineNum)
			{
				Node root = new CompilationUnit(tokens);
				root.accept(new ASTVisitor());
				System.out.print("\n\n\t[ New Expression ]\n");

				//lineNum++; //This will add as many lines as there were in the document
				lineNum = lex.line; //This will add only one line

				tokens.clear();
			}
		}
		
		Node root = new CompilationUnit(tokens);
		root.accept(new ASTVisitor());
	}
}
