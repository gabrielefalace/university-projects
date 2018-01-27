package stableAlgorithms;

import core.Constants;
import core.Peer;

/**
 * TODO RIVEDERE LA JAVADOC
 * 
 * @author Gabriele
 *
 * Classe che fornisce una realizzazione dell'algoritmo basato sulla suddivisione 
 * dell'algoritmo di inoltro per limitare l'effetto della località.
 */
public class SplitForwardingAlgorithm extends ForwardingAlgorithm{
	
	private static int PEER_TO_EVALUATE = 100; 
	
	private static String DESCRIPTION = "Questo algoritmo si basa sullo split dell'algoritmo di inoltro." +
										"In seguito si ottiene la stima della rete come media della stima calcolata dalle due metà."; 
	
	public SplitForwardingAlgorithm(int nodeNumber, int sampleSize){
		super(nodeNumber, sampleSize/2);
		super.description = DESCRIPTION;
		super.peersToEvaluate = PEER_TO_EVALUATE;
	}

	/**
	 * Calcolo della la stima della dimensione eseguito da un dato nodo.
	 * @param peer il peer dal quale si vuole eseguire l'algoritmo di stima.
	 * @param sampleSize la dimensione del campione.
	 * @return il valore stimato della dimensione della rete.
	 */
	protected double computeEstimateFrom(Peer peer) {	
		Peer otherPeer = chord.getNodeWithId(peer.getSuccessor(Constants.bits-1));	// ultimo nodo della finger table -> dall'altra parte della rete.
		double myEstimate = super.computeEstimateFrom(peer);
		double otherEstimate = super.computeEstimateFrom(otherPeer);
		return (myEstimate+otherEstimate)/2;
	}

}
