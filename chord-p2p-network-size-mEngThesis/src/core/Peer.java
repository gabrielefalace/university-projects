package core;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.TreeSet;

public class Peer implements Comparable<Peer>{

	BigInteger identifier;
	BigInteger[][] fingerTable;
	
	/**
	 * Crea un Peer con identificativo null e finger table con tutte le START corrette e tutti i successor a -1.
	 */
	public Peer(){
		identifier = null;
		fingerTable = null;
	}
	
	/**
	 * Inizializza un nodo con identificativo e imposta correttamente tutti i campi start.
	 * @param identifier l'identificativo di questo nodo
	 */
	public final void initialize(BigInteger identifier){
		this.identifier = identifier;  

		final int m = Constants.bits;
		final BigInteger two = new BigInteger("2");
		final BigInteger length = two.pow(m);
		
		fingerTable = new BigInteger[m][2];
		for(int index=0; index<m; index++){
			BigInteger startValue = (identifier.add(two.pow(index))).mod(length);
			setStart(index, startValue);
			setSuccessor(index, new BigInteger("-1"));
		}
	}
	
	
	public final BigInteger getIdentifier(){
		return identifier;
	}
	
	public final BigInteger getStart(int index){
		return fingerTable[index][0];
	}
	
	
	public final BigInteger getSuccessor(int index){
		return fingerTable[index][1];
	}
	
	public final void setStart(int index, BigInteger startValue){
		fingerTable[index][0] = startValue;
	}
	
	public final void setSuccessor(int index, BigInteger successorValue){
		fingerTable[index][1] = successorValue;
	}
	
	public boolean equals(Object o){
		Peer otherPeer = (Peer)o;
		return otherPeer.identifier == this.identifier;
	}
	
	public int compareTo(Peer otherPeer){
		return this.identifier.compareTo(otherPeer.identifier);
	}
	
	
	public final int countDistinctFingers(){
		TreeSet<BigInteger> distinctFingers = new TreeSet<BigInteger>();
		for(int i=0; i<fingerTable.length; i++){
			BigInteger finger = fingerTable[i][1];
			if(!distinctFingers.contains(finger))
				distinctFingers.add(finger);
		}
		return distinctFingers.size();
	}
	
//	public final BigInteger[] getDistinctFingers(){
//		TreeSet<BigInteger> distinctFingers = new TreeSet<BigInteger>();
//		for(int i=0; i<fingerTable.length; i++){
//			BigInteger finger = fingerTable[i][1];
//			if(!distinctFingers.contains(finger))
//				distinctFingers.add(finger);
//		}
//		BigInteger[] fingers = new BigInteger[distinctFingers.size()];
//		int i=0;
//		for(BigInteger b: distinctFingers){
//			fingers[i] = b;
//			i++;
//		}
//		return fingers;
//	}
//	
//	public final LinkedList<BigInteger> getFingerListWithDuplicates(){
//		LinkedList<BigInteger> fingerList = new LinkedList<BigInteger>();
//		for(int i=0; i<fingerTable.length; i++)
//			fingerList.add(fingerTable[i][1]);
//		return fingerList;
//	}
}
