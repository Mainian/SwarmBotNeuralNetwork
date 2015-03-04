

public abstract class Cell implements iCell 
{
	double value;
	CellType cellType;
	
	@Override
	public double GetValue() 
	{
		return value;
	}

	@Override
	public CellType GetType() 
	{
		return cellType;
	}

}
