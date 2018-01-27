package util;

import java.util.*;

/**
 * Classe che rappresenta un Servizio da pubblicare
 * 
 * @author Gabriele
 *
 */
public class Service implements Comparable<Service>{

	/**
	 * Identificativo che contraddistingue univocamente un servizio.
	 */
	private String serviceName; 
	
	/**
	 * Lista di nomi di categorie cui annotare il servizio.
	 */
	private String[] categories;
	
	/**
	 * Lista dei nomi dei Domini degli input.
	 */
	private String[] inputs;
	
	/**
	 * Lista dei nomi dei Domini degli output.
	 */
	private String[] outputs;
	
	/**
	 * Costruisce un servizio con un nome fissato e annotazioni.
	 * @param categories L'elenco delle annotazioni di categoria.
	 * @param inputs L'elenco delle annotazioni di input.
	 * @param outputs L'elenco delle annotazioni di output.
	 */
	public Service(String name, String[] categories, String[] inputs, String[] outputs){
		serviceName = name;
		this.categories = categories;
		this.inputs = inputs;
		this.outputs = outputs;
	}
	
	/**
	 * Consente di accedere all'identificativo del servizio.
	 * @return L'identificatore del servizio.
	 */
	public String getName(){
		return serviceName;
	}
	
	/**
	 * Consente di accedere alla lista di categorie del servizio.
	 * @return La lista di ccategorie del servizio.
	 */
	public String[] getCategories(){
		return categories;
	}
	
	/**
	 * Consente di accedere alla lista dei domini di input del servizio.
	 * @return La lista dei domini di input del servizio.
	 */
	public String[] getInputList(){
		return inputs;
	}
	
	/**
	 * Consente di accedere alla lista dei domini di output del servizio.
	 * @return La lista dei domini di output del servizio.
	 */
	public String[] getOutputList(){
		return outputs;
	}
	
	
	/**
	 * Consente di controllare se due servizi sono uguali: in particolare due servizi sono considerati
	 * uguali se hanno lo stesso identificativo.
	 */
	public boolean equals(Object o){
		if(!(o instanceof Service)) return false;
		else{
			Service s = (Service)o;
			return (this.serviceName.equals(s.getName()));
		}
	}
	
	/**
	 * Consente di conoscere il numero di domini di input del servizio.
	 * @return il numero di domini di input del servizio.
	 */
	public int getNumberOfInput(){
		return inputs.length;
	}
	
	/**
	 * Consente di conoscere il numero di domini di output del servizio.
	 * @return il numero di domini di output del servizio.
	 */
	public int getNumberOfOutput(){
		return outputs.length;
	}
	
	/**
	 * Consente di conoscere il numero di categorie del servizio.
	 * @return il numero di categorie del servizio.
	 */
	public int getNumberOfCategories(){
		return categories.length;
	}
	
	/**
	 * Relazione d'ordine tra servizi basata sull'ordine alfabetico.
	 */
	public int compareTo(Service service){
		return this.serviceName.compareTo(service.serviceName);
	}
}
