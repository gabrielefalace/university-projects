package ontology;

import java.util.*;

/**
 * Classe che rappresenta un Concetto di una ontologia. Consente legami con
 * un concetto superiore e molti sotto-concetti.
 * 
 * @author Gabriele
 *
 */
public class ConceptNode {
	
	/**
	 * Stringa che rappresenta l'effettivo concetto.
	 */
	String concept;
	
	/**
	 * Riferimento al concetto gerarchicamente superiore.
	 */
	ConceptNode parent;
	
	/**
	 * Lista di riferimenti ai concetti gerarchicamente inferiori.
	 */
	LinkedList<ConceptNode> subNodes;
	
	/**
	 * Crea un nuovo <i>concept node</i> senza nessun riferimento, né a nodi di livello superiore né inferiore.
	 * @param aConcept La stringa che descrive il concetto.
	 */
	public ConceptNode(String aConcept){
		concept = aConcept;
		parent = null;
		subNodes = new LinkedList<ConceptNode>();
	}
	
	/**
	 * Consente di impostare il concetto di questo nodo.
	 * 
	 * @param aConcept Il concetto da impostare.
	 */
	public void setConcept(String aConcept){
		concept = aConcept;
	}
	
	/**
	 * Consente di accedere alla stringa incapsulata in questo nodo. 
	 * 
	 * @return La stringa che contraddistingue questo nodo.
	 */
	public String getConcept(){
		return concept;
	}
	
	/**
	 * Consente di impostare il nodo genitore di questo concetto.
	 * @param aNode Il nodo da impostare come genitore.
	 */
	public void setParent(ConceptNode aNode){
		parent = aNode;
	}
	
	/**
	 * Consente di accedere al <i>super concetto</i> di questo nodo.
	 * 
	 * @return Il concetto genitore (di livello superiore) di questo nodo.
	 */
	public ConceptNode getParent(){
		return parent;
	}
	
	/**
	 * Consente di accedere alla lista dei <i>sotto concetti</i>.
	 * 
	 * @return La lista dei nodi che sono sotto-concetti.
	 */
	public LinkedList<ConceptNode> getSubNodes(){
		return subNodes;
	}
	
	/**
	 * Aggiunge un nodo come <i>sotto concetto</i>.
	 * 
	 * @param aNode Il nodo da aggiungere.
	 */
	public void addSubNode(ConceptNode aNode){
		subNodes.add(aNode);
	}
	
	/**
	 * Consente di aggiungere una Collection di nodi come <i>sotto concetti</i>.
	 * 
	 * @param nodes La Collection di nodi da aggiungere.
	 */
	public void addManySubNodes(Collection<ConceptNode> nodes){
		for(ConceptNode c: nodes)
			subNodes.add(c);
	}
	
	/**
	 * Consente di rimuovere il <i>sotto concetto</i>, specificandone la stringa che lo contraddistingue.
	 * 
	 * @param aConcept La stringa del concetto da rimuovere.
	 */
	public void removeSubNode(String aConcept){
		for(int i=0; i<subNodes.size(); i++)
			if(subNodes.get(i).getConcept().equals(aConcept))
				subNodes.remove(i);
	}
	
	/**
	 * Consente di conoscere il numero di <i>sotto concetti</i> di questo concetto.
	 * 
	 * @return il numero di <i>sotto concetti</i>.
	 */
	public int getNumberOfSubNodes(){
		return subNodes.size();
	}
	
	/**
	 * Consente di verificare se due nodi rappresentano lo stesso concetto.
	 * 
	 * @param o Il <i>concept node</i> con cui confrontare questo nodo. 
	 * @return True se i nodi rappresentano lo stesso concetto.
	 */
	public boolean equals(Object o){
		if(!(o instanceof ConceptNode))
			return false;
		else{
			ConceptNode c = (ConceptNode)o;
			return c.getConcept().equals(this.concept);
		}
	}
	
}
