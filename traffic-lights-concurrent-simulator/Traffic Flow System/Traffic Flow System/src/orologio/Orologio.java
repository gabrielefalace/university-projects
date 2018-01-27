package orologio;

import java.util.Observable;

public class Orologio extends Observable implements Sveglia{

	private static Orologio istanza = new Orologio();
	private static int tempoLimite;
	private static int tempoCorrente;
	private Timer timer;
	private Thread t ;


	/**
	  *	 @param
	  *  @return crea l'unica istanza dell'oggetto SINGLETON Orologio
	  */
	private Orologio(){
		tempoCorrente = 0;
		tempoLimite = 600;
		timer = new Timer(this);
		timer.set(1000, true);
	}

	/**
	  *	@param un int che è il tempo per cui rimane in esecuzione il progetto
	  * @return l'unica istanza di Orologio disponibile
	  */
	public static synchronized Orologio getIstanza(int limite){
		if(limite >= 600 && limite < 1200)
			tempoLimite = limite;
		return istanza;
	}

	/**
	  * Implementa l'aggiornamento del tempo di sistema
	  * con conseguente notifica degli osservatori del tempo
	  * (detti TimeObserver)
	  */
	public synchronized void suona(){
		tempoCorrente++;
		if(tempoCorrente > tempoLimite){
			deleteObservers();
			timer.cancel();
		}
		else{
			setChanged();
			notifyObservers(tempoCorrente);
		}
	}

	/**
      *
	  * @return l' int che segna il tempo di esecuzione
	  * 		del sistema
	  */
	public int getTempoLimite(){
		return tempoLimite;
	}

}
