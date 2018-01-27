package semanticNode;

import util.*;
import node.Node;
import java.util.*;

import exceptions.ConceptNotFoundException;
import ontology.*;

/**
 * Questa classe rappresenta un nodo della Semantic Overlay Network.
 * Dal momento che la SON è costruita sulla base del protocollo Chord,
 * il nodo semantico è una sottoclasse del nodo  di base del protocollo Chord.
 * 
 * @author Gabriele
 *
 */
public class SemanticNode extends Node{
	
	/**
	 * La Semantic Links Table, che contiene i link semantici verso i peer che 
	 * hanno servizi simili a quelli di interesse.
	 */
	ArrayList<SltEntry> slt;
	
	/**
	 * La Service Annotation Table, che contiene i servizi relativi alle
	 * categorie di cui questo nodo è responsabile.
	 */
	ArrayList<SatEntry> sat;
	
	/**
	 * La lista di servizi di cui questo nodo è proprietario.
	 */
	ArrayList<Service> services;
	
	
	Ontology categoryOntology;
	
	
	/**
	 * Costruttore: crea un nodo semantico.
	 * @param id l'identificativo del nodo.
	 * @param circle il cerchio logico di indirizzi.
	 */
	public SemanticNode(int id, IdentifierCircle circle, Ontology categoryOntology){
		super(id, circle);
		slt = new ArrayList<SltEntry>();
		sat = new ArrayList<SatEntry>();
		services = new ArrayList<Service>();
		this.categoryOntology = categoryOntology;
	}
	
	/**
	 * Inserisce un nuovo link semantico nella Semantic Link Table.
	 * Se il link sul dato peer per il dato concetto è già presente, lo sovrascrive.
	 * @param concept il concetto su cui creare il nuovo link semantico.
	 * @param peer il peer con cui creare il nuovo link semantico.
	 * @param strenght la "forza" del link semantico.
	 */
	public void setSemanticLink(String concept, int peer, float strenght){
		SltEntry entry = new SltEntry(concept, peer, strenght);
		if(slt.contains(entry))
			slt.remove(entry);
		slt.add(entry);
	}
	
	/**
	 * Indicizza un servizio di un peer pubblicante sulla SAT.
	 * Il servizio viene iserito in corrispondenza del concetto
	 * specificato.
	 * 
	 * @param concept il concetto su cui annotare.
	 * @param peer il peer pubblicante.
	 * @param service il servizio da annotare.
	 */
	public void insertAnnotation(String concept, int peer, Service service){
		SatEntry rightEntry = null;
		for(SatEntry entry: sat){
			if(entry.concept.equals(concept) && entry.peer==peer){
				rightEntry = entry;
				break;
			}
		}
		if(rightEntry==null){
			ArrayList<Service> list = new ArrayList<Service>();
			list.add(service);
			sat.add(new SatEntry(concept, peer, list));
		}
		else{
			if(!rightEntry.services.contains(service))
				rightEntry.services.add(service);
		}
	}
	
	/**
	 * Consente di ottenere i link semantici di questo nodo, a partire dal concetto
	 * specificato. Ovvero, consente di sapere quali peer sono vicini semantici secondo un
	 * dato concetto.
	 * 
	 * @param concept il concetto sul quale si vogliono i vicini semantici.
	 * @return il vettore di identificativi dei peer che sono vicini semantici.
	 */
	public int[] getSemanticLinksOn(String concept){
		ArrayList<SltEntry> peers = new ArrayList<SltEntry>();
		for(SltEntry elem: slt)
			if(elem.concept.equals(concept))
				peers.add(elem);
		int[] peerIds = new int[peers.size()];
		for(int i=0; i<peerIds.length; i++)
			peerIds[i] = peers.get(i).peer;
		return peerIds;
	}
	
	/**
	 * Consente di ottenere l'elenco degli ID dei <i>semantic neighbor</i> su concetti abbastanza simili ad un dato
	 * concetto di query, entro una certa soglia di similarità.
	 * @param concept Il concetto di query, su cui si vogliono i <i>semantic neighbor</i>.
	 * @param similarityThreshold La soglia di similarità per la selezione.
	 * @return L'elenco degli id dei vicini semantici abbastanza simili.
	 */
	public int[] getSemanticLinksOn(String concept, double similarityThreshold) throws ConceptNotFoundException{
		
		ArrayList<SltEntry> peers = new ArrayList<SltEntry>();
		for(SltEntry elem: slt)
			if(categoryOntology.similarity(elem.concept, concept) >= similarityThreshold)
				peers.add(elem);
		
		TreeSet<Integer> peer_set = new TreeSet<Integer>();
		for(int i=0; i<peers.size(); i++){
			peer_set.add(peers.get(i).peer);
		}
		
		int[] peerIds = new int[peer_set.size()];
		int j = 0;
		
		for(Integer id: peer_set){
			peerIds[j] = id;
			j++;
		}
		return peerIds;
	}
	
	
	/**
	 * Consente di ottenere l'elenco di categorie di cui è responsabile il nodo. 
	 * @return il vettore di stringhe delle categorie.
	 */
	public String[] getAnnotatedCategories(){
		ArrayList<String> tmp_categories = new ArrayList<String>();
		for(SatEntry elem: sat)
			tmp_categories.add(elem.concept);
		String[] categories = new String[tmp_categories.size()];
		for(int i=0; i<categories.length; i++)
			categories[i] = tmp_categories.get(i);
		return categories;
	}
	
	/**
	 * Consente di ottenere l'elenco di categorie su cui il nodo ha dei vicini. 
	 * @return il vettore di categorie.
	 */
	public String[] getSemanticLinksCategories(){
		ArrayList<String> concepts = new ArrayList<String>();
		for(SltEntry elem: slt)
			concepts.add(elem.concept);
		String[] categories = new String[concepts.size()];
		for(int i=0; i<categories.length; i++)
			categories[i] = concepts.get(i);
		return categories;
	}
	
	/**
	 * Consente di conoscere gli identificativi dei peer che hanno annotato
	 * (pubblicato) qualcosa in corrispondenza del concetto specificato.
	 * @param concept il concetto di cui si vogliono conoscere i peer.
	 * @return il vettore di identificativi dei peer.
	 */
	public int[] getPeersAnnotatingOn(String concept){
		ArrayList<SatEntry> entries = new ArrayList<SatEntry>();
		for(SatEntry entry: sat)
			if(entry.concept.equals(concept))
				entries.add(entry);
		int[] peers = new int[entries.size()];
		for(int i=0; i<peers.length; i++)
			peers[i] = entries.get(i).peer;
		return peers;
	}
	
	/**
	 * Consente di conoscere gli identificativi dei peer che hanno pubblicato
	 * almeno <i>minServices</i> sul dato concetto.
	 * @param concept Il concetto di interesse.
	 * @param minServices Il numero minimo di servizi pubblicati affinché il peer sia di interesse.
	 * @return L'elenco degli id dei peer utili.
	 */
	public int[] getPeersAnnotatingOn(String concept, int minServices){
		ArrayList<SatEntry> entries = new ArrayList<SatEntry>();
		for(SatEntry entry: sat)
			if(entry.concept.equals(concept) && entry.services.size()>=minServices)
				entries.add(entry);
		int[] peers = new int[entries.size()];
		for(int i=0; i<peers.length; i++)
			peers[i] = entries.get(i).peer;
		return peers;
	}
	
	/**
	 * Consente di ottenere l'elenco dei servizi annotati su un determinato concetto.
	 * 
	 * @param concept il concetto di cui si vogliono sapere i servizi.
	 * @return il vettore dei servizi associati al concetto.
	 */
	public Service[] getAllServicesAnnotatedOnConcept(String concept){
		ArrayList<Service> services = new ArrayList<Service>();
		for(SatEntry entry: sat)
			if(entry.concept.equals(concept))
				services.addAll(entry.services);
		Service[] serv = new Service[services.size()];
		for(int i=0; i<serv.length; i++)
			serv[i] = services.get(i);
		return serv;
	}
	
	/**
	 * Consente di ottenere l'elenco di servizi che un dato peer ha annotato su un 
	 * dato concetto.
	 * 
	 * @param concept il concetto di cui si vogiono sapere i servizi.
	 * @param peer l'id del peer di cui si vogliono sapere i servizi.
	 * @return il vettore di servizi associato al concetto ed al peer.
	 */
	public Service[] getAllServiceOnConceptAndPeer(String concept, int peer){
		ArrayList<Service> services = new ArrayList<Service>();
		for(SatEntry entry: sat)
			if(entry.concept.equals(concept) && entry.peer==peer)
				services.addAll(entry.services);
		Service[] serv = new Service[services.size()];
		for(int i=0; i<serv.length; i++)
			serv[i] = services.get(i);
		return serv;
	}
	
	/**
	 * Consente di ottenere un riferimento ai servizi pubblicati da questo peer rilevanti per
	 * una data categoria.
	 * Ovvero consente di ottenere l'elenco di servizi fisicamente appartenenti a questo nodo.
	 * @param category La categoria per la quale si vogliono filtrare i servizi rilevanti.
	 * @param similarityThreshold La soglia per stabilire la rilevanza.
	 * @return L'elenco di servizi pubblicati da questo nodo (di sua proprietà).
	 */
	public ArrayList<Service> getPublishedServices(String category, double similarityThreshold)
	throws ConceptNotFoundException
	{
		ArrayList<Service> servs = new ArrayList<Service>();
		for(Service s: services)
			for(String c: s.getCategories())
				if(categoryOntology.similarity(c, category) >= similarityThreshold)
					servs.add(s);
		return servs;
	}
	
	/**
	 * Consente di inserire un nuovo servizio presso il nodo.
	 * @param aService il serivizio da aggiungere alla lista dei serivizi di questo nodo.
	 */
	public void insertNewService(Service aService){
		services.add(aService);
	}
	
	//************************************************************************************//
	//						 INNER-CLASSES DI UTILITA' 								      //
	//************************************************************************************//
	
	/**
	 * Classe che rappresenta una entry (riga) della Semantic Links Table.
	 * Ogni entry è caratterizzata da una tripla (concetto, peerID, forza) in
	 * cui concetto è una stringa, peerID è l'id del peer e forza rappresenta 
	 * una misura di validità o di qualità del link. 
	 * 
	 * @author Gabriele
	 *
	 */
	private class SltEntry implements Comparable<SltEntry>{
		
		/**
		 * La stringa che rappresenta il concetto.
		 */
		private String concept;
		
		/**
		 * L'id del peer verso cui è formato il link semantico.
		 */
		private int peer;
		
		/**
		 * La validità di questo link semantico.
		 */
		private double strenght;
		
		/**
		 * Costruisce una entry della tabella dei link semantici.
		 * @param concept Il concetto.
		 * @param peer L'identificativo del peer.
		 * @param strenght La forza del link.
		 */
		public SltEntry(String concept, int peer, double strenght){
			this.concept = concept;
			this.peer = peer;
			this.strenght = strenght;
		}
		
		/**
		 * Relazione d'ordine tra due link semantici:
		 * vengono ordinati in base al concetto e a parità di concetto in base all'id del peer.
		 */
		public int compareTo(SltEntry entry){
			int tmp = this.concept.compareTo(entry.concept);
			if(tmp != 0)
				return tmp;
			else{
				if(this.peer==entry.peer)
					return 0;
				else if(this.peer < entry.peer)
					return -1;
				else
					return 1;
			}
		}
		
		/**
		 * Consente di verificare se due entry sono uguali: sono uguali se
		 * hanno stesso concetto e stesso identificativo del peer.
		 */
		public boolean equals(Object o){
			if(!(o instanceof SltEntry)) return false;
			else
				return(((SltEntry)o).compareTo(this)==0);
		}
	}
	
	/**
	 * Classe che rappresenta una entry della Service Annotation Table.
	 * Ogni riga è fatta da una tripla (concetto, peerID, lista_servizi).
	 *  
	 * @author Gabriele
	 *
	 */
	private class SatEntry implements Comparable<SatEntry>{
		
		/**
		 * Il concetto.
		 */
		private String concept;
		
		/**
		 * L'identificativo del peer.
		 */
		private int peer;
		
		/**
		 * La lista dei servizi.
		 */
		private ArrayList<Service> services;
		
		/**
		 * Costruisce una entry della Service Annotation Table.
		 * @param concept Iil concetto di questa entry.
		 * @param peer L'id del peer di questa entry.
		 * @param services La lista di servizi della entry
		 */
		public SatEntry(String concept, int peer, ArrayList<Service> services){
			this.concept = concept;
			this.peer = peer;
			this.services = services;
		}
		
		/**
		 * Relazione d'ordine tra entry. Vengono ordinate in base al concetto
		 * e, a parità di concetto, in base all'id del peer.
		 */
		public int compareTo(SatEntry entry){
			int tmp = this.concept.compareTo(entry.concept);
			if(tmp != 0)
				return tmp;
			else{
				if(this.peer==entry.peer)
					return 0;
				else if(this.peer < entry.peer)
					return -1;
				else
					return 1;
			}
		}
		
		/**
		 * Consente di verificare l'uguaglianza tra due entry: sono uguali solo
		 * se hanno stesso concetto e stesso id di peer.
		 */
		public boolean equals(Object o){
			if(!(o instanceof SatEntry)) return false;
			else
				return(((SatEntry)o).compareTo(this)==0);
		}
	}
}
