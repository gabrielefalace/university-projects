package parameters;

/**
 * Classe che incapsula i parametri che definiscono le ontologie necessarie alla SON.
 * @author Gabriele
 *
 */
public class OntologyDTO {

	/**
	 * Numero di categorie.
	 */
	private int numCategories;
	
	/**
	 * Numero di domini.
	 */
	private int numDomains;
	
	/**
	 * ProfonditÓ dell'albero delle categorie.
	 */
	private int categoriesDepth;
	
	/**
	 * ProfonditÓ dell'albero dei domini.
	 */
	private int domainsDepth;
	
	/**
	 * Crea un oggetto che incapsula i parametri di costruzione delle due ontologie (Categorie e Domini).
	 * @param numCategories Numero di categorie.
	 * @param numDomains Numero di domini.
	 * @param categoriesDepth ProfonditÓ dell'albero delle categorie.
	 * @param domainsDepth ProfonditÓ dell'albero dei domini.
	 */
	public OntologyDTO(int numCategories, int numDomains, int categoriesDepth, int domainsDepth){
		this.numCategories = numCategories;
		this.numDomains = numDomains;
		this.categoriesDepth = categoriesDepth;
		this.domainsDepth = domainsDepth;
	}
	
	/**
	 * Consente di ottenere il numero di domini.
	 * @return Il numero di domini.
	 */
	public int getDomainsNumber(){
		return numDomains;
	}
	
	/**
	 * Consente di ottenere il numero di categorie.
	 * @return Il numero di categorie.
	 */
	public int getCategoriesNumber(){
		return numCategories;
	}
	
	/**
	 * Consente di ottenere la profonditÓ dell'albero dei domini.
	 * @return La profonditÓ dell'albero dei domini.
	 */
	public int getDomainsDepth(){
		return domainsDepth;
	}
	
	/**
	 * Consente di ottenere la profonditÓ dell'albero delle Categorie.
	 * @return La profonditÓ dell'albero delle categorie.
	 */
	public int getCategoriesDepth(){
		return categoriesDepth;
	}
}
