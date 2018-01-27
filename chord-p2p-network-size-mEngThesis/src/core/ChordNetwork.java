package core;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;
import java.util.TreeSet;

/**
 * 
 * @author Gabriele Falace
 * 
 */
public class ChordNetwork {

	private int n;
	private int m;
	private BigDecimal ringSize;
	
	private Random random;
	
	private Peer[] peers;
	private TreeSet<BigInteger> assignedIdentifiers;
	
	private TreeSet<BigInteger> failedPeers;
	
	/**
	 * Costruttore di una rete con 2^m indirizzi e n nodi. 
	 * Inizializza anche tutte le finger table.
	 * @param bits il numero di bits usati per rappresentare l'indirizzo
	 * @param peerNumber il numero di peer da inserire nella rete
	 */
	public ChordNetwork(int bits, int peerNumber, long seed){
		n = peerNumber;
		m = bits;
		
		random = new Random(seed);
		peers = new Peer[peerNumber];
		assignedIdentifiers = new TreeSet<BigInteger>();
		failedPeers = new TreeSet<BigInteger>();
		ringSize = new BigDecimal("2").pow(m);
		
		addPeers();
		initializePeers();
		fillFingerTables();
	}
	
	/**
	 * Inizializza tutte le finger table dei nodi.
	 */
	private void fillFingerTables() {
		Arrays.sort(peers);
		for(int i=0; i<n; i++){
			updateFinger(peers[i]);
		}
	}

	/**
	 * Aggiorna la finger-table di un determinato Peer.
	 * @param peer il Peer al quale aggiornare la finger table.
	 */
	private void updateFinger(Peer peer) {
		BigInteger startKey;
		BigInteger successorID;
		for(int i=0; i<m; i++){
			startKey = peer.getStart(i);
			if(startKey.compareTo(peers[n-1].getIdentifier())>0 || startKey.compareTo(peers[0].getIdentifier())<0)
				successorID = peers[0].getIdentifier();
			else
				successorID = peers[successorOf(startKey)].getIdentifier();
			peer.setSuccessor(i, successorID);
		}
	}

	/**
	 * Inserisce tutti i peer all'array di Peer.
	 * post: le finger table sono inconsistenti.
	 */
	private final void addPeers() {
		for(int i=0; i<n; i++)
			peers[i] = new Peer();
	}
	
	/**
	 * Assegna a ciascuno degli n peer un identificativo univoco.
	 * L'assegnamento segue una distribuzione uniforme tra 0 e (2^160)-1.
	 * E' garantito che non ci siano Peer con stesso ID.
	 */
	private final void initializePeers() {
		int i=0;
		double randomDouble;
		BigInteger tmpIdentifier = null;
		BigDecimal bigRandom;
		BigDecimal tmpIdentifierDecimal;
		while(i<n){
			randomDouble = random.nextDouble();
			bigRandom = new BigDecimal(randomDouble);
			tmpIdentifierDecimal = bigRandom.multiply(ringSize);
			tmpIdentifier = tmpIdentifierDecimal.toBigInteger();
			if(!assignedIdentifiers.contains(tmpIdentifier)){
				assignedIdentifiers.add(tmpIdentifier);
				peers[i].initialize(tmpIdentifier);
				i++;
			}
		}
	}
	
	/**
	 * Consente di conoscere il numero di nodi connessi alla rete.
	 * @return il numero di nodi che compongono la rete.
	 */
	public final int getNodesNumber(){
		return n;
	}
	
	
	/**
	 * Cerca il successore di una chiave.
	 * @param key La chiave di cui si vuole trovare il successore.
	 * @return L'indice dell'array che individua il Peer successore della chiave specificata.
	 */
	public final int successorOf(BigInteger key){
		int start = 0, end = n-1;
		int mid;
		while(start < end){
			if(end - start < 20)
				break;			
			mid = Math.round((end + start)/2);
			if(mid==end || mid==start)
				break;
			
			if(peers[mid].getIdentifier().equals(key))
				return mid;
			
			if(peers[mid].getIdentifier().compareTo(key) > 0)
				end = mid;
			else
				start = mid+1;
		}
		
		// ricerca lineare
		for(int i=start; i<=end; i++)
			if(peers[i].getIdentifier().compareTo(key) > 0 || peers[i].getIdentifier().equals(key))
				return i;
		
		return -1;
	}
	
	/**
	 * Consente l'accesso alla lista dei Peer che costituiscono la rete.
	 * @return L'array di Peer in rete.
	 */
	public final Peer[] getPeerList(){
		return peers;
	}

	/**
	 * Consente di accedere al Peer con un certo identificativo. 
	 * @param selectedNodeID l'ID del Peer da recuperare.
	 * @return Il Peer con l'ID specificato.
	 */
	public Peer getNodeWithId(BigInteger selectedNodeID) {
		for(Peer p: peers)
			if(p.identifier.equals(selectedNodeID))
				return p;
		return null;
	}
	
	/**
	 * Fa fallire un peer
	 * @param p
	 */
	public void fail(Peer p){
		failedPeers.add(p.identifier);
	}
	
	/**
	 * Fa fallire un peer
	 * @param identifier
	 */
	public void fail(BigInteger identifier){
		failedPeers.add(identifier);
	}
	
	/**
	 * Verifica se un peer ha subito un guasto (non è più disponibile)
	 * @param peer
	 * @return
	 */
	public boolean isFailed(Peer peer){
		return failedPeers.contains(peer.identifier);
	}
	
	/**
	 * Verifica se un peer ha subito un guasto (non è più disponibile)
	 * @param identifier
	 * @return
	 */
	public boolean isFailed(BigInteger identifier){
		return failedPeers.contains(identifier);
	}
	
	/**
	 * Consente di conoscere il numero di peer guasti.
	 * @return
	 */
	public int getNumberOfFailedNodes(){
		return failedPeers.size();
	}
}
