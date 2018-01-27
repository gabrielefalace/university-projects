package interfaces;

/**
 * Costanti di uso globale nel sistema.
 */
public interface GlobalValues {
	
	/**
	 * Numero di celle della scacchiera.
	 */
	public static final byte CELLS_NUMBER = 37; 
	
	/**
	 * Turno del BIANCO.
	 */
	public static boolean WHITE_TURN = true;
	
	/**
	 * Turno del NERO.
	 */
	public static boolean BLACK_TURN = false;
	
	/**
	 * Rappresenta che una configurazione non è valutata.
	 */
	public static byte UNLABELED = -120;
	
}
