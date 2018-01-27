package unstableAlgorithms;

import java.math.BigInteger;
import java.util.Random;

import stableAlgorithms.ExtendedLocalAlgorithm;

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
public class ExtendedLocalUnstableAlgorithm extends ExtendedLocalAlgorithm{
	
	public ExtendedLocalUnstableAlgorithm(int nodeNumber, int sampleSize, double failurePercentage){
		super(nodeNumber, sampleSize);
		super.chrashNodes(failurePercentage);
	}


	/**
	 * @Ovveride
	 */
	protected double computeEstimateFrom(Peer peer) {

		// dimensione della successorlist
		double log2_n = Math.log10(chord.getNodesNumber())/Math.log10(2);
		int successorlistLength = (int)Math.round(log2_n); 
		
		int askedPeers = 1;
		double estimateSum = super.computeLocalEstimate(peer);
		Peer currentPeer = peer;
		Peer successor;
		
		// Occorre determinare il primo nodo disponibile non fallito.
		while(askedPeers < sampleSize){
			BigInteger successorIndex = null;
			
			int trial = 0;
			while(trial < successorlistLength){
				successorIndex  = currentPeer.getSuccessor(0);
				
				// Calcolo del prossimo successore: 
				//	se è un nodo guasto si procede con il successivo
				//  altrimenti interrompi il loop in quanto bisogna effettuare altre operazioni
				if(chord.isFailed(successorIndex)){
					currentPeer = chord.getNodeWithId(successorIndex);
					trial++;
				}
				else
					break;
			}
			// while-postcondition:: successorIndex contiene l'id dell'ultimo nodo testato
			//						 se la ricerca è stata infruttuosa, conterra l'id di un nodo guasto.
			
			// Se anche l'ultimo nodo nella successorlist è inattivo, allora non è
			// più possbile continuare la raccolta del campione. Il nodo richiedente utilizzerà quello
			// che è riuscito a raccogliere.
			if(chord.isFailed(successorIndex))
				break;
			else{
				successor = chord.getNodeWithId(successorIndex);
				estimateSum += super.computeLocalEstimate(successor);
				askedPeers++;
				currentPeer = successor;
			}
		}
		double estimate = estimateSum/askedPeers;
		return estimate;
	}

}
