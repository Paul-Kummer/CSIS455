import java.util.*;

public class Node
{
	Token tok = null;

	public Node()
	{
	}

	public Node(Token t)
	{
		tok = t;
	}

	public void accept(ASTVisitor v)
	{
		System.out.println("Node: " + tok.toString());
	}
}