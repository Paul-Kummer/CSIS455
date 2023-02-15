public class ElementD implements ElementNode
{
	
	@Override
	public void accept(ElementVisitor visitor)
	{
		visitor.visit(this);
	}
}