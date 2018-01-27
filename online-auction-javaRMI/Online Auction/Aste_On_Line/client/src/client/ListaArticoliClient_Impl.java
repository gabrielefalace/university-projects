package client;

import common.*;
import java.util.*;
import java.rmi.*;
import java.rmi.server.*;

public class ListaArticoliClient_Impl extends UnicastRemoteObject implements ListaArticoliClient{

	private ListaArticoliWrapper listaArticoli;

	/**
	  * @param law di tipo ListaArticoliWrapper che serve per effettuare
	  *		   le effettive operazioni sui dati del Client.
	  */
	public ListaArticoliClient_Impl(ListaArticoliWrapper law) throws RemoteException{
		listaArticoli = law;
	}

	/*
	 * @param lista di tipo LinkedList<Articolo> contiene una nuova lista
	 *        per aggiornare l' attributo del Client.
	 */
	public void aggiornaLista(LinkedList<Articolo> lista) throws RemoteException {
		listaArticoli.aggiornaLista(lista);
	}



}
