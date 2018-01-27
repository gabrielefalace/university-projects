package interfaces;

import core.BoardConfiguration;

/**
 * Interfaccia che rappresenta una funzione euristica.
 */
public interface Heuristic {

	/**
	 * Consente di valutare una data configurazione. Suppone che il protagonista sia 
	 * il giocatore BIANCO. Se la configurazione � vantaggiosa per il protagonista (BIANCO)
	 * il valore calcolato dall'euristica sar� positivo; se, invece � svantaggiosa, sar� negativo.  
	 * @param bc la configurazione da valutare.
	 * @return il grado di qualit�.
	 */
	public byte evaluate(BoardConfiguration bc);	
}
