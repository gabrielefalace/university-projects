package node;

import java.util.*;
import util.*;

/**
 * Classe che rappresenta un nodo (peer) della rete Chord. Può essere esteso per ottenere 
 * nodi di overlay network basate sul protocollo Chord.
 * 
 * @since  06-08-2008
 * @author Gabriele
 *
 */
public class Node {
	
	/**
	 * Costante per accedere al campo START della finger table.
	 */
	public static final int START = 0;
	
	/**
	 * Costante per accedere al campo SUCCESSOR della finger table.
	 */
	public static final int SUCCESSOR = 1;
	
	/**
	 * Identificativo del nodo.
	 */
	int nodeID;
	
	/**
	 * Indica il numero di bit utilizzati dalla rete per identificare il nodo.
	 */
	int bits;
	
	/**
	 * Consente di utilizzare un cerchio di identificatori che permette di muoversi
	 * lungo l'anello logico avanti e indietro senza dover fare complicati calcoli in
	 * aritmetica modulare.
	 */
	IdentifierCircle idCircle;
	
	/**
	 * Mappa che contiene coppie <i>(s_id, nodo)</i> dove s_id è l'identificativo del servizio e nodo
	 * è un riferimento al nodo che detiene fisicamente la risorsa, cioè il nodo che 
	 * la pubblica. 
	 */
	Hashtable<String, Integer> dht;
	
	
	/**
	 * La finger table del nodo.
	 */
	int[][] fingerTable;
	
	
	/**
	 * Costruisce un nuovo Node.
	 * 
	 * @param id L'id del nuovo nodo.
	 */
	public Node(int id, IdentifierCircle circle){
		this.bits = circle.getBits();
		idCircle = circle;
		nodeID = id;
		fingerTable = new int[bits][2];
		for(int i=0; i<fingerTable.length; i++){
			fingerTable[i][START] = ((nodeID + (int)Math.pow(2, i))%((int)Math.pow(2, bits)));
			fingerTable[i][SUCCESSOR] = -1;
		}
		// inizializzo tutte le tabelle
		dht = new Hashtable<String, Integer>();
	}
	
	
	/**
	 * Viene chiamato dalla rete (oggetto Network) per indicizzare un servizio nella
	 * hashtable di questo nodo in corrispondenza di un certo nodo pubblicante.
	 *  
	 * @param service il servizio da inserire nella tabella.
	 * @param node il nodo che detiene fisicamente la risorsa.
	 */
	public void storeInDHT(Service service, int node){
		if(!dht.keySet().contains(service.getName()))
			dht.put(service.getName(), node);
	}
	
	
	
	
	/**
	 * Modifica la entry con indice "index" della finger table, ponendo come nuovo
	 * successore l'id passato da parametro.
	 * 
	 * @param index L'indice della finger table che va modificato.
	 * @param successorID L'id del nuovo successore del nodo fingerTable[index][START].
	 */
	public void setFingerEntry(int index, int successorID){
		fingerTable[index][SUCCESSOR] = successorID;
	}
	
	
	/**
	 * Scandisce la finger table di questo nodo e ritorna l'id
	 * del più vicino nodo precedente la chiave presente nella tabella:
	 * non si tratta dell'effettivo precedente, ma del più vicino 
	 * precedente CONOSCIUTO DA QUESTO NODO.
	 * 
	 * @param keyID L'id della chiave di ricerca da localizzare.
	 * @return L'id del più vicino nodo precedente la chiave raggiungibile da questo nodo.
	 */
	public int closestPrecedingFinger(int keyID){
		int m = fingerTable.length;
		int current;
		for(int i=m-1; i>=0; i--){
			current = fingerTable[i][SUCCESSOR];
			if(idCircle.belongTo(current, nodeID+1, keyID-1)){
				return current;
			}
		}
		return nodeID;
	}
	
	
	/**
	 * Consente di ottenere il nodo che è proprietario della risorsa.
	 * 
	 * @param serviceID L'id del servizio di cui si vuole sapere il nodo proprietario.
	 * @return Il nodo proprietario del servizio specificato.
	 */
	public int getNodeCorrespondingTo(int serviceID){
		return dht.get(serviceID);
	}
	
	
	/**
	 * Consente di conoscere l'ID del successore di questo nodo.
	 * 
	 * @return L'ID del successore diretto di questo nodo.
	 */
	public int getSuccessorID(){
		return fingerTable[0][SUCCESSOR];
	}
	
	/**
	 * Consente di ottenere l'id di questo nodo.
	 * @return L'identificativo di questo nodo.
	 */
	public int getId(){
		return nodeID;
	}
	
	/**
	 * Consente di accedere ai finger (cioè al campo SUCCESSOR) della finger table.
	 * 
	 * @param i L'indice della entry da estrarre.
	 * @return Il valore del campo SUCCESSOR della entry specificata della finger table.
	 */
	public int getFinger(int i){
		return fingerTable[i][SUCCESSOR];
	}
	
	/**
	 * Consente di accedere al campo START della finger-table.
	 * 
	 * @param i L'indice della entry da estrarre.
	 * @return Il valore del campo START della entry specificata della finger table.
	 */
	public int getStart(int i){
		return fingerTable[i][START];
	}
	
	/**
	 * Consente di ottenere una rapprestentazione dell'oggetto come stringa.
	 * 
	 * @return Una rappresentazione sotto forma di stringa dell'id del nodo.
	 */
	public String toString(){
		return ""+nodeID;
	}
	
	/**
	 * Consente di verificare l'uguaglianza fra due nodi.
	 * 
	 * @return True se i due nodi hanno lo stesso identificativo.
	 */
	public boolean equals(Object o){
		if(!(o instanceof Node))
			return false;
		Node n = (Node)o;
		return this.nodeID==n.nodeID;
	}
	
	/**
	 * Consente di accedere alla DHT del nodo.
	 * 
	 * @return La parte di DHT in possesso del nodo. 
	 */
	public Hashtable<String, Integer> getDHT(){
		return dht;
	}
}
