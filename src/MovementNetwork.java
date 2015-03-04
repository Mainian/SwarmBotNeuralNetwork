import java.util.ArrayList;

public class MovementNetwork extends Network {
	
	public MovementNetwork(int numInputs, int numOutputs, ArrayList<Integer> hiddenLayers, int iterations, double learningRate)
	{
		//network = new Network();
		
		for(int inputCell : new Range(numInputs))
			super.AddInputCell(inputCell);
		
		for(int i : hiddenLayers)
			super.AddHiddenLayer(i);
		
		super.SetNumOutputs(numOutputs);
		
		super.BuildNetwork();
		
		super.SetIterations(iterations);
		super.SetLearningRate(learningRate);
	}	
	
	public void TrainMovementNetwork(ArrayList<ArrayList<Integer>> inputs, ArrayList<Integer> outputs)
	{
		super.TrainNetwork(inputs, outputs);
	}
	
	public int UseNetwork(ArrayList<Integer> inputs)
	{
		return super.RunNetwork(inputs);
	}
}
