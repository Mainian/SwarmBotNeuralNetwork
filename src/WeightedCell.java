

import java.util.ArrayList;

public class WeightedCell extends Cell {

	ArrayList<Double> weights = new ArrayList<Double>();
	ArrayList<? extends iCell> prevCells = new ArrayList<Cell>();
	
	public WeightedCell(ArrayList<? extends iCell> prev)
	{
		this.prevCells = prev;
		buildWeights(prev.size());
		this.cellType = CellType.Hidden;
	}
	
	void buildWeights(int i)
	{
		for (int j = 0; j < i; j++)
			weights.add(0.5);
	}

	public void CalculateValue() 
	{
		double v = 0;
		for (int i = 0; i < weights.size(); i++)
			v += weights.get(i) * prevCells.get(i).GetValue();
		value = 1/(1+Math.exp(0-v));
	}

	public void AdjustWeight(int weight, double value) 
	{
		weights.set(weight, value);
	}

	public ArrayList<Double> getWeights() 
	{
		return weights;
	}

	public double GetWeight(int num) 
	{
		return weights.get(num);
	}

}
