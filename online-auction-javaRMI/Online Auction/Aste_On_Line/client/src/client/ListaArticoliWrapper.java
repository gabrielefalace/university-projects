package client;

import common.*;
import java.util.*;
import java.util.concurrent.locks.*;

public class ListaArticoliWrapper {

	private LinkedList<Articolo> listaArticoli;
	private Lock lock;

	public ListaArticoliWrapper(){
		listaArticoli = new LinkedList<Articolo>();
		lock = new ReentrantLock(true);
	}

	/**
	  * Aggiunge un articolo alla lista
	  *@param a di tipo Articolo che è l'articolo che
	  */
	public void add(Articolo a){
		lock.lock();
		try{
			listaArticoli.add(a);
		}
		finally{
			lock.unlock();
		}
	}

	/**
	  * Sostituisce la vecchia lista con una nuova, aggiornata.
	  *@param l di tipo LinkedList<Articolo>
	  */
	public void aggiornaLista(LinkedList<Articolo> l){
		lock.lock();
		try{
			listaArticoli = l;
		}
		finally{
			lock.unlock();
		}
	}

	/**
	  * Consente di accedere alla lista degli articoli.
	  *@return LinkedList<Articolo> che è la lista di articoli
	  *		   attuale di questo client.
	  */
	public LinkedList<Articolo> getArticoli(){
		lock.lock();
		try{
			return listaArticoli;
		}
		finally{
			lock.unlock();
		}
	}

}
