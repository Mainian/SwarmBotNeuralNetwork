

import java.util.ArrayList;

public class Network 
{
	ArrayList<ArrayList<WeightedCell>> hiddenCells = new ArrayList<ArrayList<WeightedCell>>();
	ArrayList<OutputCell> outputCells = new ArrayList<OutputCell>();
	double initWeight = 1.0;
	
	double learningRate = 0.15;
	public void SetLearningRate(double learningRate){ this.learningRate = learningRate; }
	public double GetLearningRate() { return learningRate; }
	
	int iterations = 10000000;
	public void SetIterations(int iterations) { this.iterations = iterations; }
	public int GetIterations() { return iterations; }
	
	public void AddInputCells(int[] nums)
	{
		for (int i : nums)
			inputCells.add(new InputCell(i));
	}
	
	ArrayList<InputCell> inputCells = new ArrayList<InputCell>();
	public void AddInputCell(int num) { inputCells.add(new InputCell(num)); }
	public void ClearInputCells() { inputCells.clear(); }
	public ArrayList<Double> GetInputValues() 
	{
		ArrayList<Double> rtrn = new ArrayList<Double>();
		for(InputCell cell : inputCells)
			rtrn.add(cell.GetValue());
		return rtrn;
	}
	
	ArrayList<Integer> numHiddenCells = new ArrayList<Integer>();
	public void SetNumHiddenCells(int layer, int num) { numHiddenCells.set(layer, num); }
	public void AddHiddenLayer(int size) { numHiddenCells.add(size); }
	public void ClearHiddenCells() { numHiddenCells.clear(); }
	
	int numOutputCells;
	public void SetNumOutputs(int num) { numOutputCells = num; }
	
	double randomWeight() { return Math.random()*initWeight - 0.5; }
	
	public void BuildNetwork()
	{
		boolean fPass = true;
		int index = 0;
		for(int i : numHiddenCells)
		{
			hiddenCells.add(new ArrayList<WeightedCell>());
			if(fPass)
			{
				addHiddenCells(hiddenCells.get(0),inputCells,i);
				fPass= false;
			}
			else
			{
				addHiddenCells(hiddenCells.get(index+1),hiddenCells.get(index),i);
				index++;
			}
		}
		
		if(hiddenCells.size() < 1)
			addOutputCells(outputCells,inputCells,numOutputCells);
		else
			addOutputCells(outputCells,hiddenCells.get(hiddenCells.size()-1),numOutputCells);

	}
	
	void addHiddenCells(ArrayList<WeightedCell> toAddTo, ArrayList<? extends Cell> inputs, int numCellsToAdd)
	{
		for(int x : new Range(numCellsToAdd))
		{
			WeightedCell cell = new WeightedCell(inputs);
			for(int j : new Range(inputs.size()))
				cell.AdjustWeight(j, randomWeight());
			toAddTo.add(cell);
		}
	}
	
	void addOutputCells(ArrayList<OutputCell> toAddTo, ArrayList<? extends Cell> inputs, int numCellsToAdd)
	{
		for(int x : new Range(numCellsToAdd))
		{
			OutputCell cell = new OutputCell(inputs);
			for(int j : new Range(inputs.size()))
				cell.AdjustWeight(j, randomWeight());
			toAddTo.add(cell);
		}
	}
	
	public void TrainNetwork(ArrayList<ArrayList<Integer>> inputs, ArrayList<Integer> outputs)
	{
		for(@SuppressWarnings("unused") int i : new Range(iterations))
		{
			for(int x : new Range(inputs.size()))
			{
				propogate(inputs.get(x),outputs.get(x));
			} 
		}
	}
	
	void propogate(ArrayList<Integer> inputs, int output)
	{
		RunNetwork(inputs);
		
		ArrayList<Double> outputError = computeOutputError(output);
		ArrayList<ArrayList<Double>> hiddenError = new ArrayList<ArrayList<Double>>();
		if (hiddenCells.size() > 0)
		{
			ArrayList<Double> values = new ArrayList<Double>();
			for(int j : new Range(hiddenCells.size()))
				hiddenError.add(values);
			
			hiddenError.set(hiddenCells.size()-1, computeHiddenCellError(hiddenCells.size() - 1, outputError, outputCells));
			
			for(int j = hiddenCells.size()-2; j >= 0; j--)
				hiddenError.set(j, computeHiddenCellError(j, hiddenError.get(j+1), hiddenCells.get(j+1)));
			
			for(int j : new Range(hiddenCells.size()))
				for(int k : new Range(hiddenCells.get(j).size()))
					AdjustWeight(hiddenCells.get(j).get(k), hiddenError.get(j).get(k));
		}
	}
	
	void newInputs(ArrayList<Integer> inputs)
	{
		inputCells.clear();
		for(int i : new Range(inputs.size()))
		{
			InputCell cell = new InputCell(inputs.get(i));
			inputCells.add(cell);
		}
	}
	
	ArrayList<Double> computeOutputError(int result)
	{
		ArrayList<Double> errorResults = new ArrayList<Double>();
		
		for(int i : new Range(outputCells.size()))
		{
			double target = 0.0;
			if (result == i)
				target = 1.0;
			
			double outputValue = outputCells.get(i).GetValue();
			double errorValue = (target - outputValue) * outputValue * (1-outputValue);
			errorResults.add(errorValue);
		}
		
		return errorResults;
	}
	
	ArrayList<Double> computeHiddenCellError(int layer, ArrayList<Double> nextError, ArrayList<? extends WeightedCell> nextCells)
	{
		ArrayList<Double> errorResults = new ArrayList<Double>();
		
		for(int i : new Range(hiddenCells.get(layer).size()))
		{
			errorResults.add(0.0);
			for(int j : new Range(nextCells.size()))
			{
				double val = hiddenCells.get(layer).get(i).GetValue();
				double weight = nextCells.get(j).GetWeight(i);
				double more = nextError.get(j)*weight*val*(1-val);
				errorResults.set(i, errorResults.get(i)+more);
			}
		}
		
		return errorResults;
	}
	
	void AdjustWeight(WeightedCell cell, double error)
	{
		for(int i : new Range(cell.getWeights().size()))
			cell.AdjustWeight(i, cell.getWeights().get(i)+learningRate*error*cell.GetValue());
	}
	
	public int RunNetwork(ArrayList<Integer> inputs)
	{
		int result = 0;
		newInputs(inputs);
		
		for(ArrayList<WeightedCell> weightedCells : hiddenCells)
			for(WeightedCell cell : weightedCells)
				cell.CalculateValue();
		
		for(OutputCell cell : outputCells)
			cell.CalculateValue();
		
		for(int i : new Range(outputCells.size()))
			if(outputCells.get(i).GetValue() > outputCells.get(result).GetValue())
				result = i;
				
		return result;
	}
}
