public class ElementDemo
{
	public static void main(String[] args)
	{
		ElementNode root = new ElementRoot();
		root.accept(new ElementDisplayVisitor());
	}
}