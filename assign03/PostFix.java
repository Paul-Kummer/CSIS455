import java.io.*;
import java.util.*;

public class PostFix 
{
	public static void main(String[] args) throws IOException 
	{
		boolean notDone;
		do
		{
			System.out.println("Enter Characters (type 'exit' to quit): ");
			Parser parse = new Parser();
			parse.expr();
			System.out.write('\n');
			notDone = parse.isFinished();
		} while(notDone);	
	}
}
