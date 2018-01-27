package simulation;

import java.util.*;
import java.io.*;
import exceptions.PeerWithNoSemanticLinkException;
import parameters.LookupDTO;
import parameters.NetworkDTO;
import parameters.OntologyDTO;
import parameters.ServicesDTO;
import semanticOverlayNetwork.SemanticOverlayNetwork;

public class SimulationStarter {

	public static void main(String[] args){
		
		int BIT = 16;
		int NUMERO_NODI = 2000;
		int SOGLIA_CREAZIONE_LINK = 2; // ATTENZIONE: se troppo alta non vengono creati link semantici => la simulazione non parte!
		int NUMERO_CATEGORIE = 130;
		int NUMERO_DOMINI = 80;
		int PROFONDITA_CATEGORIE = 7;
		int PROFONDITA_DOMINI = 6;
		int NUMERO_SERVIZI = 3000;
		int MAX_CATEGORIE_PER_SERVIZIO = 8;
		int MAX_INPUT_PER_SERVIZIO = 3;
		int MAX_OUTPUT_PER_SERVIZIO = 3;
		double SOGLIA_SIMILARITA_INOLTRO = 0.75;
		int TTL = 6;
		int NUMERO_DI_LOOKUP = 10;
		
		
		NetworkDTO networkDTO = new NetworkDTO(BIT, 
											   NUMERO_NODI, 
											   SOGLIA_CREAZIONE_LINK);
		
		OntologyDTO ontologyDTO = new OntologyDTO(NUMERO_CATEGORIE, 
												  NUMERO_DOMINI, 
												  PROFONDITA_CATEGORIE, 
												  PROFONDITA_DOMINI);
		
		ServicesDTO serviceDTO = new ServicesDTO(NUMERO_SERVIZI,
												 MAX_CATEGORIE_PER_SERVIZIO, 
												 MAX_INPUT_PER_SERVIZIO,
												 MAX_OUTPUT_PER_SERVIZIO);
		
		LookupDTO lookupDTO = new LookupDTO(SOGLIA_SIMILARITA_INOLTRO,
											TTL,
											NUMERO_DI_LOOKUP);
		
		
		FileWriter fw = null;
		PrintWriter writer = null;
		
		try{
			SimulationManager manager = new SimulationManager(ontologyDTO, networkDTO, serviceDTO, lookupDTO);
			float participatingPeers = manager.computeNumberOfParticipatingPeers();
			System.out.println("Il numero di peer partecipanti al sistema sono "+participatingPeers+" frazione: "+(participatingPeers/NUMERO_NODI));
			
			System.out.println("OGNI PEER PARTECIPANTE HA IN MEDIA "+manager.computeAverageSemanticLinksPerNode()+" LINK SEMANTICI");
			
			/*
			ArrayList<SemanticOverlayNetwork.CategoryBasedResult> results = manager.executeEnhancedCategoryBasedLookups();
			
			for(int i=0; i<results.size(); i++)
				System.out.println("Risultati: lookup esatti Chord = "+results.get(i).hops+"; recall = "+results.get(i).recall+";");
			
			*/
			ArrayList<SemanticOverlayNetwork.SemanticBasedResult> results = manager.executeSemanticBasedLookups();			
			
			float[] recall_report = manager.getAverageTimedResult(results);
			float[] messages_report = manager.getAverageTimedMessages(results);
			float[] services_report = manager.getAverageTimedServices(results);
			
			double[] recall_dev = manager.getRecallDeviation(results, recall_report);
			double[] messages_dev = manager.getMessagesDeviation(results, recall_report);
			double[] services_dev = manager.getServicesDeviation(results, recall_report);
			
			
			File f = new File("c:\\users\\gabriele\\desktop\\results.txt");
			if(f.exists()){
				f.delete();
			}
			f.createNewFile();
			fw = new FileWriter(f);
			writer = new PrintWriter(fw);
			
			writer.println("#HOP   #SERVIZI  servizi-dev   #MESSAGGI   messaggi-dev   #RECALL  recall-dev");
			for(int i=0; i < recall_report.length; i++){
				writer.println((i+1)+"   "+services_report[i]+"  "+services_dev[i]+"  "+messages_report[i]+"  "+messages_dev[i]+"   "+recall_report[i]+"  "+recall_dev[i]);
			}
			
		}
		catch(PeerWithNoSemanticLinkException exc){
			System.out.println("Simulazione Fallita: scelto un nodo start senza link semantici");
		}
		catch(Exception err){
			System.out.println(err);
			err.printStackTrace();
		}
		finally{
			try{
				if(fw != null) fw.close();
				if(writer != null) writer.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
	
}
