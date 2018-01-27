package semaforiTest;

import semafori.*;
import junit.framework.TestCase;

public class TestSemaforoNonVedenti extends TestCase {

	private SemaforoNonVedenti s;
	
	protected void setUp(){
		s = new SemaforoNonVedenti();
	}
	
	protected void tearDown(){
		s = null;
	}
	
	public void testCambiaStato(){
		assertEquals("all'inizio il semaforo è rosso", Semaforo.ROSSO, s.getStato());
		
		s.cambiaStato();
		assertTrue(s.checkInvariant());
		assertEquals("ora il semaforo è verde",  Semaforo.VERDE, s.getStato());
		
		s.cambiaStato();
		assertTrue(s.checkInvariant());
		assertEquals("ora il semaforo è giallo", Semaforo.GIALLO, s.getStato());
		
		s.cambiaStato();
		assertTrue(s.checkInvariant());
		assertEquals("ora deve essere tornato rosso", Semaforo.ROSSO, s.getStato());
	}
	
	
}
