package stableAlgorithms;

import java.math.BigDecimal;
import java.math.BigInteger;
import core.Algorithm;
import core.ChordNetwork;
import core.Constants;
import core.Peer;

/**
 * 
 * @author Gabriele
 *
 * Classe che fornisce una realizzazione dell'algoritmo basato sull'inoltro per k hops del messaggio e sulla successiva
 * stima della densità della rete.
 */
public class ForwardingAlgorithm extends Algorithm {

	
	public ForwardingAlgorithm(int nodeNumber, int sampleSize){
		String DESCRIPTION = "Questo algoritmo si basa sull'inoltro per k hops del messaggio." +
							 "In seguito si ottiene la stima della rete usando la densità."; 
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
		BigDecimal two = new BigDecimal("2");
		BigDecimal N = two.pow(160);
		
		Peer picked = peer;
		int forwardings = 0;
		
		while(forwardings < sampleSize){
			BigInteger nextID = picked.getSuccessor(0);
			picked = chord.getNodeWithId(nextID);
			forwardings++;
		}
		
		BigInteger myId = peer.getIdentifier();
		BigInteger pickedId = picked.getIdentifier();
		BigInteger delta;
		
		if(pickedId.compareTo(myId) > 0)
			delta = pickedId.subtract(myId);
		else{
			BigInteger ring = new BigInteger(N.toString());
			delta = (ring.subtract(myId)).add(pickedId);
		}
		
		BigDecimal k = new BigDecimal(sampleSize);
		
		BigDecimal delta_dec = new BigDecimal(delta.toString());
		BigDecimal d = k.divide(delta_dec, SCALE, ROUNDING);
		BigDecimal estimate = d.multiply(N);
		return estimate.doubleValue();
	}

}
