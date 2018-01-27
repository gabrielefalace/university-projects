package ontology;

import java.util.*;
import exceptions.*;

/**
 * Classe che rappresenta una ontologia. 
 * 
 * @author Gabriele
 *
 */
public class Ontology {

	/**
	 * Rappresenta il numero di concetti presenti nell'ontologia.
	 */
	private int numberOfConcepts;
	
	/**
	 * Rappresenta la profondità della tassonomia dell'ontologia.
	 */
	private int depth;
	
	/**
	 * L'albero che realizza la struttura vera e propria.
	 */
	private ConceptTree tree;
	
	/**
	 * La radice dell'albero, necessaria a crearlo.
	 */
	private ConceptNode root;
	
	/**
	 * Una mappa che consente di recuperare un riferimento ad un nodo semplicemente indicando la stringa.
	 */
	private HashMap<String, ConceptNode> map;
	
	
	
	/**
	 * Costruisce un oggetto Ontology strutturato come un albero, con numero di concetti e profondità dell'albero 
	 * stabiliti da parametro. 
	 * @param numberOfConcepts Il numero di concetti nell'ontologia.
	 * @param depth La profondità dell'albero dell'ontologia.
	 */
	public Ontology(int numberOfConcepts, int depth){
		this.numberOfConcepts = numberOfConcepts;
		this.depth = depth;
		map = new HashMap<String, ConceptNode>();
		LinkedList<String> concepts = generateConcepts(numberOfConcepts);
		int c = approximateC();
		initializeRootAndTree(concepts.remove(0));
		for(int i=1; i<=depth; i++)
			createLevel(i, c, concepts);
		settleRemainingNodes(concepts);
	}
	
	/**
	 * Consente di generare un certo numero di concetti.
	 * @param numberOfConcepts Il numero di concetti da generare.
	 * @return La lista di concetti generati.
	 */
	private LinkedList<String> generateConcepts(int numberOfConcepts){
		LinkedList<String> concepts = new  LinkedList<String>();
		for(int i=0; i<numberOfConcepts; i++){
			concepts.add("concept_"+i);
		}
		return concepts;
	}
	
	/**
	 * Consente di sistemare in modo random i concetti rimasti dopo la costruzione approssimata dell'albero.
	 * @param concepts La lista di concetti rimanenti.
	 */
	private void settleRemainingNodes(LinkedList<String> concepts){
		LinkedList<ConceptNode> lastNodes = tree.getNodesAtLevel(depth-1);
		for(int i=0; i<concepts.size(); i++){
			String cs = concepts.remove(0);
			ConceptNode n = new ConceptNode(cs);
			map.put(cs, n);
			int randomIndex = (int)Math.floor(Math.random()*lastNodes.size());
			ConceptNode chosenParent = lastNodes.get(randomIndex);
			tree.addNodeToParent(n, chosenParent);
		}
	}
	
	
	/**
	 * Crea il livello i-esimo dell'albero.
	 * @param i L'indice del livello da creare.
	 * @param c L'esponente approssimato per assegnare un quantitativo di nodi al livello (vengono assegnati 2^c concetti).
	 * @param concepts La lista di concetti rimanenti.
	 */
	private void createLevel(int i, int c, LinkedList<String> concepts){
		int quantity = (int)Math.pow(c, i);
		LinkedList<ConceptNode> levelNodes = tree.getNodesAtLevel(i-1);
		for(int j=0; j<quantity && !concepts.isEmpty(); j++){
			String currentConcept = concepts.remove(0);
			ConceptNode node = new ConceptNode(currentConcept);
			map.put(currentConcept, node);
			int randomIndex = (int)Math.floor(Math.random()*levelNodes.size());
			ConceptNode chosenParent = levelNodes.get(randomIndex);
			tree.addNodeToParent(node, chosenParent);
		}
	}
	
	/**
	 * Inizializza la radice dell'albero di concetti e l'albero stesso.
	 * @param concept Il concetto che deve fare da radice all'albero.
	 */
	private void initializeRootAndTree(String concept){
		root = new ConceptNode(concept);
		map.put(concept, root);
		tree = new ConceptTree(root);
	}
	
	/**
	 * Consente di stimare un parametro, <i>c</i>, che serve per costruire l'albero dell'ontologia. 
	 * @return Il valore del parametro c.
	 */
	private int approximateC(){
		int bestC = 1;
		int localC = 1;
		for(int i=0; i<depth; i++){
			int effective = (int)Math.round((Math.pow(localC, depth)-1)/(localC-1));
			if(numberOfConcepts > effective){
				bestC = localC;
				localC++;
			}
			else
				break;
		}
		return bestC;
	}
	
	/**
	 * Consente di scegliere un concetto casuale. 
	 * @return La stringa relativa ad un concetto.
	 */
	public String getRandomConcept(){
		Set<String> keySet = map.keySet();
		int random = new Random().nextInt(keySet.size());
		String[] keys = new String[keySet.size()]; 
		keySet.toArray(keys);
		return keys[random];
	}
	
	/**
	 * Consente di ottenere un certo numero di concetti scelti casualmente dall'ontologia.
	 * Mettendo a <i>true</i> il flag <i>different</i> si specifica il vincolo che i concetti scelti devono
	 * essere tutti diversi tra loro.
	 * @param numberOfConcepts Numero di concetti da scegliere.
	 * @param different Se è true indica che i concetti scelti devono essere diversi.
	 * @return Un elenco di concetti scelti casualmente.
	 */
	public String[] getRandomConcepts(int numberOfConcepts, boolean different)
	throws TooSmallOntologyException
	{
		if(numberOfConcepts > this.numberOfConcepts)
			throw new TooSmallOntologyException();
		
		LinkedList<String> chosenConcepts = new LinkedList<String>();
		String[] concepts = new String[numberOfConcepts];
		while(chosenConcepts.size() < numberOfConcepts){
			String s = getRandomConcept();
			if(different==true && chosenConcepts.contains(s))
				continue;
			else
				chosenConcepts.add(s);
		}
		//chosenConcepts.toArray(concepts);
		for(int i=0; i<concepts.length; i++)
			concepts[i] = chosenConcepts.get(i);
		return concepts;
	}
	
	/**
	 * Calcola la similarità tra due concetti
	 * @param c1 Il primo concetto, come stringa.
	 * @param c2 Il secondo concetto, come stringa.
	 * @return Il valore di similarità tra i due concetti.
	 * @throws ConceptNotFoundException Se una delle due stringhe non corrisponde ad un concetto.
	 */
	public double similarity(String c1, String c2) throws ConceptNotFoundException{
		ConceptNode n1 = tree.getNodeWithId(c1);
		ConceptNode n2 = tree.getNodeWithId(c2);
		if(n1==null || n2==null)
			return 0;
		else
			return tree.similarity(n1, n2);
	}
	
	/**
	 * Consente di ottenere un riferimento ad un concetto tramite il suo nome.
	 * @param conceptID il nome del concetto.
	 * @return Il riferimento al <i>concept node</i> che corrisponde al nome specificato. 
	 * @throws ConceptNotFoundException Se la stringa passata non corrisponde ad alcun concetto.
	 */
	public ConceptNode getConceptWithString(String conceptID)throws ConceptNotFoundException{
		ConceptNode n = map.get(conceptID);
		if(n==null)
			throw new ConceptNotFoundException();
		else
			return n;
	}
	
	/**
	 * Consente di sapere i nomi dei concetti che sono abbastanza simili (secondo una soglia di similarità) al concetto
	 * indicato.
	 * @param concept Il concetto di cui si vogliono conoscere i simili.
	 * @param threshold La soglia di similarità per selezionare i concetti simili.
	 * @return L'elenco dei nomi dei concetti simili.
	 */
	public String[] getConceptsSimilarTo(String concept, double threshold)throws ConceptNotFoundException{
		String[] concepts;
		LinkedList<String> conceptsList = new LinkedList<String>();
		LinkedList<ConceptNode> queue = new LinkedList<ConceptNode>();
		queue.addLast(tree.root);
		while(!queue.isEmpty()){
			ConceptNode currentNode = queue.removeFirst();
			if(similarity(currentNode.getConcept(), concept) >= threshold)
				conceptsList.add(currentNode.getConcept());
			for(ConceptNode subNode: currentNode.getSubNodes())
				queue.addLast(subNode);
		}
		if(conceptsList.size()==0) 
			throw new ConceptNotFoundException();
		else{
			concepts = new String[conceptsList.size()];
			for(int i=0; i < conceptsList.size(); i++)
				concepts[i] = conceptsList.get(i);
			return concepts;
		}
	}
}
