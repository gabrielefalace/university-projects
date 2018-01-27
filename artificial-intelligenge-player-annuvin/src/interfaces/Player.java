package interfaces;

import core.BoardConfiguration;

/**
 * Interfaccia che rappresenta un giocatore. Nota: le implementazioni 
 * hanno la responsabilità di fornire una specifica politica di esplorazione 
 * dell'albero di gioco.
 */
public interface Player {

	/**
	 * Consente di ottenere la migliore configurazione tra le successive di una data.
	 * @param c la configurazione attuale.
	 * @return la migliore configurazione successiva.
	 */
	public BoardConfiguration getBestNextConfiguration(BoardConfiguration c);
	
}
