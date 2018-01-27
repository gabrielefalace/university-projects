package interfaces;

import core.BoardConfiguration;

/**
 * Interfaccia che rappresenta una funzione euristica.
 */
public interface Heuristic {

	/**
	 * Consente di valutare una data configurazione. Suppone che il protagonista sia 
	 * il giocatore BIANCO. Se la configurazione è vantaggiosa per il protagonista (BIANCO)
	 * il valore calcolato dall'euristica sarà positivo; se, invece è svantaggiosa, sarà negativo.  
	 * @param bc la configurazione da valutare.
	 * @return il grado di qualità.
	 */
	public byte evaluate(BoardConfiguration bc);	
}
