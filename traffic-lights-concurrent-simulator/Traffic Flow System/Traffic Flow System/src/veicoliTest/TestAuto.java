package veicoliTest;

import junit.framework.TestCase;
import veicoli.*;

public class TestAuto extends TestCase {
	

	private Auto a;
	
	protected void setUp(){
		a = new Auto();
	}
	
	protected void tearDown(){
		a = null;
	}
	
	
	public void testAccelera(){
		assertTrue(a.getSpeed()==0);
		a.accelera(50);
		assertEquals(a.getSpeed(), 50);
		a.accelera(50);
		assertEquals(a.getSpeed(), 100);
		a.accelera(50);
		assertEquals(a.getSpeed(), 120);
		assertTrue(a.checkInvariant());
	}
	
	public void testDecelera(){
		assertEquals(a.getSpeed(), 0);
		a.accelera(50);
		assertEquals(a.getSpeed(), 50);
		assertTrue(a.checkInvariant());
		a.decelera(20);
		assertTrue(a.checkInvariant());
		assertEquals(a.getSpeed(), 30);
		a.decelera(30);
		assertTrue(a.checkInvariant());
		assertEquals(a.getSpeed(), 0);
		a.decelera(20);
		assertTrue(a.checkInvariant());
		assertEquals(a.getSpeed(), 0);
	}

}
