package controller;

import java.util.Observable;
import orologio.*;
import semafori.*;
import veicoli.*;
import codeVeicoli.*;

public class Controller extends FiniteStateMachine implements TimeObserver {

	private int tempoCorrente;
	private Semaforo sNORD;
	private Semaforo sSUD;
	private Semaforo sOVEST;
	private Semaforo sEST;
	private CodaVeicoli cNORD;
	private CodaVeicoli cSUD;
	private CodaVeicoli cOVEST;
	private CodaVeicoli cEST;
	private Orologio clock;
	private final StatoController[] S = new StatoController[7];
	private int index = 0;

	/**
	  *
	  *@return un Controller che gestisce i semafori, cambiando il loro stato
	  *		   dipendentemente dal proprio.
	  *
	  */
	public Controller(Semaforo n, Semaforo s, Semaforo ov, Semaforo e,
					  Orologio o, CodaVeicoli cn, CodaVeicoli cs, CodaVeicoli co, CodaVeicoli ce)
	{
		super();
		S[0] = new Stato0();
		S[1] = new Stato1();
		S[2] = new Stato2();
		S[3] = new Stato3();
		S[4] = new Stato4();
		S[5] = new Stato5();
		S[6] = new Stato6();
		sNORD = n;
		sSUD = s;
		sOVEST = ov;
		sEST = e;
		clock = o;
		cNORD = cn;
		cSUD = cs;
		cOVEST = co;
		cEST = ce;
		clock.addObserver(this);
		transition(S[0]);
	}

	/**
	  * @return un int che è il tempo di sistema
	  */
	public int getTempoSistema(){	return tempoCorrente;	}

	 /**
	   	 * Aggiorna il tempo del controllore, chiamando l'altro metodo update
	   	 *
		 * @param l'oggetto Observable, nella fattispecie l'Orologio, che notifica
		 * 		 il nuovo tempo
		 * @param un Object "arg" che, di fatto, conterrà il nuovo tempo che verrà
		 *		 usato per aggiornare il tempo di sistema
	 */
	public void update(Observable o, Object arg){
		int nuovoTempo = ((Integer)arg).intValue();
		update(nuovoTempo);
	}

	/**
	  * Aggiorna il nuovo tempo e cambia il proprio stato. Questo cambiamento di
	  * stato, come da pattern STATE, delega agli oggetti-stato interni il compito
	  * di modificare lo stato dei semafori, governati dal controller.
	  * @param un int che è il nuovo tempo di sistema.
	  */
	public void update(int nuovoTempo){
		tempoCorrente = nuovoTempo;
		if(tempoCorrente >= clock.getTempoLimite()) sSUD.reset();
		if((tempoCorrente - S[index].entryTime) > S[index].deltaT){
			if(currentState()==S[0] && !(sNORD.getPassaggioRichiesto()||sSUD.getPassaggioRichiesto()))
				return;
			index = (index+1)%7;
			transition(S[index]);
		}
	}


	/**
	  * Effettua, per delegation, le azioni da compiere
	  * quando il sistema entra in questo stato
	  */
	private class Stato0 extends StatoController{

		public Stato0(){
			super.deltaT = 120;
		}

		public void action(){
			entryTime = tempoCorrente;
			sEST.cambiaStato();
			sOVEST.cambiaStato();
		}
	}

	/**
	  * Effettua, per delegation, le azioni da compiere
	  * quando il sistema entra in questo stato
	  */
	  private class Stato1 extends StatoController{
		public Stato1(){
			super.deltaT = (int)(Math.random()*60);
		}
		public void action(){
			entryTime = tempoCorrente;
		}
	}

	/**
	  * Effettua, per delegation, le azioni da compiere
	  * quando il sistema entra in questo stato
	  */
	private class Stato2 extends StatoController{
		public Stato2(){
			super.deltaT = 12;
		}
		public void action(){
			entryTime = tempoCorrente;
			sEST.cambiaStato();
			sOVEST.cambiaStato();
		}
	}

  	/**
  	  * Effettua, per delegation, le azioni da compiere
  	  * quando il sistema entra in questo stato
	  */
	private class Stato3 extends StatoController{
		public Stato3(){
			super.deltaT = 5;
		}
		public void action(){
			entryTime = tempoCorrente;
			sEST.cambiaStato();
			sOVEST.cambiaStato();
		}
	}

  /**
	* Effettua, per delegation, le azioni da compiere
	* quando il sistema entra in questo stato
	*/
	private class Stato4 extends StatoController{

		public Stato4(){
			super.deltaT = 60;
		}
		public void action(){
			entryTime = tempoCorrente;
			sNORD.passaggioAccordato();
			sSUD.passaggioAccordato();
			sNORD.cambiaStato();
			sSUD.cambiaStato();
		}
	}

  /**
	* Effettua, per delegation, le azioni da compiere
	* quando il sistema entra in questo stato
	*/
	private class Stato5 extends StatoController{
		public Stato5(){
			super.deltaT = 20;
		}
		public void action(){
			entryTime = tempoCorrente;
			sSUD.cambiaStato();
			sNORD.cambiaStato();
		}
	}

  /**
    * Effettua, per delegation, le azioni da compiere
	* quando il sistema entra in questo stato
	*/
	private class Stato6 extends StatoController{
		public Stato6(){
			super.deltaT = 5;
		}
		public void action(){
			sSUD.cambiaStato();
			sNORD.cambiaStato();
		}
	}



}
