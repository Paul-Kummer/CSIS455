public class ElementRoot implements ElementNode
{
	ElementNode root;
	
	public ElementRoot()
	{
		root = new ElementA();
	}
	
	@Override
	public void accept(ElementVisitor visitor)
	{
		visitor.visit(this);
		
		//root.accept(visitor);
	}
}