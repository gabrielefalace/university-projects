package parameters;

import semanticNode.*;

/**
 * Classe che incapsula i parametri che regolano i lookup.
 * 
 * @author Gabriele
 *
 */
public class LookupDTO {

	/**
	 * Rappresenta la soglia di similarità che un vicino di un nodo deve avere affinchè gli venga propagata la query.
	 */
	private double similarityThreshold;
	
	/**
	 * Il TTL con cui costruire le query.
	 */
	private int TTL;   
	
	/**
	 * Numero di lookup da eseguire.
	 */
	private int numberOfLookups;
	
	
	
	/**
	 * Costruisce un DTO per i parametri relativi ai lookup.
	 * @param similarityThreshold La soglia di similarità per l'inoltro.
	 * @param TTL Il Time To Live delle query.
	 * @param numberOfLookups Il numero di lookup da eseguire.
	 */
	public LookupDTO(double similarityThreshold, int TTL, int numberOfLookups){
		this.similarityThreshold = similarityThreshold;
		this.TTL = TTL;
		this.numberOfLookups = numberOfLookups;
	}
	
	
	/**
	 * Consente di conoscere il TTL delle query.
	 * @return Il TTL impostato.
	 */
	public int getTTL(){
		return TTL;
	}
	
	
	/**
	 * Consente di sapere la soglia di similarità per l'inoltro.
	 * @return La soglia di similarità per l'inoltro.
	 */
	public double getSimilarityThreshold(){
		return similarityThreshold;
	}
	
	/**
	 * Consente di ottenere il numero di lookup da eseguire.
	 * @return Il numero di lookup da eseguire.
	 */
	public int getNumberOfLookups(){
		return numberOfLookups;
	}
}
