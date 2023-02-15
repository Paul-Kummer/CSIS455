public class MotherBoard implements ComputerPart
{
	CPU cpu = new CPU();
	HDD hdd = new HDD();
	SSD ssd = new SSD();
	GraphicCard gpu = new GraphicCard();
	Memory mem = new Memory();
	
	@Override
	public void accept(ComputerPartVisitor computerPartVisitor)
	{
		computerPartVisitor.visit(this);
	}
}