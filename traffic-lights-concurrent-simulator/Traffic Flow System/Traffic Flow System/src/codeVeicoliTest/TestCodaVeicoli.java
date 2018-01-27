package codeVeicoliTest;

import junit.framework.TestCase;
import codeVeicoli.*;
import orologio.*;
import veicoli.*;
import semafori.*;
import sistema.*;

public class TestCodaVeicoli extends TestCase {

	CodaVeicoli coda;
	Orologio o;
	Semaforo s;
	FactoryVeicolo factory;
	
	protected void setUp(){
		o = Orologio.getIstanza(4000);
		s =  new SemaforoNormale();
		factory = new FactoryVeicoloEO();
		coda = new CodaVeicoli(o , s, factory , 1, Direzioni.EST);
	}
	
	protected void tearDown(){
		o = null;
		coda = null;
	}
	
	public void testEnter(){
		assertTrue("la coda dovrebbe essere vouta",coda.getSize()==0);
		assertTrue("la coda dovrebbe avere una lunghezza =0 ", coda.getLunghezza()==0);
		coda.enter();
		assertTrue("la coda non dovrebbe essere vuota",coda.getSize()==1);
		assertTrue("la coda dovrebbe avere una lunghezza >=0 ", coda.getLunghezza()>=0);
	}
	
	public void testLeave(){
		coda.enter();
		coda.enter();
		assertEquals("ci dovrebbero essere 2 veicoli", coda.getSize(), 2);
		coda.leave();
		assertTrue("ci dovrebbe essere 1 veicolo", coda.getSize()==1);
		coda.leave();
		assertTrue("dovrebbe essere vuota", coda.getSize()==0);
		coda.leave();
		assertTrue("dovrebbe essere vuota", coda.getSize()==0);
	}

}
