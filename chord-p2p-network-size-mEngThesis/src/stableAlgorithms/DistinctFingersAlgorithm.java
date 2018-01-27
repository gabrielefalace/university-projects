package stableAlgorithms;

import java.math.BigInteger;
import core.Algorithm;
import core.ChordNetwork;
import core.Constants;
import core.Peer;

/**
 * 
 * @author Gabriele
 *
 * Classe che fornisce una realizzazione dell'algoritmo basato sul calcolo di uMedio e successiva
 * stima effettuata come 2^uMedio.
 */
public class DistinctFingersAlgorithm extends Algorithm{

	public DistinctFingersAlgorithm(int nodeNumber, int sampleSize){
		String DESCRIPTION = "Questo algoritmo si basa sul calcolo di u_avg e sulla stima 2^u_avg." +
							 "Il calcolo di u_avg è effettuato chiedendo a un certo numero di successori diretti del nodo " +
							 "interrogante."; 
		super.description = DESCRIPTION;
		super.peersToEvaluate = 100;
		super.chord = new ChordNetwork(Constants.bits, nodeNumber, Constants.seed);
		super.nodeNumber = nodeNumber;
		super.sampleSize = sampleSize;
	}
	
	
	/**
	 * Calcolo della la stima della dimensione eseguito da un dato nodo.
	 * @param peer il peer dal quale si vuole eseguire l'algoritmo di stima.
	 * @param sampleSize la dimensione del campione.
	 * @return il valore stimato della dimensione della rete.
	 */
	protected double computeEstimateFrom(Peer peer) {
		int askedPeers = 1;
		double currentTotalU = peer.countDistinctFingers();
		Peer currentPeer = peer;
		Peer successor;
		while(askedPeers < sampleSize){
			BigInteger successorIndex = currentPeer.getSuccessor(0); 
			successor = chord.getNodeWithId(successorIndex);
			currentTotalU += successor.countDistinctFingers();
			askedPeers++;
			currentPeer = successor;
		}
		double uAVG = currentTotalU/askedPeers;
		double estimate = Math.pow(2, uAVG);
		return estimate;
	}
	
}
