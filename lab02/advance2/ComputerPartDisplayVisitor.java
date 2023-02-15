public class ComputerPartDisplayVisitor implements ComputerPartVisitor
{
	@Override
	public void visit(Computer computer)
	{
		System.out.println("Displaying Computer");
	}
	
	@Override
	public void visit(Mouse mouse)
	{
		System.out.println("Displaying Mouse");
	}
	
	@Override
	public void visit(Keyboard keyboard)
	{
		System.out.println("Displaying Keyboard");
	}
	
	@Override
	public void visit(Monitor monitor)
	{
		System.out.println("Displaying Monitor");
	}
	
	@Override
	public void visit(CPU cpu)
	{
		System.out.println("........Displaying CPU");
	}
	
	@Override
	public void visit(Desktop desktop)
	{
		System.out.println("Displaying Desktop");
		
		desktop.mb.accept(this);
		desktop.psu.accept(this);
	}
	
	@Override
	public void visit(GraphicCard gpu)
	{
		System.out.println("........Displaying Graphic Card");
	}
	
	@Override
	public void visit(HDD hdd)
	{
		System.out.println("........Displaying Hard Disk Drive");
		
	}
	
	@Override
	public void visit(MotherBoard motherboard)
	{
		System.out.println("....Displaying Motherboard");
		
		motherboard.cpu.accept(this);
		motherboard.mem.accept(this);
		motherboard.hdd.accept(this);
		motherboard.ssd.accept(this);
		motherboard.gpu.accept(this);
	}
	
	@Override
	public void visit(PowerSupply powersupply)
	{
		System.out.println("....Displaying Power Supply");
	}
	
	@Override
	public void visit(SSD ssd)
	{
		System.out.println("........Displaying Solid State Drive");
	}
	
	@Override
	public void visit(Memory mem)
	{
		System.out.println("........Displaying Memory");
	}
}