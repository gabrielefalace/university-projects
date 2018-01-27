package server;

import common.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.locks.*;

public class ListaArticoli implements Serializable{

	private LinkedList<Articolo> listaArticoli;
	private transient ReentrantReadWriteLock listaLock;
	private transient Lock read, write;

	/**
	  * Crea una lista di articoli vuota
	  *
	  */
	public ListaArticoli(){
		listaArticoli = new LinkedList<Articolo>();
		listaLock = new ReentrantReadWriteLock(true);
		read = listaLock.readLock();
		write = listaLock.writeLock();
	}


	/**
	  * Consente di ottenere gli articoli che rispondono ad un dato nome indicativo.
	  * @param nomeIndicativo String che è il nome generico di un articolo
	  * @return LinkedList<Articolo> la lista con un certo tipo di articolo
	  */
	public LinkedList<Articolo> getArticoli(String nomeIndicativo){
		read.lock();
		try{
			LinkedList<Articolo> listaDaRitornare = new LinkedList<Articolo>();
			if(listaArticoli != null){
				for(Articolo iter: listaArticoli){
					if((iter.getNomeIndicativo().equals(nomeIndicativo) && iter.getDisponibile()==true)
						|| nomeIndicativo.equals("TUTTO"))
						listaDaRitornare.add(iter);
				}
			}
			return listaDaRitornare;
		}
		finally{
			read.unlock();
		}
	}


	/**
	  * Consente di ottenere l'articolo che risponde ad un certo identificativo.
	  * @param articoloID String che è l'identificativo dell'articolo da reperire
	  * @return Articolo che è l'articolo cercato
	  */
	public Articolo getArticolo(String articoloID) {
		read.lock();
		try{
			for(Articolo iter: listaArticoli){
				if(iter.getArticoloID().equals(articoloID))
					return iter;
			}
			return null;
		}
		finally{
			read.unlock();
		}
	}


	/**
	  * Consente di inviare un articolo affinchè sia messo all'asta.
	  * @param a Articolo che si vuole mettere all'asta
	  * @return boolean che è true se l'invio va a buon fine, false altrimenti
	  */
	public boolean inviaArticolo(Articolo a) {
		write.lock();
		try{
			boolean trovato = false;
			for(Articolo iter: listaArticoli){
				if(iter.getArticoloID().equals(a.getArticoloID()))
					trovato = true;
			}
			if(trovato==false){
				listaArticoli.add(a);
				return true;
			}
			else
				return false;
		}
		finally{
			write.unlock();
		}
	}


	/**
	  * Consente di fare una offerta per un articolo all'asta.
	  * @param articoloID di tipo String che è l'identificativo dell'articolo verso
	  *			cui si fa l'offerta.
	  * @param userID di tipo String che è l'identificativo dell'autore dell'offerta
	  * @param prezzo di tipo double che è il valore in euro dell'offerta
	  * @return boolean che è true se l'offerta va a buon fine
	  */
	public boolean faiOfferta(String articoloID, String userID, double prezzo){
		read.lock();
		try{
			for(int i=0; i<listaArticoli.size(); i++){
				Articolo iter = listaArticoli.get(i);
				if(iter.getArticoloID().equals(articoloID) && iter.getDisponibile()==true){
					return iter.eseguiOfferta(userID, prezzo);
				}
			}
			return false;
		}
		finally{
			read.unlock();
		}
	}

	/**
	  * Consente la rimozione di un articolo.
	  * @param articoloID di tipo String che è l'ID dell'articolo da rimuovere.
	  */
	public void rimuoviArticolo(String articoloID){
		write.lock();
		try{
			for(Articolo iter: listaArticoli){
				if(iter.getArticoloID().equals(articoloID))
					listaArticoli.remove(iter);
			}
		}
		finally{
			write.unlock();
		}
	}

	/**
	  * Consente di attivare un articolo, affinchè sia fruibile per
	  * le offerte dei clienti.
	  * @param articoloID di tipo String che è l'ID dell'articolo
	  */
	public void attivaArticolo(String articoloID){
		read.lock();
		try{
			for(Articolo iter: listaArticoli)
				if(iter.getArticoloID().equals(articoloID))
					iter.setDisponibile(true);
		}
		finally{
			read.unlock();
		}
	}

	/**
	  * Consente di disattivare un articolo.
	  * @param articoloID di tipo String che è l'ID dell'articolo
	  */
	public void disattivaArticolo(String articoloID){
		read.lock();
		try{
			for(Articolo iter: listaArticoli)
				if(iter.getArticoloID().equals(articoloID))
					iter.setDisponibile(false);
		}
		finally{
			read.unlock();
		}
	}

    /**
      * Consente di ottenere gli articoli di proprietà dell' utente.
      * @param userID di tipo String che l' ID dell'utente
      * @return LinkedList<Articolo> che è la lista di articoli di proprietà
      *			dell'utente richiedente.
      */
	public LinkedList<Articolo> getArticoliProprietà(String userID){
		read.lock();
		try{
			LinkedList<Articolo> artic = new LinkedList<Articolo>();
			for(Articolo iter: listaArticoli)
				if(iter.getProprietarioID().equals(userID) && !iter.getDisponibile())
					artic.add(iter);
			return artic;
		}
		finally{
			read.unlock();
		}
	}


	/**
	  * Consente di ripristinare lo stato di un oggetto ListaArticoli, letto da uno
	  * stream inizializzando a parte i campi transient, che, per l'esattezza, sono
	  * i lock.
	  * @param s di tipo ObjectInputStream che è lo stream da cui viene letto l'oggetto
	  */
	private void readObject(ObjectInputStream s)throws IOException, ClassNotFoundException{
		s.defaultReadObject();
		listaLock = new ReentrantReadWriteLock(true);
		read = listaLock.readLock();
		write = listaLock.writeLock();
	}

}
