package semanticOverlayNetwork;

import java.util.*;
import util.*;
import network.*;
import node.*;
import semanticNode.*;
import exceptions.*;
import ontology.*;


/**
 * Classe che rappresenta una Semantic Overlay Network, basata su protocollo Chord.
 * In questa classe sono aggreagati oggetti SemanticNode anzichè i semplici Node.
 * 
 * @author Gabriele
 *
 */
public class SemanticOverlayNetwork extends Network{

	
	
	/**
	 * Valore di soglia da utilizzare per la creazione dei link semantici fra peer.
	 */
	private int linkCreationThreshold; 
	
	/**
	 * Elenco dei servizi attualmente pubblicati sulla rete.
	 */
	private ArrayList<Service> publishedServices;
	
	/**
	 * L'ontologia delle categorie (CO - Category Ontology)
	 */
	private Ontology categoryOntology;
	
	
	/**
	 * Costruisce una SON con un determinato spazio di indirizzi.
	 * @param circle il cerchio logico degli indirizzi.
	 */
	public SemanticOverlayNetwork(IdentifierCircle circle, int linkThreshold, Ontology categoryOntology){
		super(circle);
		publishedServices = new ArrayList<Service>();
		linkCreationThreshold = linkThreshold;
		this.categoryOntology = categoryOntology;
	}
	
	
	
	
	/**
	 * Consente di individuare il peer semantico che è il successore (cioè il responsabile) di una data categoria.
	 * @param n il nodo di partenza della richiesta.
	 * @param categoryString la stringa della categoria da ricercare.
	 * @return il peer della rete che è responsabile della categoria.
	 * @throws PeerNotFoundException Se non è stato possibile trovare il responsabile della categoria cercata (Si tratta
	 * 		   di un evento estremamente anomalo: il responsabile deve esistere sempre).
	 */
	public SemanticNode categoryBasedLookup(SemanticNode n, String categoryString)throws PeerNotFoundException{
		int categoryID = super.idCircle.getHashCode(categoryString);
		SemanticNode responsible = (SemanticNode)(super.findSuccessor(n, categoryID));
		return responsible;
	}
	
	
	public CategoryBasedResult enhancedCategoryBasedLookup(SemanticNode n, String categoryString, double threshold)
	throws ConceptNotFoundException, PeerNotFoundException
	{
		float totalServices = getNumberOfDistinctRelevantServices(categoryString, threshold);
		CategoryBasedResult result = new CategoryBasedResult(0, 0, 0);
		String[] concepts = categoryOntology.getConceptsSimilarTo(categoryString, threshold);
		TreeSet<Service> foundServices = new TreeSet<Service>();
		for(String concept: concepts){
			SemanticNode responsible = categoryBasedLookup(n, concept);
			Service[] services = responsible.getAllServicesAnnotatedOnConcept(concept);
			for(Service serv: services)
				foundServices.add(serv);
		}
		result.recall = foundServices.size()/totalServices;
		result.messages = concepts.length;
		result.hops = concepts.length;
		return result;
	}
	
	
	/**
	 * Consente ad un nodo di pubblicare una risorsa di sua proprietà. L'invocazione di questo metodo
	 * causa l'indicizzazione della coppia <i>(risorsa, peer_ID)</i> nella DHT del nodo responsabile della
	 * chiave del servizio. Inoltre, per ogni categoria del servizio, viene trovato il responsabile e viene
	 * annotata la coppia <i>(risorsa, peer_ID)</i> nella sua SAT (Semantic Annotation Table).
	 * Questo metodo inoltre crea un link semantico bidirezionale tra il peer pubblicante e altri peer che hanno
	 * annotato almeno <i>linkThreshold</i> altri servizi sullo stesso concetto.
	 * 
	 * @param publisher il nodo che pubblica una risorsa.
	 * @param service il servizio da pubblicare
	 * @throws PeerNotFoundException Se non è stato possibile trovare il responsabile della categoria cercata (Si tratta
	 * 		   di un evento estremamente anomalo: il responsabile deve esistere sempre).
	 */
	public void publish(SemanticNode publisher, Service service)throws PeerNotFoundException{
		publisher.insertNewService(service);
		// Annotazione esatta sulla DHT
		SemanticNode serviceResponsible = categoryBasedLookup(publisher, service.getName());
		serviceResponsible.storeInDHT(service, publisher.getId());
		// Annotazioni semantiche sulle SAT (diverse categorie) e creazione dei link semantici.
		String[] categories = service.getCategories();
		for(String category: categories){
			SemanticNode categoryResponsible = categoryBasedLookup(publisher, category);
			categoryResponsible.insertAnnotation(category, publisher.getId(), service);
			int[] semanticNeighborsIDs = categoryResponsible.getPeersAnnotatingOn(category, linkCreationThreshold);
			for(int neighbor_id: semanticNeighborsIDs){
				publisher.setSemanticLink(category, neighbor_id, 1);
				// COMMENTARE SE SI VOGLIONO LINK SEMANTICI UNIDIREZIONALI.
				SemanticNode neighbor = getNodeWithId(neighbor_id);
				neighbor.setSemanticLink(category, publisher.getId(), 1);
			}
		}
		publishedServices.add(service);
		System.out.println("Pubblicato il servizio "+service.getName());
	}
	
	
	/**
	 * Effettua la ricerca semantica basata solo sui link semantici.
	 * @param start Il nodo che genera la query.
	 * @param category La categoria di query.
	 * @param similarityThreshold La soglia di similarità per propagare la query ad un vicino semantico.
	 * @return I risultati della simulazione in un oggetto {@link SemanticBasedResult}}
	 * @throws PeerNotFoundException Se non è stato possibile trovare il responsabile della categoria cercata (Si tratta
	 * 		   di un evento estremamente anomalo: il responsabile deve esistere sempre).
	 * @throws ConceptNotFoundException Se non è stato possibile trovare la categoria o questa non appartiene all'ontologia.
	 */
	public SemanticBasedResult semanticBasedLookup(SemanticNode start, String category, double similarityThreshold, int TTL)
	throws PeerNotFoundException, ConceptNotFoundException
	{
		SemanticBasedResult result = new SemanticBasedResult();		
		ArrayList<Integer> alreadyForwardingPeers = new ArrayList<Integer>();
		LinkedList<NodeTracker> queue = new LinkedList<NodeTracker>();
		
		int[] timedResults = new int[TTL];
		int[] timedMessages = new int[TTL];
		float[] timedRecall = new float[TTL];
		
		result.query = category;
		

		float distinctRelevantServices = getNumberOfDistinctRelevantServices(category, similarityThreshold);
		result.totalRelevantServices = distinctRelevantServices;
		
		for(int i=0; i<TTL; i++){
			timedResults[i] = -1;
			timedMessages[i] = -1;
			timedRecall[i] = -1;
		}
		
		int currentHop = 0;
		int currentNumberOfMessages = 0;
		
		queue.addLast(new NodeTracker(start, 0));
		
		while(currentHop < TTL && !queue.isEmpty()){
			
			NodeTracker currentPeer = queue.removeFirst();
			
			// Se il nodo ha già propagato la query salta il turno.
			if(alreadyForwardingPeers.contains(currentPeer.node.getId()))
				continue;
			
			alreadyForwardingPeers.add(currentPeer.node.getId());
			
			ArrayList<Service> currentPeerServices = currentPeer.node.getPublishedServices(category, similarityThreshold);
			
			for(Service s: currentPeerServices)
				result.foundServices.add(new ResultService(s, category));
			
			currentHop = currentPeer.hopNumber;
			if(currentHop == TTL)
				break;
			
			timedResults[currentHop] = result.foundServices.size();
			timedMessages[currentHop] = currentNumberOfMessages;
			timedRecall[currentHop] = timedResults[currentHop]/distinctRelevantServices;
			
			int[] neighborIDs = currentPeer.node.getSemanticLinksOn(category, similarityThreshold);
			
			// La query la devo inviare a tutti, indistintamente.
			for(int neighborId: neighborIDs){
				//if(!alreadyReachedPeers.contains(neighborId)){
					SemanticNode node = getNodeWithId(neighborId);
					queue.add(new NodeTracker(node, currentHop+1));
					currentNumberOfMessages++;
				//}
			}
		}
		for(int i=0; i<TTL; i++){
			if(timedResults[i] >= 0)
				result.timedNumberOfServices.add(timedResults[i]);
			if(timedMessages[i] >= 0)
				result.timedNumberOfMessages.add(timedMessages[i]);
			if(timedRecall[i] >= 0)
				result.timedRecall.add(timedRecall[i]);
		}
		result.numHops = currentHop;
		return result;
	}
	
	
	
	/**
	 * Consente di ottenere un riferimento diretto ad un nodo semantico con uno specificato id.
	 * @param id L'id del nodo cercato.
	 * @return Il riferimento al nodo semantico cercato.
	 * @throws PeerNotFoundException Se si è specificato un id che non corrisponde a nessun nodo della rete.
	 */
	public SemanticNode getNodeWithId(int id)throws PeerNotFoundException{
		return (SemanticNode)(super.getNodeWithId(id));
	}
	
	
	/**
	 * Consente di conoscere quanti annotazioni di servizi ci sono nella rete che sono rilevanti per la
	 * query sulla categoria (concetto) specificato in base alla soglia di similarità considerata.
	 * <b>Nota:</b> <i>Se un servizio s è annotato a due categorie c1 e c2 entrambe rilevanti, viene contato 2 volte</i>.
	 * @param category La categoria di interesse.
	 * @param similarityThreshold La soglia di similarità da usare per valutare la pertinenza.
	 * @return il numero di annotazioni rilevanti.
	 * @throws ConceptNotFoundException  Se si è specificato una categoria non presente nell'ontologia.
	 */
	public float getNumberOfRelevantAnnotationOfServices(String category, double similarityThreshold)
	throws ConceptNotFoundException
	{
		float total = 0;
		for(Service s: publishedServices){
			for(String c: s.getCategories())
				if(categoryOntology.similarity(category, c) >= similarityThreshold)
					total++;
		}
		return total;	
	}	
	
	
	/**
	 * Consente di conoscere quanti servizi distinti pubblicati sono rilevanti per la categoria di query
	 * secondo una similarità.
	 * @param category La categoria di query.
	 * @param similarityThreshold La soglia di similarità.
	 * @return Il numero di serivizi rilevanti.
	 * @throws ConceptNotFoundException Se si è specificato un concetto di query che non corrisponde a nessun concetto dell'ontologia. 
	 */
	public float getNumberOfDistinctRelevantServices(String category, double similarityThreshold)
	throws ConceptNotFoundException{
		float total = 0;
		for(Service s: publishedServices){
			loop: for(String c: s.getCategories()){
				if(categoryOntology.similarity(category, c) >= similarityThreshold){
					total++;
					break loop;		
				}
			}
		}
		return total;	
	}
	
	/**
	 * Consente di ottenere un nodo casuale della rete.
	 * @return Un nodo casuale.
	 */
	public SemanticNode getRandomPeer(){
		Node[] list = getNodeList();
		int random = new Random().nextInt(list.length);
		return (SemanticNode)list[random];
	}
	
	
	
	//***********************************************************************************//
	//****			INNER-CLASSES DI UTILITA'											 //
	//***********************************************************************************//
	
	/**
	 * Classe che incapsula i risultati di un lookup di tipo <i>Semantic-Based</i>.
	 * @author Gabriele
	 *
	 */
	public class SemanticBasedResult{
		
		/**
		 * Numero complessivo di hops.
		 */
		int numHops;
		
		/**
		 * Concetto di query.
		 */
		String query;
		
		/**
		 * Insieme dei risultati trovati.
		 */
		TreeSet<ResultService> foundServices;
		
		/**
		 * Lista dell'evoluzione dei servizi durante gli hop.
		 */
		LinkedList<Integer> timedNumberOfServices;
		
		/**
		 * Lista dell'evoluzione dei messaggi durante gli hop.
		 */
		LinkedList<Integer> timedNumberOfMessages;
		
		/**
		 * Lista dell'evoluzione del recall durante gli hop.
		 */
		LinkedList<Float> timedRecall;
		
		/**
		 * numero totale di servizi rilevanti.
		 */
		float totalRelevantServices;
		
		/**
		 * Costruttore. Inizializza il numero di hops a 0 e istanzia una lista di servizi trovati vuota. 
		 */
		public SemanticBasedResult(){
			numHops = 0;
			totalRelevantServices = 0;
			foundServices = new TreeSet<ResultService>();
			timedNumberOfServices = new LinkedList<Integer>();
			timedNumberOfMessages = new LinkedList<Integer>();
			timedRecall = new LinkedList<Float>();
			query = "";
		}
		
		/**
		 * Consente di stampare in modo formattato il risultato.
		 */
		public String toString(){
			String s = "";
			/* Uncomment: per stampare l'elenco dei risultati
			for(ResultService res: foundServices){
				s += "Servizio "+res.service.getName()+" preso per la categoria "+res.category+"\n";
			}
			*/
			s += "numero di hops = "+numHops+"\n";
			s += "numero totale di servizi presenti "+totalRelevantServices+"\n";
			s += "totale numero di servizi trovati "+foundServices.size()+"\n";
			s += "\n\n";
			for(int i=0; i<timedNumberOfServices.size(); i++)
				s += "hop: "+i+" - trovati "+timedNumberOfServices.get(i)+" servizi - sono in giro "+timedNumberOfMessages.get(i)+" messaggi - il recall attuale è "+timedRecall.get(i)+" - il recall distinto è \n";
			return s;
		}
		
		/**
		 * Consente di ottenere la lista dell'evoluzione dei servizi durante gli hop.
		 * @return La lista dell'evoluzione dei servizi durante gli hop.
		 */
		public LinkedList<Integer> getTimedNumberOfServices(){
			return timedNumberOfServices;
		}
		
		/**
		 * Consente di accedere alla categoria di query.
		 * @return La categoria di query.
		 */
		public String getQueryString(){
			return query;
		}
		
		/**
		 * Consente di ottenere la lista dell'evoluzione dei messaggi durante gli hop.
		 * @return La lista dell'evoluzione dei messaggi durante gli hop.
		 */
		public LinkedList<Integer> getTimedNumberOfMessages(){
			return timedNumberOfMessages;
		}
		
		/**
		 * Consente di ottenere la lista dell'evoluzione del recall durante gli hop.
		 * @return La lista dell'evoluzione del recall durante gli hop.
		 */
		public LinkedList<Float> getTimedRecall(){
			return timedRecall;
		}
		
		/**
		 * Consente di impostare il valore del totale di servizi rilevanti per la query.
		 * @param annotation Il totale dei servizi rilevanti per la query.
		 */
		public void setTotalRelevantAnnotation(float annotation){
			totalRelevantServices = annotation;
		}		
	}

	
	/**
	 * Classe che incapsula il risultato di un lookup <i>Category-Based</i>
	 * @author Gabriele
	 *
	 */
	public class CategoryBasedResult{
		
		/**
		 * Recall totale.
		 */
		public float recall;
		
		/**
		 * Totale dei messaggi.
		 */
		public int messages;
		
		/**
		 * Numero totale di hops.
		 */
		public int hops;
		
		/**
		 * Costruisce un nuovo oggetto con i risultati.
		 * @param hops Numero totale di hops.
		 * @param messages Totale dei messaggi.
		 * @param recall Recall totale.
		 */
		public CategoryBasedResult(int hops, int messages, float recall){
			this.recall = recall;
			this.messages = messages;
			this.hops = hops;
		}
		
		/**
		 * Consente di accedere al recall.
		 * @return Il recall totale.
		 */
		public float getRecall(){
			return recall;
		}
		
		/**
		 * Consente di conoscere il totale degli hops effettuati.
		 * @return Il numero di hop.
		 */
		public int getHops(){
			return hops;
		}
		
		/**
		 * Consente di ottenere il numero di messaggi totali.
		 * @return Il numero di messaggi totali.
		 */
		public int getMessages(){
			return messages;
		}
	}

	/**
	 * Classe che incapsula coppie <i>(servizio, categoria)</i> in cui categoria indica
	 * la categoria per la quale è il servizi è stato scelto.
	 * @author Gabriele
	 *
	 */
	public class ResultService implements Comparable<ResultService>{
		
		/**
		 * Servizio scelto.
		 */
		Service service;
		
		/**
		 * Categoria per cui il servizio è stato scelto. 
		 */
		String category;
		
		/**
		 * Crea un oggetto <i>ResultService</i>
		 * @param service Il servizio.
		 * @param category La categoria rilevante, per cui è stato scelto.
		 */
		public ResultService(Service service, String category){
			this.service = service;
			this.category = category;
		}	
		
		/**
		 * Verifica se due oggetti sono lo stesso oggetto.
		 */
		public boolean equals(Object o){ 
			if(!(o instanceof ResultService)) 
				return false;
			else
				return ((ResultService)o).equals(this.service);
		}
		
		/**
		 * Relazione d'ordine fra oggetti, basata sull'ordinamento già definito sui servizi.
		 */
		public int compareTo(ResultService result){
			return this.service.compareTo(result.service);
		}
	}

	
	/**
	 * Classe che consente di tenere traccia di un peer semantico e del
	 * hop al quale è stato scoperto.
	 * @author Gabriele
	 *
	 */
	public class NodeTracker{
		
		/**
		 * Il nodo da tracciare.
		 */
		SemanticNode node;
		
		/**
		 * L'hop nel quale è stato scoperto.
		 */
		int hopNumber;
		
		/**
		 * Costruisce un nuovo oggetto <i>NodeTracker</i>
		 * @param node Il nodo da tracciare.
		 * @param hopNumber L'hop cui è stato scoperto.
		 */
		public NodeTracker(SemanticNode node, int hopNumber){
			this.node = node;
			this.hopNumber = hopNumber;
		}
	}
	
}
