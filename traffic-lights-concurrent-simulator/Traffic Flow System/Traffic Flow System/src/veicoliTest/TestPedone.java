package veicoliTest;

import junit.framework.TestCase;
import veicoli.*;

public class TestPedone extends TestCase {

	private Pedone p;
	
	protected void setUp(){
		p = new Pedone();
	}
	
	protected void tearDown(){
		p = null;
	}
	
	
	public void testAccelera(){
		assertTrue(p.getSpeed()==0);
		p.accelera(2);
		assertEquals(p.getSpeed(), 2);
		p.accelera(2);
		assertEquals(p.getSpeed(), 4);
		p.accelera(3);
		assertEquals(p.getSpeed(), 4);
		assertTrue(p.checkInvariant());
	}
	
	public void testDecelera(){
		assertEquals(p.getSpeed(), 0);
		p.accelera(4);
		assertEquals(p.getSpeed(), 4);
		assertTrue(p.checkInvariant());
		p.decelera(2);
		assertTrue(p.checkInvariant());
		assertEquals(p.getSpeed(), 2);
		p.decelera(2);
		assertTrue(p.checkInvariant());
		assertEquals(p.getSpeed(), 0);
		p.decelera(2);
		assertTrue(p.checkInvariant());
		assertEquals(p.getSpeed(), 0);
	}

}
