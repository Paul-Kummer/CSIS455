public class ElementC implements ElementNode
{
	ElementNode f = new ElementF();
	
	@Override
	public void accept(ElementVisitor visitor)
	{
		visitor.visit(this);
	}
}