package server;

import common.*;
import java.io.*;
import java.util.LinkedList;
import java.util.concurrent.locks.*;

public class ListaUtenti implements Serializable{

	private LinkedList<AccountUtente> listaClienti;
	private transient ReentrantLock listaLock;

	/**
	  * Crea una ListaUtenti vuota
	  */
	public ListaUtenti(){
		listaClienti = new LinkedList<AccountUtente>();
		listaLock = new ReentrantLock(true);
	}

	/**
	  * Consente ad un utente registrato di autenticarsi per accedere al sistema.
	  * @param userID di tipo String che è l'identificativo dell'utente
	  * @param password di tipo String che è la password dell'utente
	  * @param URL di tipo String che è l'URL dell'utente
	  * @return AccountUtente relativo all'utente autenticato
	  */
	public AccountUtente autentica(String userID, String password, String URL){
		listaLock.lock();
		try{
			for(AccountUtente iter: listaClienti){
				if(iter.getUserID().equals(userID) && iter.getPassword().equals(password)
					&& !iter.getDisattivato()){
						iter.setURL(URL);
						return iter;
				}
			}
			return null;
		}
		finally{
			listaLock.unlock();
		}
	}


	/**
	  * Consente la disconnessione all' utente.
	  * @param userID di tipo String che è l'identificativo dell'utente
	  *		   che si vuole disconnettere.
	  */
	public void disconnetti(String userID){
		listaLock.lock();
		try{
			for(AccountUtente iter: listaClienti)
				if(iter.getUserID().equals(userID))
					iter.setURL(null);
		}
		finally{
			listaLock.unlock();
		}
	}


	/**
	  * Consente la registrazione di un utente non registrato.
	  * @param acc di tipo AccountUtente che è l'account del nuovo utente
	  * @return boolean che è true se la registrazione è andata a buon fine false
	  *		    altrimenti.
	  */
	public boolean registra(AccountUtente acc){
		listaLock.lock();
		try{
			for(AccountUtente iter: listaClienti){
				if(iter.getUserID().equals(acc.getUserID()))
					return false;
			}
			listaClienti.add(acc);
			return true;
		}
		finally{
			listaLock.unlock();
		}
	}


	/**
	  *	Consente la rimozione di un utente.
	  * @param userID String che è l'identificativo dell'utente
	  */
	public void rimuoviUtente(String userID){
		listaLock.lock();
		try{
			for(int i=0; i<listaClienti.size(); i++){
				if(listaClienti.get(i).getUserID().equals(userID))
					listaClienti.get(i).setDisattivato(true);
			}
		}
		finally{
			listaLock.unlock();
		}
	}


	/**
	  * Permette di ottenere la lista degli utenti registrati.
	  * @return LinkedList<AccountUtente> che è la lista utenti
	  */
	public LinkedList<AccountUtente> getUtenti(){
		listaLock.lock();
		try{
			LinkedList<AccountUtente> lista = new LinkedList<AccountUtente>();
			lista.addAll(listaClienti);
			return lista;
		}
		finally{
			listaLock.unlock();
		}
	}

	/**
	  * Consente di leggere da stream un oggetto di tipo ListaUtenti
	  * inizializzando a parte il lock di questo oggetto che è un campo
	  * transient, che (giustamente) non viene serializzato.
	  * @param s di tipo ObjectInputStream che è lo stream da cui l'oggetto viene letto.
	  */
	private void readObject(ObjectInputStream s)throws IOException, ClassNotFoundException{
			s.defaultReadObject();
			listaLock = new ReentrantLock(true);
	}

}
