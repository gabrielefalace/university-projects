package core;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Random;
import java.util.TreeSet;

import core.ChordNetwork;
import core.Constants;
import core.Peer;


public abstract class Algorithm {

	protected static final int SCALE = 1000;
	protected static final RoundingMode ROUNDING = RoundingMode.HALF_EVEN;
	
	protected String description;
	protected int peersToEvaluate;
	protected ChordNetwork chord;
	protected int nodeNumber;
	protected int sampleSize;
	
	/**
	 * Avvia la simulazione di questo algoritmo con i parametri prefissati e scrive su file (nella cartella results) i risultati.
	 * @param nodeNumber numero di nodi nella rete da simulare.
	 * @param sampleSize dimensione del campione.
	 */
	public void StartSimulation(boolean writeDescription){
		ResultWriter resultWriter = new ResultWriter("Algorithm-", nodeNumber, sampleSize);
		
		if(writeDescription==true)
			resultWriter.writeAlgorithmDescription(description);
		
		TreeSet<Integer> usedIndexes = new TreeSet<Integer>(); 
		Peer peerList[] = chord.getPeerList();
		
		Random random = new Random(Constants.seed);
		
		double results[] = new double[peersToEvaluate];
		
		int current = 0;
		while(usedIndexes.size() < peersToEvaluate){
			// scelta del peer da cui eseguire la stima:
			int index = random.nextInt(peerList.length);
			// solo se l'indice non è stato usato:
			if(!usedIndexes.contains(index)){
				usedIndexes.add(index);	
				// stima dal peer scelto:
				results[current] = computeEstimateFrom(peerList[index]);
				current++;
			}
		}
		
		for(int i=0; i<peersToEvaluate; i++)
			resultWriter.writeSampledEstimate(results[i]);
		resultWriter.shutDown();
	}

	protected abstract double computeEstimateFrom(Peer peer);
	
	public BigDecimal difference(BigInteger successor, BigInteger start){
		BigDecimal two = new BigDecimal("2");
		BigDecimal N = two.pow(160);
		if(successor.compareTo(start) > 0)
			return new BigDecimal(successor.subtract(start));
		else
			return ( N.subtract(new BigDecimal(start)) ).add(new BigDecimal(successor));
	}
	
	
	public void chrashNodes(double failurePercentage) {
		Peer[] peerList = chord.getPeerList();
		Random random = new Random(Constants.failure_seed);
		int crashedNodes = 0;
		int numberOfNodesToCrash = (int) (peerList.length*failurePercentage);
		int index; 
		while(crashedNodes < numberOfNodesToCrash){
			index = random.nextInt(peerList.length);
			if(!chord.isFailed(peerList[index])){
				chord.fail(peerList[index]);
				crashedNodes++;
			}
		}
	}
}
