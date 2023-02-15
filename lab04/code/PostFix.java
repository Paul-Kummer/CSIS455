import java.io.*;
import java.util.*;

public class PostFix 
{
	public static void main(String[] args) throws IOException 
	{
		System.out.println("Enter Characters: ");
		Parser2 parse = new Parser2();
		parse.expr(); 
		System.out.write('\n');
	}
}
