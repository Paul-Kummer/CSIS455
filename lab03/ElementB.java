public class ElementB implements ElementNode
{
	ElementNode d = new ElementD();
	ElementNode e = new ElementE();
	
	@Override
	public void accept(ElementVisitor visitor)
	{
		visitor.visit(this);
	}
}