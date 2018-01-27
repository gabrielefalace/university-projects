package simulation;

import semanticNode.*;
import java.util.*;

import exceptions.ConceptNotFoundException;
import exceptions.PeerNotFoundException;
import exceptions.PeerWithNoSemanticLinkException;
import parameters.*;
import semanticOverlayNetwork.*;
import semanticOverlayNetwork.SemanticOverlayNetwork.SemanticBasedResult;
import util.Service;

public class LookupHandler {

	/**
	 * Il DTO con i parametri per i lookup.
	 */
	private LookupDTO lookupDTO;
	
	/**
	 * La SON (<i>Semantic Overlay Network</i>) su cui effettuare i lookup.
	 */
	private SemanticOverlayNetwork son;
	
	/**
	 * L'elenco dei nodi da cui devono partire i lookup.
	 */
	private SemanticNode[] startingNodes;
	
	
	
	/**
	 * Costruisce un oggetto per la gestione dei lookup.
	 * @param son La SON su cui eseguire i lookup.
	 * @param lookupDTO Il DTO con i parametri dei lookup.
	 * @throws PeerNotFoundException Qualora non si siano individuati dei nodi da cui far partire i lookup.
	 */
	public LookupHandler(SemanticOverlayNetwork son, LookupDTO lookupDTO)
	throws PeerNotFoundException{
		this.lookupDTO = lookupDTO;
		this.son = son;
		
		startingNodes = new SemanticNode[lookupDTO.getNumberOfLookups()];
		int tentatives = 0;
		int maxTentatives = son.getNodeList().length;
		
		System.out.println("\nSelezione degli starting node per i lookup");
		for(int i=0; i<lookupDTO.getNumberOfLookups();){
			SemanticNode chosen = son.getRandomPeer();
			tentatives++;
			System.out.println("Provando il nodo "+chosen.getId()+" per il "+tentatives+"-esimo tentativo per la posizione "+i);
			if(tentatives==maxTentatives)
				throw new PeerNotFoundException("Impossibile individuare un peer per effettuare il lookup");
			
			if(chosen.getSemanticLinksCategories().length > 0){
				startingNodes[i] = chosen;
				tentatives = 0;
				i++;
			}
		}
	}
	
	/**
	 * Esegue tutti i lookup semantic-based sulla SON, secondo i parametri dettati dai DTO.
	 * @return Un insieme di oggetti {@link SemanticBasedResult}} con i risultati di tutti i lookup.
	 * @throws PeerNotFoundException Se non si può cominciare il lookup a causa della mancanza di peer iniziali.
	 * @throws ConceptNotFoundException Se la categoria cercata non appartiene all'ontologia.
	 */
	public ArrayList<SemanticOverlayNetwork.SemanticBasedResult> executeSemanticBasedLookups()
	throws PeerNotFoundException, ConceptNotFoundException, PeerWithNoSemanticLinkException
	{
		ArrayList<SemanticOverlayNetwork.SemanticBasedResult> results = new ArrayList<SemanticOverlayNetwork.SemanticBasedResult>();		
		
		int numLookup = 1;
		for(SemanticNode start: startingNodes){
			
			System.out.println("In esecuzione il lookup "+numLookup);
			numLookup++;
			
			String[] categories = start.getSemanticLinksCategories();
			
			if(categories.length==0)
				throw new PeerWithNoSemanticLinkException();
			
			String category = categories[new Random().nextInt(categories.length)];
			
			SemanticOverlayNetwork.SemanticBasedResult curr_result = son.semanticBasedLookup(start, category, lookupDTO.getSimilarityThreshold(), lookupDTO.getTTL());
			results.add(curr_result);
		}
		return results;
	}
	

	public ArrayList<SemanticOverlayNetwork.CategoryBasedResult> executeEnhancedCategoryBasedLookups()
	throws PeerNotFoundException, PeerWithNoSemanticLinkException, ConceptNotFoundException
	{
		ArrayList<SemanticOverlayNetwork.CategoryBasedResult> results = new ArrayList<SemanticOverlayNetwork.CategoryBasedResult>();
		
		int numLookup = 1;
		for(SemanticNode start: startingNodes){
			
			System.out.println("In esecuzione il lookup "+numLookup);
			numLookup++;
			
			String[] categories = start.getSemanticLinksCategories();
			
			if(categories.length==0)
				throw new PeerWithNoSemanticLinkException();
			
			String category = categories[new Random().nextInt(categories.length)];
			
			SemanticOverlayNetwork.CategoryBasedResult curr_result = son.enhancedCategoryBasedLookup(start, category, lookupDTO.getSimilarityThreshold());
			results.add(curr_result);
		}
		
		return results;
	}

}
