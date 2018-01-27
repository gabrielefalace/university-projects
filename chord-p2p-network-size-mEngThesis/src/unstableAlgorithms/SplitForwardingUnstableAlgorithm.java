package unstableAlgorithms;

import core.Constants;
import core.Peer;

/**
 * 
 * @author Gabriele
 *
 * Classe che fornisce una realizzazione dell'algoritmo basato sul calcolo di uMedio e successiva
 * stima effettuata come 2^uMedio.
 */
public class SplitForwardingUnstableAlgorithm extends ForwardingUnstableAlgorithm{

	public SplitForwardingUnstableAlgorithm(int nodeNumber, int sampleSize, double failurePercentage){
		super(nodeNumber, sampleSize/2, failurePercentage);
		super.chrashNodes(failurePercentage);
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
