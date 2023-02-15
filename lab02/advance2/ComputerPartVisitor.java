public interface ComputerPartVisitor
{
	public void visit(Computer computer);
	public void	visit(Mouse mouse);
	public void visit (Keyboard keyboard);
	public void visit (Monitor monitor);
	public void visit(SSD ssd);
	public void	visit(HDD hdd);
	public void visit (MotherBoard motherboard);
	public void visit (PowerSupply powersupply);
	public void visit (GraphicCard gpu);
	public void visit (Desktop desktop);
	public void	visit(CPU cpu);
	public void	visit(Memory mem);
}