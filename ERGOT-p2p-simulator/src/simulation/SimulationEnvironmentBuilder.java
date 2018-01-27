package simulation;

import parameters.*;
import semanticNode.SemanticNode;
import semanticOverlayNetwork.*;
import ontology.*;
import util.*;
import java.util.*;

import exceptions.NodeAlreadyPresentException;
import exceptions.PeerNotFoundException;
import exceptions.TooSmallIdentifierCircleException;
import exceptions.TooSmallOntologyException;


/**
 * Classe che gestisce la creazione e la configurazione della Semantic Overlay Network con i parametri di simulazione.
 * 
 * @author Gabriele
 *
 */
public class SimulationEnvironmentBuilder {

	
	/**
	 * Il DTO con i parametri dei servizi.
	 */
	private ServicesDTO serviceDTO;
	
	
	/**
	 * Il DTO con i parametri costruttivi della rete.
	 */
	private NetworkDTO networkDTO;
	
	
	/**
	 * Il DTO con i parametri costruttivi delle ontologie.
	 */
	private OntologyDTO ontologyDTO;
	
	/**
	 * L'ontologia delle categorie (CO - <i>Category Ontology</i>).
	 */
	private Ontology categoryOntology;
	
	/**
	 * L'ontologia dei domini (DO - <i>Domain Ontology</i>).
	 */
	private Ontology domainOntology;
	
	/**
	 * La SON da costruire.
	 */
	private SemanticOverlayNetwork son;
	
	/**
	 * L'insieme di servizi da creare.
	 */
	private ArrayList<Service> services;
	
	
	/**
	 * Istanzia un oggetto che Si occupi della costruzione dell'ambiente di simulazione.
	 * @param serviceDTO I parametri dei servizi.
	 * @param networkDTO I parametri di rete.
	 * @param ontologyDTO I parametri delle ontologie.
	 * @throws TooSmallOntologyException Se si prova a creare una ontologia troppo piccola.
	 */
	public SimulationEnvironmentBuilder(ServicesDTO serviceDTO, NetworkDTO networkDTO, OntologyDTO ontologyDTO)
	throws TooSmallOntologyException, TooSmallIdentifierCircleException, PeerNotFoundException, NodeAlreadyPresentException
	{
		this.serviceDTO = serviceDTO;
		this.networkDTO = networkDTO;
		this.ontologyDTO = ontologyDTO;
		
		buildSimulationEnvironment();
	}
	
	
	/**
	 * Avvia la costruzione dell'ambiente di simulazione.
	 * @throws TooSmallOntologyException Se si prova a creare una ontologia troppo piccola.
	 */
	protected void buildSimulationEnvironment()
	throws TooSmallOntologyException, TooSmallIdentifierCircleException, PeerNotFoundException, NodeAlreadyPresentException
	{
		categoryOntology = buildCategoryOntology();
		System.out.println("Costruita la category-ontology");
		domainOntology = buildDomainOntology();
		System.out.println("Costruita la domain-ontology");
		services = buildServices();
		System.out.println("Costruiti i servizi");
		son = buildSemanticOverlayNetwork();
		System.out.println("Costruita la Semantic Overlay Network");
	}
	
	/**
	 * Costruisce l'insieme dei servizi.
	 * @return L'elenco di servizi (tutti distinti) costruiti con i parametri.
	 * @throws TooSmallOntologyException Se si prova a creare una ontologia troppo piccola.
	 */
	protected ArrayList<Service> buildServices() throws TooSmallOntologyException{
		ArrayList<Service> builtServices = new ArrayList<Service>();
		int maxCat = serviceDTO.getMaxNumberOfCategoriesPerService();
		int maxIn = serviceDTO.getMaxNumberOfInputPerService();
		int maxOut = serviceDTO.getMaxNumberOfOutputPerService();
		for(int i=0; i<serviceDTO.getNumberOfServices(); i++){
			String[] categories = categoryOntology.getRandomConcepts(maxCat, true);
			String[] inputs = domainOntology.getRandomConcepts(maxIn, false);
			String[] outputs = domainOntology.getRandomConcepts(maxOut, false);
			builtServices.add(new Service("Service_"+i, categories, inputs, outputs));
		}
		return builtServices;
	}
	
	
	/**
	 * Costruisce una <i>Category Ontology</i> con i parametri assegnati.
	 * @return la <i>Category Ontology</i> costruita.
	 */
	protected Ontology buildCategoryOntology(){
		return new Ontology(ontologyDTO.getCategoriesNumber(), ontologyDTO.getCategoriesDepth());
	}
	
	/**
	 * Costruisce una <i>Domain Ontology</i> con i parametri assegati.
	 * @return la <i>Domain Ontology</i> costruita.
	 */
	protected Ontology buildDomainOntology(){
		return new Ontology(ontologyDTO.getDomainsNumber(), ontologyDTO.getDomainsDepth());
	}
	
	/**
	 * Costruisce la <i>Semantic Ovelay Network</i> con i parametri assegnati.
	 * @return La <i>Semantic Ovelay Network</i> costruita.
	 */
	protected SemanticOverlayNetwork buildSemanticOverlayNetwork()
	throws TooSmallIdentifierCircleException, NodeAlreadyPresentException, PeerNotFoundException
	{
		IdentifierCircle circle = IdentifierCircle.getInstance(networkDTO.getBits());
		SemanticOverlayNetwork net = new SemanticOverlayNetwork(circle, networkDTO.getLinkThreshold(), categoryOntology);
		Set<Integer> usedIds = new TreeSet<Integer>();
		for(int i=0; i<networkDTO.getNodesNumber(); i++){
			int id = -1;
			while(id < 0){
				int tmp = circle.getRandomId();
				if(!usedIds.contains(tmp)){
					id = tmp;
					break;
				}
			}
			SemanticNode sn = new SemanticNode(id, circle, categoryOntology);
			usedIds.add(id);
			net.join(sn);
			System.out.println("Aggiunta "+i+"-esima:  peer "+sn.getId()+"\n");
		}
		return net;
	}
	
	/**
	 * Consente di ottenere la <i>Category Ontology</i>.
	 * @return Un riferimento alla <i>Category Ontology</i>.
	 */
	public Ontology getCategoryOntology(){
		return categoryOntology;
	}
	
	/**
	 * Consente di ottenere la <i>Domain Ontology</i>.
	 * @return Un riferimento alla <i>Domain Ontology</i>.
	 */
	public Ontology getDomainOntology(){
		return domainOntology;
	}
	
	/**
	 * Consente di ottenere la <i>Semantic Overlay Network</i>.
	 * @return Un riferimento alla <i>Semantic Overlay Network</i>.
	 */
	public SemanticOverlayNetwork getSemanticOverlayNetwork(){
		return son;
	}
	
	/**
	 * Consente di accedere all'elenco di servizi pubblicati.
	 * @return Un riferimento all'elenco di servizi pubblicati.
	 */
	public ArrayList<Service> getServices(){
		return services;
	}
}
