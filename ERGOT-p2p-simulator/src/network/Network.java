package network;

import java.util.*;
import node.*;
import util.*;
import exceptions.*;

/**
 * Classe che rappresenta la rete Chord, mediante una aggregazione di peer.
 * 
 * @since  07-08-2008
 * @author Gabriele
 *
 */
public class Network {
	
	/**
	 * La lista dei nodi nella rete.
	 */
	protected LinkedList<Node> nodes;
	
	/**
	 * L'oggetto link IdentifierCircle usato per muoversi sul cerchio logico.
	 */
	protected IdentifierCircle idCircle;

	
	/**
	 * Costruttore.
	 * @param circle Il cerchio logico di identificatori.
	 */
	public Network(IdentifierCircle circle){
		idCircle = circle;
		nodes = new LinkedList<Node>();
	}
	
	/**
	 * Individua nella rete il nodo precedente la chiave più vicino in assoluto.
	 * Il nodo individuato è quello strettamente precedente, in quanto il metodo 
	 * belongTo() è inclusivo e n1.getSuccessorID() è un estremo incluso: perciò 
	 * se una chiave ha id uguale al successore di n1, il predecessore di tale
	 * chiave è n1. 
	 * 
	 * @param n Il nodo da cui viene inoltrata la richiesta.
	 * @param keyId L'id della risorsa da individuare.
	 * @return Il peer che è il predecessore della risorsa.
	 * @throws PeerNotFoundException Qualora il predecessore non venga trovato. Nota che di norma il predecessore di una chiave deve esistere sempre.
	 */
	public Node findPredecessor(Node n, int keyId)throws PeerNotFoundException{
		if(n.getId()==n.getSuccessorID()){
			return n;
		}
		else{
			Node n1 = n;
			while( !idCircle.belongTo(keyId, (n1.getId()+1)%idCircle.length(), n1.getSuccessorID()) ){
				int nextHop = n1.closestPrecedingFinger(keyId);
				n1 = getNodeWithId(nextHop);
				if(n1==null)
					break;
			}
			return n1;
		}
	}
	
	
	/**
	 * Individua il successore, cioè il responsabile, di una data risorsa.
	 * @param n Il nodo che inoltra la richiesta.
	 * @param keyId L'id della risorsa da individuare. 
	 * @return Il Node che è responsabile della risorsa.
	 * @throws PeerNotFoundException Qualora il successore non venga trovato. In situazioni normali, il successore di una chiave esiste sempre.
	 */
	public Node findSuccessor(Node n, int keyId)throws PeerNotFoundException{
		Node pre = findPredecessor(n, keyId);
		int succ = pre.getSuccessorID();
		return getNodeWithId(succ);
	}

	/**
	 * <b>Implementazione vuota!</b> Questo metodo deve spostare le chiavi da un nodo ad un altro quando un nuovo nodo
	 * si aggiunge alla rete e diventa responsabile di alcune chiavi. Il metodo dovrebbe pertanto anche individuare quali
	 * siano le risorse che vanno spostate di nodo in nodo. L'implementazione è stata trascurata perchè la simulazione si svolge
	 * secondo due fasi separate: prima si aggiungono peer alla rete, poi si pubblicano le risorse; per questo non è mai
	 * necessario spostare risorse da un nodo all'altro.
	 * @param n Il peer origine.
	 * @param p Il peer destinazione.
	 */
	public void moveKeys(int n, int p){

	}
	
	/**
	 * <b>Implementazione vuota!</b> Disconnette un nodo dalla rete. L'implementazione è stata
	 * trascurata perchè il simulatore è orientato alla valutazione degli algoritmi di ricerca 
	 * e pertanto non è mai necessario disconnettere dei nodi. 
	 * @param n Il nodo da disconnettere. 
	 */
	public void fail(Node n){
		
	}
	
	/**
	 * Aggiorna la finger table del nodo specificato inserendo un nuovo SUCCESSOR nella 
	 * finger table in corrispondenza di una prefissata entry.
	 * @param n Il nodo da aggiornare.
	 * @param i L'indice della entry da aggiornare.
	 * @param s Il valore della entry, cioè il nuovo i-esimo finger.
	 * @throws PeerNotFoundException Se è stato impossibile aggiornare la finger table di qualche nodo che andava aggiornato.
	 */
	public void updateFingerTable(Node n, int i, int s)throws PeerNotFoundException{
		int start = (n.getId()+1)%idCircle.length();
		if( idCircle.belongTo(s, start, n.getFinger(i) ) ){
			n.setFingerEntry(i, s);
			Node pre = findPredecessor(n, n.getId());
			if(pre != null)
				updateFingerTable(pre, i, s);
		}
	}
	
	
	
	/**
	 * Notifica i peer che potrebbero essere interessati ad aggiornarsi
	 * in base al nuovo nodo che si è aggiunto alla rete.
	 * @param n Il nodo che si è aggiunto e che inoltra la notifica.
	 * @return Il vettore di identificativi di peer notificati.
	 * @throws PeerNotFoundException Se è stato impossibile notificare qualche nodo che andava notifcato.
	 */
	public int[] notify(Node n)throws PeerNotFoundException{
		int[] notifiedNodes = new int[idCircle.getBits()];
		for(int i=0; i<idCircle.getBits(); i++){
			int goBack = idCircle.retreat( n.getId(), ((int)Math.pow(2, i)) ); 
			Node predecessor = findPredecessor(n, goBack);
			notifiedNodes[i] = predecessor.getId();
			if(!predecessor.equals(n)){
				updateFingerTable(predecessor, i, n.getId());
			}
		}
		return notifiedNodes;
	}
	
	
	
	/**
	 * Inizializza la finger table di un nuovo nodo che si sta aggiungendo alla rete.
	 *  
	 * @param n Il nuovo nodo che si deve aggiungere.
	 * @param n1 Il nodo "di appoggio", che n deve conoscere per aggiungersi alla rete.
	 * @throws PeerNotFoundException Se non si riesce ad inizializzare la finger table a causa dell'impossibilità
	 * 		   di individuare il successore per qualche entry della tabella.
	 */
	public void initFingerTable(Node n, Node n1)throws PeerNotFoundException{
		Node n_successor = findSuccessor(n1, n.getStart(0));
		n.setFingerEntry(0, n_successor.getId());
		for(int  i=0; i<idCircle.getBits()-1; i++){
			boolean sameFinger = idCircle.belongTo(n.getStart(i+1), n.getId(), n.getFinger(i)-1);
			if(sameFinger==true)
				n.setFingerEntry(i+1, n.getFinger(i));
			else{
				Node i_successor = findSuccessor(n1, n.getStart(i+1));
				n.setFingerEntry(i+1, i_successor.getId());
			}	
		}
	}
	
	
	/**
	 * Aggiunge un nodo alla rete.
	 * 
	 * @param n Il nodo da aggiungere alla rete.
	 * @throws NodeAlreadyPresentException Se nella rete è presente già un nodo con quell'id.
	 * @throws PeerNotFoundException Se si verifica un errore nell'inizializzazione della finger table o
	 * 		   nella notifica agli altri nodi.
	 */
	public void join(Node n) throws NodeAlreadyPresentException, PeerNotFoundException{
		if(containsNode(n.getId()))
			throw new NodeAlreadyPresentException();
		if(!nodes.isEmpty()){
			initFingerTable(n, nodes.get(0));
			nodes.add(n);
			notify(n);
		}
		else{
			for(int i=0; i<idCircle.getBits(); i++)
				n.setFingerEntry(i, n.getId());
			nodes.add(n);
		}
	}
	
	
	/**
	 * Consente di ottenere un riferimento ad un nodo di cui si conosce l'id.
	 * 
	 * @param id L'id del nodo da trovare.
	 * @return Il riferimento al nodo cercato, null se il nodo non è presente.
	 * @throws PeerNotFoundException Se nella rete non è presente un peer con l'id specificato.
	 */
	public Node getNodeWithId(int id)throws PeerNotFoundException{
		for(Node iter: nodes)
			if(iter.getId()==id)
				return iter;
		throw new PeerNotFoundException();
	}
	
	/**
	 * Consente di sapere se la rete contiene un nodo con un determinato id.
	 * 
	 * @param id L'identificativo da verificare.
	 * @return true Se la rete contiene un nodo con l'id specificato, false altrimenti.
	 */
	public boolean containsNode(int id){
		for(Node n: nodes)
			if(n.getId()==id)
				return true;
		return false;
	}
	
	
	/**
	 * Consente di accedere all'insieme dei nodi della rete.
	 * 
	 * @return La lista di nodi della rete.
	 */
	public Node[] getNodeList(){
		Node[] nodeArray = new Node[nodes.size()];
		for(int i=0; i<nodeArray.length; i++)
			nodeArray[i] = nodes.get(i);
		return nodeArray;
	}
}
