

import java.util.ArrayList;

public class OutputCell extends WeightedCell
{
	public OutputCell(ArrayList<? extends Cell> prev)
	{
		super(prev);
		this.cellType = CellType.Output;
	}
}
