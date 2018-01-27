package core;

import java.math.BigDecimal;

public interface Constants {
	
	/**
	 * Bit usati per rappresentare gli indirizzi.
	 */
	int bits = 160;
	
	
	/**
	 * Seme utilizzato nelle simulazioni, sia per assegnare gli id, sia per scegliere i nodi da cui eseguire la stima.
	 */
	long seed = 35754932L;
	
	
	/**
	 * Utilizzato per selezionare i peer da far fallire.
	 */
	long failure_seed = 23945753L;
	
}
