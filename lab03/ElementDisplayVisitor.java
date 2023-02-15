public class ElementDisplayVisitor implements ElementVisitor
{
	int level = 0;
	@Override
	public void visit(ElementRoot r)
	{
		System.out.println("Element Root: " + level);
		
		level++;
		r.root.accept(this);
		level--;
	}
	
	@Override
	public void visit(ElementA a)
	{
		System.out.println("Element A: " + level);
		
		level++;
		a.b.accept(this);
		a.c.accept(this);
		level --;
	}
	
	@Override
	public void visit(ElementB b)
	{
		System.out.println("Element B: " + level);
		
		level++;
		b.d.accept(this);
		b.e.accept(this);
		level--;
	}
	
	@Override
	public void visit(ElementC c)
	{
		System.out.println("Element C: " + level);
		
		level++;
		c.f.accept(this);
		level--;
	}
	
	@Override
	public void visit(ElementD d)
	{
		System.out.println("Element D: " + level);
	}
	
	@Override
	public void visit(ElementE e)
	{
		System.out.println("Element E: " + level);
	}
	
	@Override
	public void visit(ElementF f)
	{
		System.out.println("Element F: " + level);
	}
}