public class AssignmentNode extends Node
{
	String ident = "=";

	Node left;
	Node right;
	
	public AssignmentNode()
	{
		
	}
	
	public AssignmentNode (Node l, Node r)
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