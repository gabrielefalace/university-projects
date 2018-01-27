package unstableSimulations;

import unstableAlgorithms.DistinctFingersUnstableAlgorithm;
import unstableAlgorithms.ExtendedLocalUnstableAlgorithm;
import unstableAlgorithms.ForwardingUnstableAlgorithm;
import unstableAlgorithms.SplitForwardingUnstableAlgorithm;
import core.Algorithm;

public class UnstableEstimationSimulation {

	static final boolean DESCRIPTION_FLAG = false;
	
	/*
	 * Avvia una simulazione dell'algoritmo DistinctFingers su reti.
	 * 
	 */
	public static void main(String[] args){

		double failurePercentage = 0.5;
		
		for(int networkSize=1000; networkSize<=16000; networkSize*=2){
			System.out.println("Rete di dimensione: "+networkSize+"\n--------------------------------");
			for(int sampleSize=10; sampleSize<=100; sampleSize+=10){
				System.out.println("Campione da "+sampleSize+" nodi");
				Algorithm algo;
				// algo = new DistinctFingersUnstableAlgorithm(networkSize, sampleSize, failurePercentage);
				// algo = new ExtendedLocalUnstableAlgorithm(networkSize, sampleSize, failurePercentage);
				algo = new ForwardingUnstableAlgorithm(networkSize, sampleSize, failurePercentage);
				algo.StartSimulation(DESCRIPTION_FLAG);
			}
			System.out.println("\n");
		}
		
	}
	
}
