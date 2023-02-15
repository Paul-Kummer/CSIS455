public class ModuloNode extends Node
{
	String ident = "%";

	Node left;
	Node right;
	
	public ModuloNode()
	{
		
	}

	public ModuloNode (Node l, Node r)
	{
		this.left = l;
		this.right = r;
	}

	
	@Override
	public void accept(ASTVisitor v)
	{
		v.visit(this);
	}
	
	void printNode()
	{
		System.out.println("Operand: " + ident);
	}
}