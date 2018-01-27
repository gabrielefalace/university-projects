package semafori;

import java.awt.Toolkit;

public class BeeperThread extends Thread{

	private volatile boolean continua = true;
	private Semaforo snv;

	public BeeperThread(Semaforo snv){
		this.snv = snv;
	}

	/**
	  * Avvia l'inizio del beep prodotto dal thread.
	  */
	public void startBeep(){
		start();
	}

	/**
	  * Arresta il beep prodotto dal thread.
	  */
	public void endBeep(){
		continua = false;
	}

	/**
	  * Mantiene in attività il thread che continua ad
	  * emettere il segnale acustico.
	  */
	public void run(){
		while(continua){
				if(snv.getStato() == Semaforo.VERDE){
					Toolkit.getDefaultToolkit().beep();
					try{ Thread.sleep(700); }
					catch (InterruptedException e){}
				}
				else if(snv.getStato() == Semaforo.GIALLO){
					Toolkit.getDefaultToolkit().beep();
					try{ Thread.sleep(300); }
					catch (InterruptedException e){}
				}
		}
	}

}