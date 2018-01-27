package veicoliTest;

import junit.framework.TestCase;
import veicoli.*;

public class TestBus extends TestCase {

	private Bus b;
	
	protected void setUp(){
		b = new Bus();
	}
	
	protected void tearDown(){
		b = null;
	}
	
	
	public void testAccelera(){
		assertTrue(b.getSpeed()==0);
		b.accelera(50);
		assertEquals(b.getSpeed(), 50);
		b.accelera(42);
		assertEquals(b.getSpeed(), 92);
		b.accelera(50);
		assertEquals(b.getSpeed(), 92);
		assertTrue(b.checkInvariant());
	}
	
	public void testDecelera(){
		assertEquals(b.getSpeed(), 0);
		b.accelera(50);
		assertEquals(b.getSpeed(), 50);
		assertTrue(b.checkInvariant());
		b.decelera(20);
		assertTrue(b.checkInvariant());
		assertEquals(b.getSpeed(), 30);
		b.decelera(30);
		assertTrue(b.checkInvariant());
		assertEquals(b.getSpeed(), 0);
		b.decelera(20);
		assertTrue(b.checkInvariant());
		assertEquals(b.getSpeed(), 0);
	}

}
