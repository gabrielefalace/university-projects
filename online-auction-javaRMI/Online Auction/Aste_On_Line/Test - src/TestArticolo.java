package server;

import junit.framework.TestCase;

public class TestArticolo extends TestCase {

	Articolo a;
	
	protected void setUp(){
		a = new Articolo("id", "nome", "desc", "prop", 20.30);
	}
	
	protected void tearDown(){
		a = null;
	}
	
	public void testAttiva(){
		assertFalse("l'articolo devrebbe essere inattivo", a.getDisponibile());
		a.setDisponibile(true);
		assertTrue("l'articolo devrebbe essere attivo", a.getDisponibile());
	}
	
	public void testOfferta(){
		a.setDisponibile(true);
		assertTrue("il prezzo iniziale dovrebbe essere 20.30",a.getPrezzo()==20.30);
		assertTrue("il proprietario iniziale dovrebbe essere prop",a.getProprietarioID().equals("prop"));
		a.eseguiOfferta("ABC", 20.00);
		assertTrue("il prezzo iniziale dovrebbe essere 20.30",a.getPrezzo()==20.30);
		assertTrue("il proprietario iniziale dovrebbe essere prop",a.getProprietarioID().equals("prop"));
		a.eseguiOfferta("ABC", 25.00);
		assertTrue("il prezzo dovrebbe essere 25",a.getPrezzo()==25.00);
		assertTrue("il proprietario dovrebbe essere ABC",a.getProprietarioID().equals("ABC"));
	}
	
	public void setChiusura(){
		assertTrue("l'articolo non dovrebbe avere data di chiusura", a.getDataChiusura()==null);
		a.setDataChiusura(new Data(12, 10, 10, 10));
		assertTrue("la data di chiusura dovrebbe essere 12/10 10:10", a.getDataChiusura().equals("12/10 10:10"));
		assertFalse("l'articolo devrebbe essere inattivo", a.getDisponibile());
		a.setDisponibile(true);
		assertTrue("l'articolo devrebbe essere attivo", a.getDisponibile());
		a.setDisponibile(false);
		assertFalse("l'articolo devrebbe essere inattivo", a.getDisponibile());
	}
	
}
