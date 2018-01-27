package parameters;

/**
 * Classe che incapsula i parametri costruttivi della rete.
 * @author Gabriele
 *
 */
public class NetworkDTO {

	/**
	 * Parametro che indica il numero di bit che deve avere la rete.
	 */
	private int bits;
	
	/**
	 * Numero di nodi della rete.
	 */
	private int nodesNumber;
	
	/**
	 * Soglia per la costruzione di un link semantico.
	 */
	private int linkThreshold;
	
	/**
	 * Costruisce un DTO per i parametri di rete.
	 * @param bits Numero di bit.
	 * @param nodeNumber Numero di nodi.
	 * @param linkThreshold La soglia per la costruzione dei link semantici.
	 */
	public NetworkDTO(int bits, int nodeNumber, int linkThreshold){
		this.bits = bits;
		this.nodesNumber = nodeNumber;
		this.linkThreshold = linkThreshold;
	}
	
	/**
	 * Consente di accedere al numero di bit.
	 * @return Il numero di bit.
	 */
	public int getBits(){
		return bits;
	}
	
	/**
	 * Consente di ottenere il numero di nodi della rete.
	 * @return Il numero di nodi della rete.
	 */
	public int getNodesNumber(){
		return nodesNumber;
	}
	
	/**
	 * Consente di ottenere la soglia di costruzione dei link semantici.
	 * @return La soglia di costruzione dei link semantici.
	 */
	public int getLinkThreshold(){
		return linkThreshold;
	}
}
