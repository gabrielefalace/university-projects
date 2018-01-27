package semaforiTest;

import semafori.SemaforoNormale;
import junit.framework.TestCase;

public class TestSemaforoNormale extends TestCase {

	private SemaforoNormale sN ; 
	
	public TestSemaforoNormale(String nome){
		super(nome);
	}
	
	protected  void setUp(){
		sN = new SemaforoNormale();
	}
	
	protected void tearDown(){
		sN = null;
	}
	
	public void testCambiaStato(){
		assertEquals("all'inizio il semaforo è rosso", SemaforoNormale.ROSSO, sN.getStato());
		
		sN.cambiaStato();
		assertTrue(sN.checkInvariant());
		assertEquals("il semaforo dovrebbe essere verde", SemaforoNormale.VERDE, sN.getStato());
		
		sN.cambiaStato();
		assertTrue(sN.checkInvariant());
		assertEquals("il semaforo dovrebbe essere giallo", SemaforoNormale.GIALLO, sN.getStato());
		
		sN.cambiaStato();
		assertTrue(sN.checkInvariant());
		assertEquals("il semaforo dovrebbe essere tornato rosso", SemaforoNormale.ROSSO, sN.getStato());
	}
	
	public void testGetMessaggio(){
		assertEquals(sN.getMessaggio(), "ROSSO");
		sN.cambiaStato();
		sN.checkInvariant();
		assertEquals(sN.getMessaggio(), "VERDE");
		sN.cambiaStato();
		sN.checkInvariant();
		assertEquals(sN.getMessaggio(), "GIALLO");
		sN.cambiaStato();
		sN.checkInvariant();
		assertEquals(sN.getMessaggio(), "ROSSO");
	}
	
	
}
