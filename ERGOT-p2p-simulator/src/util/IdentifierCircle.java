package util;

import exceptions.TooSmallIdentifierCircleException;

/**
 * Questa classe rappresenta un anello logico di identificativi ed ha principalmente lo scopo
 * di effettuare calcoli in aritmetica modulare su uno spazio di indirizzi circolare.
 * 
 * @author Gabriele
 * 
 */
public final class IdentifierCircle {
	
	/**
	 * Variabile statica unica per l'implementazione del cerchio di identificativi come <b>SINGLETON</b> 
	 */
	private static IdentifierCircle identifierCircleInstance = null;
	
	/**
	 * Indica il numero di bit da cui è composto l'anello logico di identificativi.
	 */
	private int bits;
	
	/**
	 * E' un attributo derivato che indica la lunghezza dell'anello, cioè il numero di identificativi
	 * in esso contenuti. In particolare gli identificativi vanno da 0 a length-1 estremi inclusi.
	 */
	private int length;
	
	/**
	 * Costruisce un cerchio di identificatori.
	 * @param bits il numero di bit da usare per ciascun identificatore dell'anello.
	 */
	private IdentifierCircle(int bits){
		this.bits = bits;
		length = ((int)Math.pow(2, bits));
	}
	
	/**
	 * Metodo per ottenere l'unica istanza di IdentifierCircle.
	 * @param bits il numero di bits che caratterizza l'anello logico.
	 * @return un riferimento all'unica istanza di IdentifierCircle.
	 * @throws TooSmallIdentifierCircleException Qualora si cerchi di istanziare l'oggetto con meno di 5 bit.
	 */
	public static IdentifierCircle getInstance(int bits)throws TooSmallIdentifierCircleException{
		if(bits < 5)
			throw new TooSmallIdentifierCircleException();
		if(identifierCircleInstance == null){
			identifierCircleInstance = new IdentifierCircle(bits);
		}
		return identifierCircleInstance;
	}
	
	/**
	 * Consente di conoscere il numero di bit dell'anello logico.
	 * @return il numero di bit dellì'anello logico.
	 */
	public int getBits(){
		return bits;
	}
	
	/**
	 * Verifica se un elemento dato appartiene ad un dato intervallo del cerchio logico.
	 * NOTA: Gli estremi dell'intervallo sono considerati inclusi.
	 * 
	 * @param element L'elemento da verificare.
	 * @param start Il primo estremo dell'intervallo.
	 * @param end Il secondo estremo dell'intervallo.
	 * @return true se element appartiene a [start, end] sul cerchio logico.
	 */
	public boolean belongTo(int element, int start, int end){
		boolean esito = false;
		if(start==end)
			esito = (element==start);
		else if(start > end){
			int lastID = ((int)Math.pow(2, bits))-1;
			// recursive way: esito = belongTo(element, start, lastID) || belongTo(element, 0, end);
			esito = (element>=start && element<=lastID)||(element>=0 && element<=end);
		}
		else{
			esito = (element>=start && element<=end);
		}
		return esito;
	}


	/**
	 * Consente di percorrere all'indietro il cerchio logico.
	 * 
	 * @param start il punto di inizio del percorso all'indietro.
	 * @param distance la distanza da percorrere all'indietro.
	 * @return l'id raggiunto effettuando la retrocessione.
	 */
	public int retreat(int start, int distance){
		int idFound = start - distance;
		if(idFound < 0)
			idFound = length - Math.abs(idFound);
		idFound = (idFound + 1)%length;
		return idFound;
	}

	/**
	 * Consente di accedere alla lunghezza dell'anello.
	 * @return la lunghezza dell'anello.
	 */
	public int length(){
		return length;
	}
	
	/**
	 * Consente di accedere all'ultimo identificatore della rete.
	 * @return l'ultimo identificatore della rete.
	 */
	public int lastID(){
		return length - 1;
	}

	/**
	 * Consente di ottenere un id casuale tra quelli facenti parte dell'anello logico.
	 * Non necessariamente esiste una risorsa o un nodo con l'id trovato.
	 * @return l'identificatore casuale calcolato.
	 */
	public int getRandomId(){
		double r = Math.random();
		int limit = ((int)Math.pow(2, bits))-1;
		return (int)Math.round(r*limit);
	}
	
	/**
	 * Calcola il codice hash di una stringa rispetto all'anello logico.
	 * @param s la stringa di cui calcolare l'hashcode.
	 * @return il codice hash della stringa, normalizzato sull'anello logico.
	 */
	public int getHashCode(String s){
		int h = Math.abs(s.hashCode());
		return h%length;
	}
}
