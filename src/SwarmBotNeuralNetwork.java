
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class SwarmBotNeuralNetwork 
{
	/*
	 * Inputs:
	 * Object in Front 				(Index = 0)
	 * Robot Heading: 
	 * 		North 					(Index = 1)
	 * 		South 					(Index = 2)
	 * 		East  					(Index = 3)
	 * 		West  					(Index = 4)
	 * Robot Position to Target:
	 * 		South 					(Index = 5)
	 *		North 					(Index = 6)
	 *		West  					(Index = 7)
	 * 		East  					(Index = 8)
	 * Robot at Target				(Index = 9)
	 * Total inputs: 11
	 */
	
	/*
	 * Outputs:
	 * No Change 					(Index = 0)
	 * Stop (At target) 			(Index = 1)
	 * Reverse 						(Index = 2)
	 * Turn Right(45) 				(Index = 3)
	 * Turn Left(45) 				(Index = 4)
	 * Object Avoidance Algorithm 	(Index = 5)
	 * Total Outputs: 6
	 */

	static int numInputs;
	static int numOutputs;
	static int numHiddenLayers;
	static int numHiddenCellsPerLayer;
	static int numIterations;
	static double learningRate;
	static MovementNetwork network;
	
	//For training
	//static ArrayList<Integer> numStops = new ArrayList<Integer>();
	static int numAvoids = 0;
	static int numDoNothings = 0;
	static int numReverses = 0;
	static ArrayList<Integer> turnRights = new ArrayList<Integer>();
	static ArrayList<Integer> turnLefts = new ArrayList<Integer>();
	
	public static void main(String[] args) 
	{
		createNetwork();
		trainNetwork();
		
		int[] check1 = {0,0,
				0,0,0,0,
				0,0,0,0,
				0};
		TestNetwork(check1);
		
		int[] check2 = {1,0,
					1,0,1,0,
					0,1,0,0,
					0};
		TestNetwork(check2);
		
		int[] check3 = {0,1,
				1,0,1,0,
				0,1,0,0,
				0};
		TestNetwork(check3);
		
	}
	
	static void createNetwork()
	{
		//The robot has several sensors, we are able to adjust these down to just 11 values
		numInputs = 10;	
		
		//The robot has several output objects: we are able to adjust these to 6. 
		numOutputs = 6;
		
		//Let's use two hidden layers. The number of outputs to inputs is rather large
		numHiddenLayers = 2;
		

		//Let's use 4 cells at each hidden layer
		numHiddenCellsPerLayer = 5;
		
		//Populate Hidden Layers Array List
		ArrayList<Integer> hiddenLayers = new ArrayList<Integer>();
		for(int x: new Range(numHiddenLayers))
			hiddenLayers.add(numHiddenCellsPerLayer);
		
		//Let's use 1,000,000 iterations
		numIterations = 100000;
		
		//Let's use a learning rate of 0.15
		learningRate = 0.15;
		
		network = new MovementNetwork(numInputs, numOutputs, hiddenLayers, numIterations, learningRate);
	}
	
	static void trainNetwork()
	{
		network.TrainNetwork(sampleInputs(), sampleOutputs());
	}
	
	static ArrayList<ArrayList<Integer>> sampleInputs()
	{
		ArrayList<ArrayList<Integer>> sampleInputs = new ArrayList<ArrayList<Integer>>();
		
		/*
		 * Let's handle the easy cases first:
		 * 1 when the object is at the location : STOP
		 * 1 when an object is detected and robot is going that way -> AVOID OBJECT
		 */
		
		ArrayList<Integer> test1 = new ArrayList<Integer>();
		test1.add(0); // object in front 1
		test1.add(0); // Robot Heading North 3
		test1.add(0); // Robot Heading South 4
		test1.add(0); // Robot Heading East 5
		test1.add(0); // Robot Heading West 6
		test1.add(0); // Robot South of Target 7
		test1.add(0); // Robot North of Target 8
		test1.add(0); // Robot West of Target 9
		test1.add(0); // Robot East of Target 10
		test1.add(1); // Robot at Target
		sampleInputs.add(test1);
		
		ArrayList<Integer> test2 = new ArrayList<Integer>();
		test2.add(1); // object in front 1
		test2.add(1); // Robot Heading North 3
		test2.add(1); // Robot Heading South 4
		test2.add(1); // Robot Heading East 5
		test2.add(1); // Robot Heading West 6
		test2.add(1); // Robot South of Target 7
		test2.add(1); // Robot North of Target 8
		test2.add(1); // Robot West of Target 9
		test2.add(1); // Robot East of Target 10
		test2.add(1); // Robot at Target
		sampleInputs.add(test2);
		
		ArrayList<Integer> test3 = new ArrayList<Integer>();
		test3.add(1); // object in front 1
		test3.add(1); // Robot Heading North 3
		test3.add(1); // Robot Heading South 4
		test3.add(1); // Robot Heading East 5
		test3.add(1); // Robot Heading West 6
		test3.add(1); // Robot South of Target 7
		test3.add(1); // Robot North of Target 8
		test3.add(1); // Robot West of Target 9
		test3.add(1); // Robot East of Target 10
		test3.add(0); // Robot at Target
		sampleInputs.add(test3);
		
		ArrayList<Integer> test4 = new ArrayList<Integer>();
		test4.add(1); // object in front 1
		test4.add(0); // Robot Heading North 3
		test4.add(0); // Robot Heading South 4
		test4.add(0); // Robot Heading East 5
		test4.add(0); // Robot Heading West 6
		test4.add(0); // Robot South of Target 7
		test4.add(0); // Robot North of Target 8
		test4.add(0); // Robot West of Target 9
		test4.add(0); // Robot East of Target 10
		test4.add(0); // Robot at Target
		sampleInputs.add(test4);
		
		for(int i = 1; i <= 4; i++)
		{
			ArrayList<Integer> test = new ArrayList<Integer>();
			for(int x : new Range(10))
				test.add(0);

			test.set(i, 1);
			test.set(i+4, 1);
			sampleInputs.add(test);
			numDoNothings++;
		}
		
		for(int i = 1; i <= 4; i++)
		{
			for(int j = 2; j < 3; j++)
			{
				ArrayList<Integer> test = new ArrayList<Integer>();
				for(int x : new Range(10))
					test.add(0);
				
				test.set(i, 1);
				test.set(i+j,1);
				test.set(4+i, 1);
				test.set(4+j,1);
				
				sampleInputs.add(test);
				numDoNothings++;
			}
		}
		
		for(int i = 1; i <= 4; i++)
		{
			ArrayList<Integer> test = new ArrayList<Integer>();
			for(int x : new Range(10))
				test.add(0);
			
			test.set(i,1);
			if(i % 2 == 1)
				test.set(i+5, 1);
			else
				test.set(i+3, 1);
			
			sampleInputs.add(test);
			numReverses++;
		}
		
		int goRight = 0;
		for(int i = 1; i <=2 ; i++)
		{
			ArrayList<Integer> test = new ArrayList<Integer>();
			for(int x: new Range(10))
				test.add(0);
		
			test.set(i,1);
			test.set(i+6,1);
			sampleInputs.add(test);
			goRight++;
		}
		turnRights.add(goRight);
		
		int goLeft = 0;
		for(int i = 1; i <=2 ; i++)
		{
			ArrayList<Integer> test = new ArrayList<Integer>();
			for(int x: new Range(10))
				test.add(0);
		
			test.set(i,1);
			test.set(i+7,1);
			sampleInputs.add(test);
			goLeft++;
		}
		turnLefts.add(goLeft);
		
		//Go East, North of target turn right
		//Go West, South of target turn right
		goRight = 0;
		for(int i = 3; i <=4 ; i++)
		{
			ArrayList<Integer> test = new ArrayList<Integer>();
			for(int x: new Range(10))
				test.add(0);
			test.set(i,1);
			if(i == 4)
				test.set(7,1);
			else
				test.set(6,1);
			sampleInputs.add(test);
			goRight++;
		}
		turnRights.add(goRight);
		
		//Go East, South of target turn left
		//Go West, South of target turn left
		goLeft = 0;
		for(int i = 3; i <=4 ; i++)
		{
			ArrayList<Integer> test = new ArrayList<Integer>();
			for(int x: new Range(10))
				test.add(0);
			test.set(i,1);
			if(i == 4)
				test.set(7,1);
			else
				test.set(8,1);
			sampleInputs.add(test);
			goLeft++;
		}
		turnLefts.add(goLeft);

		
		
		ArrayList<Integer> test = new ArrayList<Integer>();
		test.add(0); // object in front 1
		test.add(0); // Robot Heading North 3
		test.add(0); // Robot Heading South 4
		test.add(0); // Robot Heading East 5
		test.add(0); // Robot Heading West 6
		test.add(0); // Robot South of Target 7
		test.add(0); // Robot North of Target 8
		test.add(0); // Robot West of Target 9
		test.add(0); // Robot East of Target 10
		test.add(0); // Robot at Target
		sampleInputs.add(test);
		

		return sampleInputs;
	}
	
	static ArrayList<Integer> sampleOutputs()
	{
		/*
		 * Outputs:
		 * No Change 					(Index = 0)
		 * Stop (At target) 			(Index = 1)
		 * Reverse 						(Index = 2)
		 * Turn Right(45) 				(Index = 3)
		 * Turn Left(45) 				(Index = 4)
		 * Object Avoidance Algorithm 	(Index = 5)
		 * Total Outputs: 6
		 */
		ArrayList<Integer> sampleOutputs = new ArrayList<Integer>();
		for(int x : new Range(2))
		{
			sampleOutputs.add(1);
		}
		
		for(int x : new Range(2))
		{
			sampleOutputs.add(5);
		}
		
		for(int x : new Range(numDoNothings))
		{
			sampleOutputs.add(0);
		}
		
		for(int x: new Range(numReverses))
		{
			sampleOutputs.add(2);
		}
		
		for(int i = 0; i < turnRights.size(); i++)
		{
			for(int x : new Range(turnRights.get(i)))
			{
				sampleOutputs.add(3);
			}
			
			for(int x : new Range(turnLefts.get(i)))
			{
				sampleOutputs.add(4);
			}
		}
		
		sampleOutputs.add(1);
		
		return sampleOutputs;
	}
	
	static void TestNetwork(int[] inputs)
	{
		ArrayList<Integer> ins = new ArrayList<Integer>();
		System.out.println("");
		for(int i : inputs)
		{
			System.out.print(i + " ");
			ins.add(i);
		}
			
		int output = network.RunNetwork(ins);
		/*
		 * Outputs:
		 * No Change 					(Index = 0)
		 * Stop (At target) 			(Index = 1)
		 * Reverse 						(Index = 2)
		 * Turn Right(45) 				(Index = 3)
		 * Turn Left(45) 				(Index = 4)
		 * Object Avoidance Algorithm 	(Index = 5)
		 * Total Outputs: 6
		 */
		switch(output)
		{
			case 0: System.out.println("\nOutput: No Change");break;
			case 1: System.out.println("\nOutput: Stop"); break;
			case 2: System.out.println("\nOutput: Reverse"); break;
			case 3: System.out.println("\nOutput: Turn Right"); break;
			case 4: System.out.println("\nOutput: Turn Left"); break;
			case 5: System.out.println("\nOutput: Avoidance Algorithm"); break;
			default: System.out.println("\nOutput: Error"); break;
		}
	}
	
	ArrayList<ArrayList<Integer>> allPosibleInputs()
	{
		ArrayList<ArrayList<Integer>> allPossibleOutputs = new ArrayList<ArrayList<Integer>>();
		ArrayList<int[]> values = new ArrayList<int[]>();
		for(int i = -1; i <= 10; i++)
		{
			int[] vals = new int[10];
			for(int j = 0; j <= 11; j++)
			{
				if(j >= i)
					vals[j] = 1;
				else
					vals[j] = 0;
			}
			values.addAll(permutations(vals));
		}
		
		for(int[] x : values)
		{
			ArrayList<Integer> toAdd = new ArrayList<Integer>();
			for(int i : x)
				toAdd.add(i);
			allPossibleOutputs.add(toAdd);
		}
				
		return allPossibleOutputs;
	}
	
	static ArrayList<int[]> permutations(int[] a) {
	    ArrayList<int[]> ret = new ArrayList<int[]>();
	    permutation(a, 0, ret);
	    return ret;
	}

	static void permutation(int[] arr, int pos, ArrayList<int[]> list){
	    if(arr.length - pos == 1)
	        list.add(arr.clone());
	    else
	        for(int i = pos; i < arr.length; i++){
	            swap(arr, pos, i);
	            permutation(arr, pos+1, list);
	            swap(arr, pos, i);
	        }
	}

	static void swap(int[] arr, int pos1, int pos2){
	    int h = arr[pos1];
	    arr[pos1] = arr[pos2];
	    arr[pos2] = h;
	}
}
