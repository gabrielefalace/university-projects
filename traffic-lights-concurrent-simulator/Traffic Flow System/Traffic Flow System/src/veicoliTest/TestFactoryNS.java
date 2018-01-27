package veicoliTest;

import junit.framework.TestCase;
import veicoli.*;

public class TestFactoryNS extends TestCase {
	
	private FactoryVeicoloNS f;
	
	protected void setUp(){
		f = new FactoryVeicoloNS();
	}
	
	protected void tearDown(){
		f = null;
	}
	
	public void testGenera(){
		// test condizioni regolari
		Veicolo v = f.genera(0.5);
		assertTrue(f.checkInvariant());
		assertTrue("tipo sbagliato", v instanceof Pedone);
		v = f.genera(0.95);
		assertTrue("tipo sbagliato", v instanceof Bici);
		// test Boundary Conditions
		v = f.genera(0);
		assertTrue("tipo sbagliato", v instanceof Pedone);
		v = f.genera(0.9);
		assertTrue("tipo sbagliato", v instanceof Pedone);
	}
	
}
