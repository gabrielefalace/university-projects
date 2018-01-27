package simulation;

import java.util.*;
import exceptions.ConceptNotFoundException;
import exceptions.NodeAlreadyPresentException;
import exceptions.PeerNotFoundException;
import exceptions.PeerWithNoSemanticLinkException;
import exceptions.TooSmallIdentifierCircleException;
import exceptions.TooSmallOntologyException;
import semanticNode.SemanticNode;
import semanticOverlayNetwork.*;
import semanticOverlayNetwork.SemanticOverlayNetwork.SemanticBasedResult;
import ontology.*;
import util.*;
import node.*;
import parameters.*;

/**
 * Classe che gestisce tutta la simulazione.
 * 
 * @author Gabriele
 *
 */
public class SimulationManager {

	/**
	 * Il costruttore dell'ambienete di simulazione. Vedi {@link SimulationEnvironmentBuilder}.
	 */
	SimulationEnvironmentBuilder builder;
	
	/**
	 * Oggetto che gestisce l'esecuzione dei lookup. Vedi {@link LookupHandler}
	 */
	LookupHandler lookupHandler;
	
	/**
	 * La SON. Vedi {@link SemanticOverlayNetwork}
	 */
	SemanticOverlayNetwork son;
	
	/**
	 * La Category Ontology. Vedi {@link Ontology}
	 */
	Ontology categoryOntology;
	
	/**
	 * La Domain Ontology. Vedi {@link Ontology}
	 */
	Ontology domainOntology;
	
	/**
	 * L'insieme dei servizi da pubblicare.
	 */
	ArrayList<Service> services;
	
	/**
	 * Il DTO con i parametri della rete.
	 */
	NetworkDTO networkDTO;
	
	/**
	 * Il DTO con i parametri delle ontologie.
	 */
	OntologyDTO ontologyDTO;
	
	/**
	 * Il DTO con i parametri dei servizi.
	 */
	ServicesDTO serviceDTO;
	
	/**
	 * Il DTO con i parametri dei lookup.
	 */
	LookupDTO lookupDTO;
	
	/**
	 * Costruisce un <i>SimulationManager</i> che si occupa sia di costruire l'ambiene di simulazione, sia
	 * di eseguire i lookup: in particolare, in questo costruttore, vengono anche pubblicati i servizi.
	 * @param ontoDTO Il DTO con i parametri delle ontologie.
	 * @param netDTO Il DTO con i parametri della rete.
	 * @param servDTO Il DTO con i parametri dei servizi.
	 * @param lookupDTO Il DTO con i parametri dei lookup.
	 * @throws TooSmallOntologyException
	 * @throws TooSmallIdentifierCircleException
	 * @throws PeerNotFoundException
	 * @throws NodeAlreadyPresentException
	 */
	public SimulationManager(OntologyDTO ontoDTO, NetworkDTO netDTO, ServicesDTO servDTO, LookupDTO lookupDTO)
	throws TooSmallOntologyException, TooSmallIdentifierCircleException, PeerNotFoundException, NodeAlreadyPresentException
	{
		this.lookupDTO = lookupDTO;
		networkDTO = netDTO;
		serviceDTO = servDTO;
		ontologyDTO = ontoDTO;
		
		builder = new SimulationEnvironmentBuilder(serviceDTO, networkDTO, ontologyDTO);
		
		//builder.buildSimulationEnvironment();
		
		son = builder.getSemanticOverlayNetwork();
		categoryOntology = builder.getCategoryOntology();
		domainOntology = builder.getDomainOntology();
		services = builder.getServices();
		
		for(Service service: services){
			SemanticNode publisher = son.getRandomPeer();
			son.publish(publisher, service);
		}
		
		lookupHandler = new LookupHandler(son, this.lookupDTO);
	}

	/**
	 * Esegue i lookup <i>Semantic-Based</i> secondo quanto indicato nel DTO.
	 * @return L'insieme dei risultati, con ciascun risultato incapsulato in un oggetto {@link SemanticBasedResult}}.
	 * @throws ConceptNotFoundException Se il concetto di query non appartiene all'ontologia.
	 * @throws PeerNotFoundException Se non è stato possibile individuare un nodo dovuto nell'esecuzione della query. 
	 * @throws PeerWithNoSemanticLinkException Se per iniziare un lookup è stato scelto un peer senza semantic link.
	 */
	public ArrayList<SemanticOverlayNetwork.SemanticBasedResult> executeSemanticBasedLookups()
	throws ConceptNotFoundException, PeerNotFoundException, PeerWithNoSemanticLinkException
	{
		System.out.println("Inizio esecuzione lookup");
		return lookupHandler.executeSemanticBasedLookups();
	}
	
	
	public ArrayList<SemanticOverlayNetwork.CategoryBasedResult> executeEnhancedCategoryBasedLookups() 
	throws PeerNotFoundException, PeerWithNoSemanticLinkException, ConceptNotFoundException
	{
		System.out.println("Inizio esecuzione lookup");
		return lookupHandler.executeEnhancedCategoryBasedLookups();
	}
	
	/**
	 * Consente di accedere alle medie (hop per hop) del recall.
	 * @param results L'insieme dei risultati di tutti i lookup, su cui calcolare le medie.
	 * @return Un vettore con le medie.
	 */
	public float[] getAverageTimedResult(ArrayList<SemanticOverlayNetwork.SemanticBasedResult> results){
		
		int maxHop = 0;
		int currentHop = 0;
		
		for(SemanticOverlayNetwork.SemanticBasedResult res: results)
			if(res.getTimedRecall().size() > maxHop)
				maxHop = res.getTimedRecall().size();
		
		float[] report = new float[maxHop];
		
		while(currentHop < maxHop){
			float num = 0; 
			float den = 0;
			for(SemanticOverlayNetwork.SemanticBasedResult res: results){
				if(res.getTimedRecall().size() > currentHop){
					num += res.getTimedRecall().get(currentHop);
					den++;
				}
			}
			report[currentHop] = num/den;
			currentHop++;
		}
		return report;	
	}
	
	/**
	 * Consente di accedere alle medie (hop per hop) dei messaggi inviati.
	 * @param results L'insieme dei risultati di tutti i lookup, su cui calcolare le medie.
	 * @return Un vettore con le medie.
	 */
	public float[] getAverageTimedMessages(ArrayList<SemanticOverlayNetwork.SemanticBasedResult> results){
		int maxHop = 0;
		int currentHop = 0;
		
		for(SemanticOverlayNetwork.SemanticBasedResult res: results)
			if(res.getTimedNumberOfMessages().size() > maxHop)
				maxHop = res.getTimedNumberOfMessages().size();
		
		float[] report = new float[maxHop];
		
		while(currentHop < maxHop){
			float num = 0; 
			float den = 0;
			for(SemanticOverlayNetwork.SemanticBasedResult res: results){
				if(res.getTimedNumberOfMessages().size() > currentHop){
					num += res.getTimedNumberOfMessages().get(currentHop);
					den++;
				}
			}
			report[currentHop] = num/den;
			currentHop++;
		}
		return report;	
	}
	
	/**
	 * Consente di accedere alle medie (hop per hop) dei servizi scoperti.
	 * @param results L'insieme dei risultati di tutti i lookup, su cui calcolare le medie.
	 * @return Un vettore con le medie.
	 */
	public float[] getAverageTimedServices(ArrayList<SemanticOverlayNetwork.SemanticBasedResult> results){
		int maxHop = 0;
		int currentHop = 0;
		
		for(SemanticOverlayNetwork.SemanticBasedResult res: results)
			if(res.getTimedNumberOfServices().size() > maxHop)
				maxHop = res.getTimedNumberOfServices().size();
		
		float[] report = new float[maxHop];
		
		while(currentHop < maxHop){
			float num = 0; 
			float den = 0;
			for(SemanticOverlayNetwork.SemanticBasedResult res: results){
				if(res.getTimedNumberOfServices().size() > currentHop){
					num += res.getTimedNumberOfServices().get(currentHop);
					den++;
				}
			}
			report[currentHop] = num/den;
			currentHop++;
		}
		return report;	
	}
	
	/**
	 * Calcola la deviazione standard del recall rispetto alla media, per ogni hop dei lookup.
	 * @param results Insieme dei risultati.
	 * @param recall_report Oggetto che contiene le medie dei recall.
	 * @return Un vettore con le deviazioni standard.
	 */
	public double[] getRecallDeviation(ArrayList<SemanticOverlayNetwork.SemanticBasedResult> results, float[] recall_report){
		double[] deviations = new double[recall_report.length];
		for(int currentHop=0; currentHop < recall_report.length; currentHop++){
			double mean_sq = Math.pow(recall_report[currentHop], 2);
			double N = 0;
			double sum = 0;
			for(int i=0; i<results.size(); i++){
				if(results.get(i).getTimedRecall().size() > currentHop){
					double tmp = results.get(i).getTimedRecall().get(currentHop);
					double term = Math.pow(tmp, 2) - mean_sq ;
					sum += term;
					N++;
				}
			}
			deviations[currentHop] = Math.sqrt(sum/N);
		}
		return deviations;
	}
	
	
	/**
	 * Calcola la deviazione standard dei messaggi rispetto alla media, per ogni hop dei lookup.
	 * @param results Insieme dei risultati.
	 * @param message_report Oggetto che contiene le medie dei messaggi.
	 * @return Un vettore con le deviazioni standard.
	 */
	public double[] getMessagesDeviation(ArrayList<SemanticOverlayNetwork.SemanticBasedResult> results, float[] message_report){
		double[] deviations = new double[message_report.length];
		for(int currentHop=0; currentHop < message_report.length; currentHop++){
			double mean_sq = Math.pow(message_report[currentHop], 2);
			double N = 0;
			double sum = 0;
			for(int i=0; i<results.size(); i++){
				if(results.get(i).getTimedNumberOfMessages().size() > currentHop){
					double tmp = results.get(i).getTimedNumberOfMessages().get(currentHop);
					double term = Math.pow(tmp, 2) - mean_sq ;
					sum += term;
					N++;
				}
			}
			deviations[currentHop] = Math.sqrt(sum/N);
		}
		return deviations;
	}
	
	/**
	 * Calcola la deviazione standard dei servizi rispetto alla media, per ogni hop dei lookup.
	 * @param results Insieme dei risultati.
	 * @param services_report Oggetto che contiene le medie dei servizi.
	 * @return Un vettore con le deviazioni standard.
	 */
	public double[] getServicesDeviation(ArrayList<SemanticOverlayNetwork.SemanticBasedResult> results, float[] services_report){
		double[] deviations = new double[services_report.length];
		for(int currentHop=0; currentHop < services_report.length; currentHop++){
			double mean_sq = Math.pow(services_report[currentHop], 2);
			double N = 0;
			double sum = 0;
			for(int i=0; i<results.size(); i++){
				if(results.get(i).getTimedNumberOfServices().size() > currentHop){
					double tmp = results.get(i).getTimedNumberOfServices().get(currentHop);
					double term = Math.pow(tmp, 2) - mean_sq ;
					sum += term;
					N++;
				}
			}
			deviations[currentHop] = Math.sqrt(sum/N);
		}
		return deviations;
	}
	
	/**
	 * Calcola il numero totale di peer che partecipano nella SON. 
	 * @return Il numero totale di peer partecipanti.
	 */
	public int computeNumberOfParticipatingPeers(){
		int nonParticipating = 0;
		Node[] nodes = son.getNodeList();
		for(int i=0; i<nodes.length; i++){
			SemanticNode sn = (SemanticNode)nodes[i];
			if(sn.getSemanticLinksCategories().length==0)
				nonParticipating++;
		}
		return (nodes.length - nonParticipating);
	}
	
	/**
	 * Consente di sapere il numero medio di link semantici che hanno i peer della rete.
	 * @return Il numero medio di link semantici dei peer della rete.
	 */
	public float computeAverageSemanticLinksPerNode(){
		float totalLinks = 0;
		Node[] nodes = son.getNodeList();
		LinkedList<SemanticNode> nodeList = new LinkedList<SemanticNode>();
		for(int i=0; i<nodes.length; i++)
			nodeList.add((SemanticNode)nodes[i]);
		for(SemanticNode current: nodeList){
			String[] categories = current.getSemanticLinksCategories();
			for(String category: categories){
				int nodeLinks = current.getSemanticLinksOn(category).length;
				totalLinks += nodeLinks;
			}
		}
		return totalLinks/computeNumberOfParticipatingPeers();
	}
}