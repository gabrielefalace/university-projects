package stableSimulations;

import stableAlgorithms.DistinctFingersAlgorithm;
import stableAlgorithms.ForwardingAlgorithm;
import stableAlgorithms.SplitForwardingAlgorithm;

/**
 * Classe in cui vengono eseguite le simulazioni.
 * @author Gabriele
 *
 */
public class EstimationSimulation {

	static final boolean DESCRIPTION = false;
	
	public static void main(String[] args){

		for(int sampleSize=10; sampleSize<=100; sampleSize+=10){
			System.out.println("Simulating sample = "+sampleSize+" ... ");
			SplitForwardingAlgorithm algorithm = new SplitForwardingAlgorithm(16000, sampleSize);
			algorithm.StartSimulation(DESCRIPTION);
		}
		System.out.println("SIMULATION FINISHED");
	}
	
}
