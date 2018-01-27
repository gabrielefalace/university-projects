package client;

import java.util.*;
import junit.framework.TestCase;

public class TestListaArticoliWrapper extends TestCase {

	ListaArticoliWrapper lista;
	LinkedList<Articolo> articoli;
	Articolo a;
	 
	protected void setUp(){
		lista = new ListaArticoliWrapper();
		articoli = new LinkedList<Articolo>();
		a = new Articolo("id", "nome", "desc", "prop", 20.00);
	}
	
	protected void tearDown(){
		lista = null;
		articoli = null;
	}
	
	public void testAdd(){
		articoli = lista.getArticoli();
		assertTrue("la lista dovrebbe essere vuota", articoli.isEmpty());
		lista.add(a);
		assertFalse("la lista non dovrebbe essere vuota", articoli.isEmpty());
	}
	
	public void testAggiorna(){
		LinkedList<Articolo> altraLista = new LinkedList<Articolo>();
		altraLista.add(a);
		lista.aggiornaLista(altraLista);
		articoli = lista.getArticoli();
		assertTrue("ci dovrebbe essere un elemento nella lista", articoli.size()==1);
	}
		
}
