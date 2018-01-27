package ontology;

import java.util.*;

import exceptions.ConceptNotFoundException;

/**
 * Classe che rappresenta una tassonomia di concetti raggruppati in una ontologia.
 * 
 * @author Gabriele
 *
 */
public class ConceptTree {

	/**
	 * Nodo che rappresenta la radice dell'albero.
	 */
	ConceptNode root;
	
	/**
	 * Attributo derivato che rappresenta la profondità dell'albero.
	 */
	int depth;
	
	/**
	 * Attributo derivato che rappresenta il numero di nodi dell'albero.
	 */
	int nodes;
	
	/**
	 * Costruisce un albero avente solo la radice, profondità 0 e numero di nodi pari a 1.
	 * @param aNode la radice dell'albero.
	 */
	public ConceptTree(ConceptNode aNode){
		root = aNode;
		depth = 0;
		nodes = 1;
	}
	

	/**
	 * Consente di ottenere i tutti i nodi di un dato livello.
	 * @param i Ll livello cui si vogliono i nodi.
	 * @return La lista dei nodi al livello i-esimo.
	 */
	public LinkedList<ConceptNode> getNodesAtLevel(int i){
		int level = 0;
		LinkedList<ConceptNode> currentNodes = new LinkedList<ConceptNode>();
		currentNodes.add(root);
		while(level < i){
			currentNodes = getSubNodesOf(currentNodes);
			level++;	
		}
		return currentNodes;
	}
	
	/**
	 * Consente di posizionare un nodo come figlio di un altro.
	 * @param aNode Il nodo da inserire.
	 * @param parent Il nodo scelto come parent.
	 */
	public void addNodeToParent(ConceptNode aNode, ConceptNode parent){
		aNode.setParent(parent);
		parent.addSubNode(aNode);
		LinkedList<ConceptNode> lastLevel = getLastLevelNodes();
		nodes++;
		if(lastLevel.contains(parent))
			depth++;
	}
	
	/**
	 * Consente di ottenere i nodi dell'ultimo livello (le foglie).
	 * @return La lista di nodi dell'ultimo livello.
	 */
	public LinkedList<ConceptNode> getLastLevelNodes(){
		return getNodesAtLevel(depth);
	}
	
	/**
	 * Consente di ottenere tutti i sotto-nodi di un gruppo di nodi.
	 * @param list I nodi di cui si vogliono i sotto-nodi.
	 * @return La lista complessiva di tutti i sotto-nodi.
	 */
	private LinkedList<ConceptNode> getSubNodesOf(LinkedList<ConceptNode> list){
		LinkedList<ConceptNode> subs = new LinkedList<ConceptNode>();
		for(ConceptNode c: list){
			LinkedList<ConceptNode> tmp = c.getSubNodes();
			for(ConceptNode n: tmp)
				subs.add(n);
		}	
		return subs;
	}
	
	/**
	 * Consente di ottenere il primo antenato comune dati due nodi.
	 * @param c1 Il primo dei due nodi.
	 * @param c2 Il secondo dei due nodi.
	 * @return Il nodo che è il più basso antenato comune nell'albero.
	 */
	public ConceptNode getFirstCommonAncestor(ConceptNode c1, ConceptNode c2){
		LinkedList<ConceptNode> pathC1 = new LinkedList<ConceptNode>();
		LinkedList<ConceptNode> pathC2 = new LinkedList<ConceptNode>();
		ConceptNode c = c1;
		while(!c.equals(root)){
			pathC1.addFirst(c);
			c = c.getParent();
		}
		c = c2;
		while(!c.equals(root)){
			pathC2.addFirst(c);
			c = c.getParent();
		}
		pathC2.addFirst(root);
		pathC1.addFirst(root);
		ConceptNode lastCommon = pathC1.get(0);
		for(int i=0; i<pathC1.size() && i<pathC2.size(); i++){
			if(pathC1.get(i).equals(pathC2.get(i)))
				lastCommon = pathC1.get(i);
			else
				break;
		}
		return lastCommon;
	}
	

	/**
	 * Calcola l'Information Content di un nodo.
	 * @param c Il nodo di cui calcolare l'IC.
	 * @return Il valore di IC calcolato.
	 */
	private double IC(ConceptNode c){
		double ic = (1 - (Math.log(c.getNumberOfSubNodes()+ 1) / Math.log(2))/(Math.log(nodes) / Math.log(2)));
		return ic;
	}
	
	/**
	 * Calcola la similarità tra due concetti dell'albero.
	 * @param c1 Il primo dei due nodi di cui calcolare la similarità.
	 * @param c2 Il secondo dei due nodi di cui calcolare la similarità
	 * @return Il valore di similarità tra i due nodi-concetto.
	 */
	public double similarity(ConceptNode c1, ConceptNode c2){
		ConceptNode ancestor = getFirstCommonAncestor(c1, c2);
		double ic_ancestor = IC(ancestor);
		double ic_c1 = IC(c1);
		double ic_c2 = IC(c2);
		double sim = (3*ic_ancestor)-ic_c1-ic_c2;
		return sim;
	}
	
	/**
	 * Consente di ottenere un riferimento ad un nodo avente la stringa specificata.
	 * @param conceptID La stringa che caratterizza il nodo cercato.
	 * @return Il nodo con la stringa specificata.
	 * @throws ConceptNotFoundException Qualora è richiesta una stringa che non corrisponde ad alcun concetto nell'albero.
	 */
	public ConceptNode getNodeWithId(String conceptID)throws ConceptNotFoundException{
		ConceptNode node = null;
		LinkedList<ConceptNode> queue = new LinkedList<ConceptNode>();
		queue.add(root);
		while(!queue.isEmpty()){
			ConceptNode current = queue.removeFirst();
			LinkedList<ConceptNode> subs = current.getSubNodes();
			for(ConceptNode c: subs)
				queue.addLast(c);
			if(conceptID.equals(current.getConcept())){
				node = current;
				break;
			}
		}
		if(node != null)
			return node;
		else
			throw new ConceptNotFoundException();
	}
}
