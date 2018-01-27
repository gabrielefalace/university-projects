package parameters;

/**
 * Classe che incapsula i parametri per la definizione dei servizi.
 * 
 * @author Gabriele
 *
 */
public class ServicesDTO {

	/**
	 * Numero di servizi che dovranno essere generati per la simulazione.
	 */
	private int numServices;
	
	/**
	 * Numero massimo di annotazioni di categoria per servizio.
	 */
	private int maxCategoriesPerService;
	
	/**
	 * Numero massimo di annotazioni di input per servizio.
	 */
	private int maxInputsPerService;
	
	/**
	 * Numero massimo di annotazioni di output per servizio.
	 */
	private int maxOutputPerService;
	
	
	/**
	 * Costruisce un DTO per incapsulare i parametri di simulazione relativi ai servizi.
	 * @param numServices Il totale di servizi.
	 * @param maxCategoriesPerService Il massimo di annotazioni di categoria.
	 * @param maxInputsPerService Il massimo di annotazioni di input.
	 * @param maxOutputPerService Il massimo di annotazioni di output.
	 */
	public ServicesDTO(int numServices, int maxCategoriesPerService, int maxInputsPerService, int maxOutputPerService){
		this.numServices = numServices;
		this.maxCategoriesPerService = maxCategoriesPerService;
		this.maxInputsPerService = maxInputsPerService;
		this.maxOutputPerService = maxOutputPerService;
	}
	
	/**
	 * Consente di accedere al numero di servizi. 
	 * @return Il numero totale di servizi.
	 */
	public int getNumberOfServices(){
		return numServices;
	}
	
	/**
	 * Consente di accedere al numero massimo di annotazioni di input.
	 * @return Il massimo di annotazioni di input.
	 */
	public int getMaxNumberOfInputPerService(){
		return maxInputsPerService;
	}
	
	/**
	 * Consente di accedere al numero massimo di annotazioni di output.
	 * @return Il massimo di annotazioni di output.
	 */
	public int getMaxNumberOfOutputPerService(){
		return maxOutputPerService;
	}
	
	/**
	 * Consente di accedere al numero massimo di annotazioni di categoria.
	 * @return Il massimo di annotazioni di categoria.
	 */
	public int getMaxNumberOfCategoriesPerService(){
		return maxCategoriesPerService;
	}
}
