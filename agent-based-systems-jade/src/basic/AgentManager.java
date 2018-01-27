package basic;

import basic.Map;
import java.util.Random;

import robot.GreedyBehaviour;
import jade.core.*;
import jade.core.Runtime;
import jade.wrapper.AgentController;

/**
 * Classe per la gestione dell'esecuzione dei robot. In questa classe vengono anche collocati i siti in posizioni casuali e 
 * viene settato il comportamento dei robot in accordo con quanto richiesto dall'utente.
 * 
 * @author Gabriele Falace (127599)
 *
 */
public class AgentManager {

	/**
	 * Runtime per la messa in esecuzione del Container.
	 */
	private Runtime rt;
	
	/**
	 * Profile per la creazione di un AgentContainer.
	 */
	private Profile profile;
	
	/**
	 * Container per ospitare i robot in esecuzione.
	 */
	private jade.wrapper.AgentContainer mainContainer;
	
	/**
	 * AgentController per gestire l'esecuzione dei robot.
	 */
	private AgentController[] robot;
	
	/**
	 * Mappa su cui agiscono i robot.
	 */
	private Map map;
	
	/**
	 * Tipo di agente da settare in modo da assegnare al robot il comportamento desiderato dall'utente.
	 */
	private String agentType;
	
	/**
	 * Oggetto random per generare coordinate casuali per i siti da attivare. 
	 */
	private Random random;
	
	/**
	 * Log su cui gli agenti scrivono messaggi relativi al loro stato ed alle loro comunicazioni.
	 */
	private Log log;
	
	/**
	 * Costruisce un AgentManager con una mappa 100X100 e 2 agenti di default.
	 */
	public AgentManager(Log log){
		this.log = log;
		agentType = "NAIVE";
		map = new Map(100);
		robot = new AgentController[map.getNumAgents()];
		rt = Runtime.instance();
		rt.setCloseVM(true);
		profile = new ProfileImpl(null, 3000, null);
		mainContainer = rt.createMainContainer(profile);
		random = new Random();
	}
	
	/**
	 * Imposta il numero di agenti che opereranno nell'ambiente (mappa).
	 * @param n il numero di agenti.
	 */
	public void setNumAgents(int n){map.setNumAgents(n);}
	
	/**
	 * Accede al numero di agenti presenti.
	 * @return il numero di agenti.
	 */
	public int getNumAgents(){return map.getNumAgents();}
	
	/**
	 * Imposta il numero di siti che saranno attivati nell'ambiente (mappa).
	 * @param numSites il numero di siti.
	 */
	public void setNumSites(int numSites){map.setNumSites(numSites);}
	
	/**
	 * Accede al numero di siti definiti sulla mappa.
	 * @return il numero di siti.
	 */
	public int getNumSites(){return map.numSites;}
	
	/**
	 * Reimposta tutte le variabili nella mappa ai loro valori iniziali.
	 */
	public void resetMap(){map.reset();}
	
	/**
	 * Accede al tipo di agente impostato.
	 * @return Una <code>String</code> che rappresenta il tipo di agente impostato.
	 */
	public String getAgentType(){return agentType;}
	
	/**
	 * Imposta il tipo di agente.
	 * @param type Una <code>String</code> che rappresenta il tipo di agente.
	 */
	public void setAgentType(String type){agentType = type;}
	
	/**
	 * Cosnente di accedere alla mappa.
	 * @return Un oggetto <code>{@link Map}</code> che è la mappa su cui operano gli agenti.
	 */
	public Map getMap(){return map;}
	
	/**
	 * Istanzia un oggetto <code>Map</code> ed uno <code>Log</code> e li passa agli agenti che crea in base
	 * al tipo scelto dall'utente.  
	 */
	public void startAll(){
		// Carico gli oggetti condivisi da passare agli agenti
		Object[] arguments = new Object[3];
		arguments[0] = map;
		// Passo anche il behaviour deciso dall'utente!
		arguments[1] = new String(agentType);
		// Passo il log
		arguments[2] = log;
		try{
			robot = new AgentController[map.getNumAgents()];
			for(int i=0; i<robot.length; i++){
				robot[i] = mainContainer.createNewAgent("robot"+i, "robot.Robot", arguments);
				robot[i].start();
			}
			
			for(int i=0; i<map.getNumSites(); i++){
				int row = random.nextInt(100);
				int col = random.nextInt(100);
				map.setActiveSite(row, col);
			}
			map.setShowReport(true);
		}
		catch(Exception err){
			err.printStackTrace();
		}
	}
	
	/**
	 * Resetta tutto: Map, Log e termina gli agenti.
	 */
	public void resetAll(){
		try{
			// IMPORTANTE
			System.out.println("Chiamo reset su log e map...");
			log.reset();
			map.reset();
			
			System.out.println("Ora sopprimo gli agenti e poi resetto mappa e log...");
			for(int i=0; i<Map.MAX_AGENTS; i++){
				System.out.println(robot[i].getName());
				if(robot[i]!=null){
					System.out.println("Soppresso "+robot[i].getName());
					robot[i].kill();
				}
			}
		}
		catch(Exception err){
			//err.printStackTrace();
		}
	}
	

}
