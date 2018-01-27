package server;

import common.*;
import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

public class ArticoliManager_Impl extends UnicastRemoteObject implements ArticoliManager{

	static final long  serialVersionUID = 4;
	private ListaArticoli listaArticoli ;

	/**
	  * Crea un ArticoliManager_Impl che consente agli utenti di inserire
	  * articoli da mettere all'asta o di fare offerte per quelli già presenti
	  *
	  */
	public ArticoliManager_Impl(ListaArticoli lista) throws RemoteException{
		super();
		listaArticoli = lista;
	}

	/**
	  * Consente di ottenere gli articoli che rispondono ad un dato nome indicativo.
	  * @param nomeIndicativo String che è il nome generico di un articolo
	  * @return LinkedList<Articolo> la lista con un certo tipo di articolo
	  */
	public LinkedList<Articolo> getArticoli(String nomeIndicativo) throws RemoteException {
		return listaArticoli.getArticoli(nomeIndicativo);
	}


	/**
	  * Consente di ottenere l'articolo che risponde ad un certo identificativo.
	  * @param articoloID String che è l'identificativo dell'articolo da reperire
	  * @return Articolo che è l'articolo cercato
	  */
	public Articolo getArticolo(String articoloID) throws RemoteException {
		return listaArticoli.getArticolo(articoloID);
	}


	/**
	  * Consente di inviare un articolo affinchè sia messo all'asta.
	  * @param a Articolo che si vuole mettere all'asta
	  * @return boolean che è true se l'invio va a buon fine, false altrimenti
	  */
	public boolean inviaArticolo(Articolo a) throws RemoteException {
		return listaArticoli.inviaArticolo(a);
	}


	/**
	  * Consente di fare una offerta per un articolo all'asta.
	  * @param articoloID di tipo String che è l'identificativo dell'articolo verso
	  *			cui si fa l'offerta.
	  * @param userID di tipo String che è l'identificativo dell'autore dell'offerta
	  * @param prezzo di tipo double che è il valore in euro dell'offerta
	  * @return boolean che è true se l'offerta va a buon fine
	  */
	public boolean faiOfferta(String articoloID, String userID, double prezzo) throws RemoteException {
		return listaArticoli.faiOfferta(articoloID, userID, prezzo);
	}

}
