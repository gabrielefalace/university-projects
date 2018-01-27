package semafori;

public abstract class Semaforo {

	public static final int ROSSO = 0;
	public static final int VERDE = 1;
	public static final int GIALLO = 2;


	protected volatile int currentState;
	protected volatile boolean passaggioRichiesto;

	public synchronized void cambiaStato(){
		currentState = (currentState+1)%3;
	}

	/**
	  *@return un int che è lo "stato" del semaforo, può essere
	  *		   ROSSO (=0), VERDE (=1) oppure GIALLO (=2).
	  */
	public int getStato(){
		return currentState;
	}

	/**
	  *@return true se è stato richiesto un passaggio
	  *        false altrimenti.
	  */
	public boolean getPassaggioRichiesto(){
		return passaggioRichiesto;
	}

	/**
	  * Richiede un passaggio tramite la messa a true dell' apposito
	  * attributo del semaforo.
	  */
	public void richiestaPassaggio(){
		passaggioRichiesto = true;
	}


	/**
	  * Riporta a false l'attributo di passaggio richiesto, per far
	  * si che siano possibili nuove prenotazioni.
	  */
	public void passaggioAccordato(){
		passaggioRichiesto = false;
	}

	public boolean checkInvariant() {
		if( !(((currentState==ROSSO)^(currentState==GIALLO))^currentState==VERDE) )
			return false;
		return true;
	}

	/**
	  * consente, se sovracsritto, di modificare alcune proprietà
	  * del semaforo durante il suo funzionamento.
	  */
	public void reset(){}

	/**
	  * Consente di ottenere un messaggio dal semaforo,
	  * che può essere utile, ad esempio, per una sua
	  * rappresentazione grafica.
	  *@return ritorna una String che è il messaggio
	  *        proveniente dal semaforo.
	  *
	  */
	public abstract String getMessaggio();
}


