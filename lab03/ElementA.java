public class ElementA implements ElementNode
{
	ElementNode b = new ElementB();
	ElementNode c = new ElementC();
	
	@Override
	public void accept(ElementVisitor visitor)
	{
		visitor.visit(this);
	}
}