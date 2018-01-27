package codeVeicoli;

import orologio.*;
import java.util.Observable;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import semafori.*;
import sistema.Direzioni;
import veicoli.*;

public class CodaVeicoli implements TimeObserver {

	// l'attraversamento dura sempre 5 secondi
	private static final int tempoPassaggio = 5000;
	private Orologio orologio;
	private int tempoCorrente;
	private Semaforo sem;
	private FactoryVeicolo factory;
	private LinkedList<Veicolo> coda;
	private boolean autoPassateGiallo = false;
	private double pVeicolo;
	private String direzione ;
	private volatile int tempoUscita;

	/**
	  * Crea un oggetto CodaVeicoli, avente la responsabilità
	  * di gestire le code di veicoli facendone entrare e uscire
	  * ogni secondo, secondo regole prefissate.
	  *
	  */
	public CodaVeicoli(Orologio o, Semaforo s, FactoryVeicolo f, double pVeicolo, String d){
		this.orologio = o;
		factory = f;
		sem = s;
		tempoCorrente = 0;
		orologio.addObserver(this);
		coda = new LinkedList<Veicolo>();
		this.pVeicolo = pVeicolo;
		direzione = d;
		tempoUscita = 0;
	}


   /**
   	 * Aggiorna il tempo dell'orologio, chiamando l'altro metodo update
   	 *
	 * @param l'oggetto Observable, nella fattispecie il Timer, che notifica
	 * 		 il nuovo tempo
	 * @param un Object "arg" che, di fatto, conterrà il nuovo tempo che verrà
	 *		 usato per aggiornare il tempo di sistema
	 */
	public void update(Observable o, Object arg){
		int nuovoTempo = ((Integer)arg).intValue();
		update(nuovoTempo);
	}

   /**
   	 * Aggiorna il nuovo tempo e intraprende le opportune azioni:
   	 * Fa entrare o uscire eventualmente i veicoli dalla coda, controllando
   	 * lo stato dei semafori e lo stato di attraversameto dell'incrocio.
	 * @param un intero che è il nuovo tempo di sistema
	 */
	public void update(int nuovoTempo){
		tempoCorrente = nuovoTempo;
		enter();
		if(sem.getStato()==Semaforo.ROSSO){
			autoPassateGiallo = false;
			return;
		}
		else if(sem.getStato()==Semaforo.VERDE && !coda.isEmpty()){
			if(tempoUscita >= 5){
				tempoUscita = 0;
				leave();
			}
			else if(tempoUscita < 5){
				tempoUscita++;
				return;
			}
		}
		else if(sem.getStato()==Semaforo.GIALLO && autoPassateGiallo==false && !coda.isEmpty()){
			if(tempoUscita >= 5){
				tempoUscita = 0;
				leave();
				autoPassateGiallo = true;
			}
			else if(tempoUscita < 5){
				tempoUscita++;
				return;
			}
		}
	}

	/**
	  * Fa entrare un veicolo in coda, in modo random:
	  * genera un numero casuale e se questo è maggiore di
	  * una certa prefissata probabilità, genera un veicolo
	  * e lo mette in coda.
	  */
	public void enter(){
		double r = Math.random();
		if(r >= pVeicolo)
			return;
		Veicolo v = factory.creaVeicolo();
		if(direzione == Direzioni.NORD || direzione == Direzioni.SUD){
			sem.richiestaPassaggio();
		}
		coda.addLast(v);
	}


	/**
	  * Fa uscire un veicolo dalla coda.
	  */
	public void leave(){
		if(!coda.isEmpty())
			coda.removeFirst();
	}

	/**
	  *@return un double che è la lunghezza della
	  * 	   	coda in metri.
	  */
	public double getLunghezza(){
		double lunghezza = 0;
		for(Veicolo v: coda)
			lunghezza += v.getLunghezza();
		return lunghezza;
	}

	/**
	  *@return un int che è il numero di veicoli in coda.
	  */
	public int getSize(){
		return coda.size();
	}

	/**
	  * @return la LinkedList<Veicolo> che contiene i veicoli ed
	  *		 	effettivamente implementa la coda dei veicoli.
	  *
	  */
	public LinkedList<Veicolo> getCoda(){
		return coda;
	}


}
