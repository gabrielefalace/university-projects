package veicoliTest;

import junit.framework.TestCase;
import veicoli.*;

public class TestFactoryEO extends TestCase {

	private FactoryVeicoloEO f;
	
	protected void setUp(){
		f = new FactoryVeicoloEO();
	}
	
	protected void tearDown(){
		f = null;
	}
	
	public void testGenera(){
		// test condizioni regolari
		Veicolo v = f.genera(0.5);
		assertTrue(f.checkInvariant());
		assertTrue("tipo sbagliato", v instanceof Auto);
		v = f.genera(0.85);
		assertTrue("tipo sbagliato", v instanceof Bus);
		v = f.genera(0.95);
		assertTrue("tipo sbagliato", v instanceof Bici);
		// test Boundary Conditions
		v = f.genera(0);
		assertTrue("tipo sbagliato", v instanceof Auto);
		v = f.genera(0.8);
		assertTrue("tipo sbagliato", v instanceof Auto);
		v = f.genera(0.9);
		assertTrue("tipo sbagliato", v instanceof Bus);
	}
	
}
