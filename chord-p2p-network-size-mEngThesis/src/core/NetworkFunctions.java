package core;

public class NetworkFunctions {
	
	private static final double LOG_2 = Math.log(2);
	private static final double CORRECTION = 0.33333333333333;
	
	private ChordNetwork chordNetwork;
	
	
	public NetworkFunctions(ChordNetwork aChordNetwork){
		chordNetwork = aChordNetwork;
	}
	
	
	/**
	 * Calcola la media dei finger distinti su tutta la rete
	 * @return la media dei finger distinti.
	 */
	public final float averageDistinctFingers(){
		float result = 0.0f;
		Peer[] peers = chordNetwork.getPeerList();
		for(int i=0; i<peers.length; i++)
			result += peers[i].countDistinctFingers();
		result = result/peers.length;
		return result;
	}
	
	/**
	 * Calcola il logaritmo in base 2 di un dato numero.
	 * @param n
	 * @return
	 */
	public double lg(int n){
		return Math.log(n)/LOG_2;
	}
	
	
	public double estimate(double u){
		double correctedU = u - CORRECTION;
		return Math.pow(2, correctedU);
	}
	
	
	public double error(double estimatedSize, int realSize){
		return Math.abs(estimatedSize - realSize)/realSize;
	}
	
	
	public final int countPeerWithFingers(int numFingers){
		int counter = 0;
		Peer[] peers = chordNetwork.getPeerList();
		for(Peer p: peers){
			int nf = p.countDistinctFingers();
			if(nf==numFingers)
				counter++;
		}
		return counter;
	}
	
	
}
