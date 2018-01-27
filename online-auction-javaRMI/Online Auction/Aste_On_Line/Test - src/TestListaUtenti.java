package server;

import java.util.*;
import junit.framework.TestCase;

public class TestListaUtenti extends TestCase {

	ListaUtenti lista;
	LinkedList<AccountUtente> utenti;
	AccountUtente u0;
	
	protected void setUp(){
		lista = new ListaUtenti();
		utenti = new LinkedList<AccountUtente>();
		LinkedList<String> pref = new LinkedList<String>();
		pref.add("Computer");
		u0 = new AccountUtente("", "", pref);
	}
	
	protected void tearDown(){
		lista = null;
		utenti = new LinkedList<AccountUtente>();
	}
	
	
	public void testRegistra(){
		utenti = lista.getUtenti();
		assertTrue("la lista dovrebbe essere vuota", utenti.isEmpty());
		lista.registra(u0);
		utenti = lista.getUtenti();
		assertTrue("ci dovrebbe essere un elemento", utenti.size()==1);
		assertTrue("dovrebbe esserci u0", utenti.getFirst().getUserID().equals(u0.getUserID()));
	}
	
	public void testAutentica(){
		lista.registra(u0);
		AccountUtente ris = lista.autentica(u0.getUserID(), u0.getPassword(), u0.getURL());
		assertTrue("dovrebbe essere ritornato un account non null", ris!=null);
	}
	
	public void testDisconnetti(){
		lista.registra(u0);
		AccountUtente ris = lista.autentica(u0.getUserID(), u0.getPassword(), u0.getURL());
		lista.disconnetti(u0.getUserID());
		utenti = lista.getUtenti();
		assertTrue("ci dovrebbe essere solo u0", utenti.size()==1);
		AccountUtente ut = utenti.getFirst();
		assertTrue("dovrebbe essere null", ut.getURL()==null);
	}	
	
}
