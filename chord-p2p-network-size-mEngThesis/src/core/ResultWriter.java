package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Classe che consente di scrivere i risultati di simulazione su file.
 * La classe crea il file sul desktop, nella cartella "results" e il nome del file è 
 * individuato dal nome dell'algoritmo, dalla dimensione della rete e dalla dimensione del
 * campione interrogato.
 * 
 * @author Gabriele
 *
 */
public class ResultWriter {

	private static String FILE_PATH = "C:\\Users\\Gabriele\\Desktop\\results";
	private File fileHandler;
	private PrintWriter printWriter;
	
	/**
	 * Crea uno scrittore di risultati su un file.
	 * @param algorithmName Nome dell'algoritmo simulato
	 * @param numberOfNodes numero di nodi che oggetto della simulazione
	 * @param sampleSize numerosità del campione da cui si è tratta la stima.
	 */
	public ResultWriter(String algorithmName, int numberOfNodes, int sampleSize){
		try {
			String fileName = FILE_PATH+"\\"+algorithmName+"_N#"+numberOfNodes+"_K#"+sampleSize+".txt";
			fileHandler = new File(fileName);
			if(!fileHandler.exists()) 
				fileHandler.createNewFile();
			printWriter = new PrintWriter(new FileOutputStream(fileHandler));
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		} 
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Scrive sul file una descrizione dell'algoritmo usato.
	 * @param description una stringa che descrive l'algoritmo.
	 */
	public void writeAlgorithmDescription(String description){
		String tokens[] = description.split("\\.");
		for(String token: tokens)
			printWriter.println("# "+token);
	}
	
	/**
	 * Scrive un valore (dimensione stimata) sul file.
	 * @param estimatedValue il valore (dimensione stimata).
	 */
	public void writeSampledEstimate(double estimatedValue){
		String valueToWrite = estimatedValue+"";
		//per avere la virgola al posto del punto
		String valueString = valueToWrite.replace('.', ',');
		printWriter.println(valueString+"");
	}
	
	/**
	 * Chiude gli stream utilizzati da questo oggetto.
	 */
	public void shutDown(){
		printWriter.close();
	}

}
