public class Computer implements ComputerPart
{
	ComputerPart[] parts;
	
	public Computer()
	{
		parts = new ComputerPart[] 
		{
			new Mouse(), 
			new Keyboard(), 
			new Monitor(),
			new Desktop(),
			new CPU(),
			new GraphicCard(),
			new PowerSupply(),
			new MotherBoard(),
			new HDD(),
			new SSD()
		};
	}
	
	@Override
	public void accept(ComputerPartVisitor computerPartVisitor)
	{
		System.out.println("parts.length = " + parts.length);
		
		for(int i = 0; i < parts.length; i++)
		{
			parts[i].accept(computerPartVisitor);
		}
		
		computerPartVisitor.visit(this);
	}
}