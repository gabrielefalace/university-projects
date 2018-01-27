package server;

import java.util.*;
import junit.framework.TestCase;

public class TestListaArticoli extends TestCase {

	ListaArticoli lista;
	LinkedList<Articolo> articoli;
	Articolo a0; 
	
	protected void setUp(){
		lista = new ListaArticoli();
		articoli = new LinkedList<Articolo>();
		a0 = new Articolo("id0", "nome0", "desc0", "prop0", 10.00);
	}
	
	protected void tearDown(){
		lista = null;
		articoli = new LinkedList<Articolo>();
		a0 = new Articolo("id0", "nome0", "desc0", "prop0", 10.00);
	}
	
	public void testInviaArticolo(){
		assertTrue("la lista dovrebbe essere vuota", lista.getArticoli("TUTTO").isEmpty());
		lista.inviaArticolo(a0);
		assertFalse("la lista non dovrebbe essere vuota", lista.getArticoli("TUTTO").isEmpty());
		Articolo a = lista.getArticolo(a0.getArticoloID());
		assertTrue("nella lista ci dovrebbe essere a0", a.getArticoloID().equals(a0.getArticoloID()));
		lista.inviaArticolo(a0);
		LinkedList<Articolo> presenti = lista.getArticoli("TUTTO");
		assertTrue("ci deve essere solo a0", presenti.size()==1);
	}
	
	public void testAttivaDisattivaArticolo(){
		lista.inviaArticolo(a0);
		Articolo a = lista.getArticolo(a0.getArticoloID());
		assertTrue("nella lista ci dovrebbe essere a0", a.getArticoloID().equals(a0.getArticoloID()));
		assertFalse("l'articolo deve essere inattivo", a.getDisponibile());
		lista.attivaArticolo(a.getArticoloID());
		a = lista.getArticolo(a0.getArticoloID());
		assertTrue("l'articolo deve essere attivo", a.getDisponibile());
		lista.disattivaArticolo(a.getArticoloID());
		a = lista.getArticolo(a0.getArticoloID());
		assertFalse("l'articolo deve essere inattivo", a.getDisponibile());
	}
	
	public void testFaiOfferta(){
		lista.inviaArticolo(a0);
		lista.attivaArticolo(a0.getArticoloID());
		assertTrue("il prezzo dovrebbe essere 10.00", a0.getPrezzo()==10.00);
		lista.faiOfferta(a0.getArticoloID(), "XXX", 25.00);
		Articolo nuovoA0 = lista.getArticolo(a0.getArticoloID());
		assertTrue("il nuovo prezzo dovrebbe essere 25.00", nuovoA0.getPrezzo()==25.00);
		assertTrue("il nuovo proprietario dovrebbe essere XXX", nuovoA0.getProprietarioID().equals("XXX"));
	}
	
	
	
	
	
}
