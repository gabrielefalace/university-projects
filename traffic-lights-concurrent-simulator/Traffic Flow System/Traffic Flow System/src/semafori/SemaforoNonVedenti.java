package semafori;

public class SemaforoNonVedenti extends SemaforoPedoni{


	BeeperThread beeper;

	public SemaforoNonVedenti(){
		super();
	}

	/**
	  * Cambia lo stato del semaforo in modo circolare,
	  * attraversando nell' ordine ROSSO, VERDE, GIALLO.
	  * Dipendentemente dallo stato viene attivato/disattivato
	  * il thread che emette il segnale acustico.
	  */
	public void cambiaStato(){
		if(currentState == ROSSO){
			currentState = VERDE;
			beeper = new BeeperThread(this);
			beeper.startBeep();
		}
		else if(currentState == VERDE){
			currentState = GIALLO;
		}
		else if(currentState == GIALLO){
			currentState = ROSSO;
			beeper.endBeep();
		}
	}


	/**
	  * Consente di bloccare il thread che emette il beep,
	  * utile per quanto il sistema deve terminare.
	  */
	public void reset(){
		beeper.endBeep();
	}




}
