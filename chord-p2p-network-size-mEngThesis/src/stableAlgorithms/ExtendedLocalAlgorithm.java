package stableAlgorithms;

import java.math.BigDecimal;
import core.Algorithm;
import core.ChordNetwork;
import core.Constants;
import core.Peer;

/**
 * 
 * @author Gabriele
 *
 * Classe che fornisce una realizzazione dell'algoritmo basato sulla stima locale (formula di Wurzburg) tramite densità e successivo
 * calcolo della media su più nodi.
 */
public class ExtendedLocalAlgorithm extends Algorithm{
	
	
	

	
	public ExtendedLocalAlgorithm(int nodeNumber, int sampleSize){
		String DESCRIPTION = "Questo algoritmo si basa sul calcolo locale della densità da cui si ottiene una stima della dimensione locale (formula di Wurzburg)." +
							 "In seguito si ottiene la stima della dimensione facendo la media delle stime locali." +
							 "Il modo operazionale di raccolta del campione è sempre quello dei k successori immediati"; 
		super.description = DESCRIPTION;
		super.peersToEvaluate = 100;
		super.chord = new ChordNetwork(Constants.bits, nodeNumber, Constants.seed);
		super.nodeNumber = nodeNumber;
		super.sampleSize = sampleSize;
	}

	public ExtendedLocalAlgorithm(){
		this(0,0);
	}
	
	public double computeLocalEstimate(Peer peer){
		BigDecimal two = new BigDecimal("2");
		BigDecimal one = new BigDecimal("1");
		BigDecimal N = two.pow(160);
		
		BigDecimal[] d = new BigDecimal[Constants.bits];
		
		BigDecimal den = difference(peer.getSuccessor(0), peer.getStart(0));
		d[0] = one.divide(den, SCALE, ROUNDING);
		int counted = 1;
		
		for(int i=1; i<d.length; i++){
			d[i] = new BigDecimal("0");
			if(peer.getSuccessor(i).equals(peer.getSuccessor(i-1)))
				continue;
			else{
				den = difference(peer.getSuccessor(i), peer.getSuccessor(i-1)).add(one);
				d[i] = one.divide(den, SCALE, ROUNDING);
				counted++;
			}
		}
		
		BigDecimal avg_d = new BigDecimal("0");
		for(int i=0; i<d.length; i++)
			avg_d = avg_d.add(d[i]);
		
		BigDecimal count = new BigDecimal(counted);
		avg_d = avg_d.divide(count, SCALE, ROUNDING);
		BigDecimal estimate = N.multiply(avg_d);
		return estimate.doubleValue();
	}
	
	/**
	 * Calcolo della la stima della dimensione eseguito da un dato nodo.
	 * @param peer il peer dal quale si vuole eseguire l'algoritmo di stima.
	 * @param sampleSize la dimensione del campione.
	 * @return il valore stimato della dimensione della rete.
	 */
	protected double computeEstimateFrom(Peer peer) {
		double total = 0.00;
		
		Peer current = peer;
		int sampled = 0;
		
		while(sampled<sampleSize){
			total += computeLocalEstimate(current);
			current = chord.getNodeWithId(current.getSuccessor(0));
			sampled++;
		}
		
		return (double)total/sampleSize;
	}

}
