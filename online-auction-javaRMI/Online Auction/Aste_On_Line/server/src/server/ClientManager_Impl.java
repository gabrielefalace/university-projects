package server;

import common.*;
import java.rmi.*;
import java.rmi.server.*;

public class ClientManager_Impl extends UnicastRemoteObject implements ClientManager{

	static final long serialVersionUID = 3;
	private ListaUtenti listaClienti;


	/**
	  * Crea un ClientManager_Impl che consente agli utenti
	  * di registrarsi ed autenticarsi da remoto.
	  */
	public ClientManager_Impl(ListaUtenti lista) throws RemoteException{
		super();
		listaClienti = lista;
	}

	/**
	  * Consente ad un utente registrato di autenticarsi per accedere al sistema.
	  * @param userID di tipo String che è l'identificativo dell'utente
	  * @param password di tipo String che è la password dell'utente
	  * @param URL di tipo String che è l'URL dell'utente
	  * @return AccountUtente relativo all'utente autenticato
	  */
	public AccountUtente autentica(String userID, String password, String URL) throws RemoteException {
		return listaClienti.autentica(userID, password, URL);
	}

	/**
	  * Consente la registrazione di un utente non registrato.
	  * @param acc di tipo AccountUtente che è l'account del nuovo utente
	  * @return boolean che è true se la registrazione è andata a buon fine false
	  *		    altrimenti.
	  */
	public boolean registra(AccountUtente acc) throws RemoteException {
		return listaClienti.registra(acc);
	}


	/**
	  * Consente la disconnessione all' utente.
	  * @param userID di tipo String che è l'identificativo dell'utente
	  *		   che si vuole disconnettere.
	  */
	public void disconnetti(String userID) throws RemoteException {
		listaClienti.disconnetti(userID);
	}

}
