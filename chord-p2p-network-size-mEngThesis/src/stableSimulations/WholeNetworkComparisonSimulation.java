package stableSimulations;

import core.ChordNetwork;
import core.Constants;
import core.Peer;
import stableAlgorithms.ExtendedLocalAlgorithm;

public class WholeNetworkComparisonSimulation {

	
	
	public static void compareDistinctFinger(){
		// Tecnica DISTINCT-FINGERS
		ChordNetwork chord;
		
		for(int n=1000; n<=16000; n*=2){
			chord = new ChordNetwork(Constants.bits, n, Constants.seed);
			Peer[] peerList = chord.getPeerList();
			double sum = 0.00;
			for(int i=0; i<peerList.length; i++)
				sum += peerList[i].countDistinctFingers();
			double average = sum/peerList.length;
			double estimate = Math.pow(2, average);
			double error = (Math.abs(estimate - peerList.length)/peerList.length)*100;
			System.out.println("Size = "+n+" -> Errore(%) campionando tutta la rete = "+error+" stima = "+estimate);
		}
	}
	
	public static void compareExtendedLocal(){
		// Tecnica WURZBURG
		ChordNetwork chord;
		
		for(int n=1000; n<=16000; n*=2){
			chord = new ChordNetwork(Constants.bits, n, Constants.seed);
			Peer[] peerList = chord.getPeerList();
			double sum = 0.00;
			ExtendedLocalAlgorithm algo = new ExtendedLocalAlgorithm();
			for(int i=0; i<peerList.length; i++)
				sum += algo.computeLocalEstimate(peerList[i]);
			double estimate = sum/peerList.length;
			double error = (Math.abs(estimate - peerList.length)/peerList.length)*100;
			System.out.println("Size = "+n+" -> Errore(%) campionando tutta la rete = "+error+" stima = "+estimate);
		}
	}
	
	public static void main(String[] args){
		compareExtendedLocal();
	}
	
}
