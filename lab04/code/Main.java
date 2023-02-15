import java.io.*;
import java.util.*;

public class Main 
{
	public static void main(String[] args) throws IOException
	{
		Lexer lexer = new Lexer();
		Parser parser = new Parser(lexer);
	}
}
