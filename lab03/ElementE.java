public class ElementE implements ElementNode
{
	
	@Override
	public void accept(ElementVisitor visitor)
	{
		visitor.visit(this);
	}
}