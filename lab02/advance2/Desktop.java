public class Desktop implements ComputerPart
{
	MotherBoard mb = new MotherBoard();
	PowerSupply psu = new PowerSupply();
	
	
	@Override
	public void accept(ComputerPartVisitor computerPartVisitor)
	{
		computerPartVisitor.visit(this);
	}
}