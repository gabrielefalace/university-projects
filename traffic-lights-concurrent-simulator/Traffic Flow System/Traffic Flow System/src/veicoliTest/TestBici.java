package veicoliTest;

import junit.framework.TestCase;
import veicoli.*;

public class TestBici extends TestCase {

	private Bici b;
	
	protected void setUp(){
		b = new Bici();
	}
	
	protected void tearDown(){
		b = null;
	}
	
	
	public void testAccelera(){
		assertTrue(b.getSpeed()==0);
		b.accelera(10);
		assertEquals(b.getSpeed(), 10);
		b.accelera(10);
		assertEquals(b.getSpeed(), 20);
		b.accelera(10);
		assertEquals(b.getSpeed(), 25);
		assertTrue(b.checkInvariant());
	}
	
	public void testDecelera(){
		assertEquals(b.getSpeed(), 0);
		b.accelera(20);
		assertEquals(b.getSpeed(), 20);
		assertTrue(b.checkInvariant());
		b.decelera(10);
		assertTrue(b.checkInvariant());
		assertEquals(b.getSpeed(), 10);
		b.decelera(10);
		assertTrue(b.checkInvariant());
		assertEquals(b.getSpeed(), 0);
		b.decelera(20);
		assertTrue(b.checkInvariant());
		assertEquals(b.getSpeed(), 0);
	}
	
	
}
