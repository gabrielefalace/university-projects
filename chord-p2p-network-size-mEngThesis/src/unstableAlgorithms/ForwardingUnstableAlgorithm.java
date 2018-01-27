package unstableAlgorithms;

import java.math.BigDecimal;
import java.math.BigInteger;
import stableAlgorithms.ForwardingAlgorithm;
import core.Peer;

/**
 * 
 * @author Gabriele
 *
 * Classe che fornisce una realizzazione dell'algoritmo basato sul calcolo di uMedio e successiva
 * stima effettuata come 2^uMedio.
 */
public class ForwardingUnstableAlgorithm extends ForwardingAlgorithm{

	public ForwardingUnstableAlgorithm(int nodeNumber, int sampleSize, double failurePercentage){
		super(nodeNumber, sampleSize);
		super.chrashNodes(failurePercentage);
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
		
		// taglia della successorlist
		double log2_n = Math.log10(chord.getNodesNumber())/Math.log10(2);
		int successorlistLength = (int)Math.round(log2_n);
		
		int forwardings = 0;
		Peer currentPeer = peer;
		Peer successor;
		
		while(forwardings < sampleSize){
			
			// Calcolo del prossimo successore: 
			//	  se è un nodo guasto si procede con il successivo
			//    altrimenti interrompi il loop in quanto bisogna
			//	  effettuare altre operazioni
			BigInteger successorIndex = null;
			int trial = 0;
			while(trial < successorlistLength){
				successorIndex  = currentPeer.getSuccessor(0);
				if(chord.isFailed(successorIndex)){
					currentPeer = chord.getNodeWithId(successorIndex);
					trial++;
				}
				else
					break;
			}
			
			if(chord.isFailed(successorIndex))	// non si può continuare
				break;
			else{
				successor = chord.getNodeWithId(successorIndex);
				currentPeer = successor;
				forwardings++;
			}
		}		
		
		// calcolo della stima sul nodo raggiunto
		
		BigInteger myId = peer.getIdentifier();
		BigInteger pickedId = currentPeer.getIdentifier();
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
