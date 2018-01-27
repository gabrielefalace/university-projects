package core;

import java.util.LinkedList;

/**
 * Classe che rappresenta una cattura. Contiene la cella di partenza e la lista delle celle con le pedine 
 * da catturare. Consente di costruire una cattura in modo progressivo aggiungendo celle alla lista
 * delle catture, verificando l'ammissibilità di tale aggiunta tramite la matrice delle distanze e 
 * l'aggiornamento dell'<i>m_res</i>.
 */
public class Capture{
	/**
	 * La lista delle pedine attualmente catturate.
	 */
	private LinkedList<Byte> cells;
	
	/**
	 * L'<i>m</i> residuo aggiornato secondo le catture attuali.
	 */
	private byte m_res;
	
	/**
	 * Il codice di cella della pedina che inizia la cattura.
	 */
	private byte source;
	
	/**
	 * La matrice delle distanze tra le pedine avversarie per verificare l'estendibilità
	 * della cattura corrente.
	 */
	private byte[][] distance;
	
	/**
	 * Costruttore di Capture.
	 * @param source il codice di cella della pedina che inizia la cattura.
	 * @param m l'<i>m</i> attualmente a disposizione.
	 * @param distance la matrice delle distanze tra le pedine avversarie.
	 * @init la lista di pedine avversarie catturate è inizialmente vuota.
	 */
	public Capture(byte source, byte m, byte[][] distance){
		this.source = source; 
		cells = new LinkedList<Byte>();
		m_res = m;
		this.distance = distance;
	}
	
	/**
	 * Consente di ottenere il numero di pedine attualmente in cattura.
	 * @return il numero di pedine attualmente in cattura.
	 */
	public byte size(){
		return (byte)cells.size();
	}
	
	/**
	 * Verifica se la lista delle catture attuali contiene una data pedina.
	 * @param cell la cella da verificare.
	 * @return true se la cella è contenuta nella lista delle catture attuali, false altrimenti.
	 */
	public boolean contains(byte cell){
		return cells.contains(cell);
	}
	
	/**
	 * Consente di accedere alla lista delle catture attuali.
	 * @return la lista delle catture attuali.
	 */
	public LinkedList<Byte> getCells(){
		return cells;
	}
	
	/**
	 * Consente di accedere alla pedina che inizia la cattura.
	 * @return il codice di cella della pedina che inizia la cattura.
	 */
	public byte getSource(){
		return source;
	}
	
	/**
	 * Espande la lista delle pedine in cattura con una pedina data.  
	 * @param cell il codice di cella della pedina da aggiungere.
	 */
	public void expandWith(byte cell){
		if(cells.isEmpty())
			cells.add(cell);
		else{
			byte lastCell = cells.getLast();
			byte dist = distance[lastCell][cell];
			cells.addLast(cell);
			m_res = (byte)(m_res - dist);
		}
	}
    
	/**
	 * Verifica se la lista delle pedine in cattura può essere estesa con una data pedina.
	 * @param cell il codice di cella della pedina che si vorrebbe aggiungere.
	 * @return true se la lista delle pedine in cattura può essere estesa, false altrimenti.
	 */
	public boolean canExpandWith(byte cell){
		byte lastCell = cells.getLast();
		return (distance[lastCell][cell] <= m_res);
	}
}

