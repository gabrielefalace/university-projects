package annuvin;


import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.BoardConfiguration;

public class BoardConfiguration_FailingMethods {

	BoardConfiguration board;
	
	@Before
	public void setUp() throws Exception {
		board = new BoardConfiguration();
	}

	@After
	public void tearDown() throws Exception {
		board = null;
	}
	
	
	@Test
	public void failing_directMove(){
		// EMPTY SOURCE
		byte source = 6;
		byte dest = 4;
		boolean outcomeB = board.moveBlack(source, dest);
		boolean outcomeW = board.moveWhite(source, dest);
		assertFalse(outcomeB);
		assertFalse(outcomeW);
		assertTrue(board.isFreeCell(source));
		assertTrue(board.isWhiteCell(dest));
		
		// NON-FREE DESTINATION
		source = 4;
		dest = 14;
		boolean outcome = board.moveWhite(source, dest);
		assertFalse(outcome);
		assertFalse(board.isFreeCell(dest));
	}
	
	
	
	@Test
	public void failing_capture(){
		byte source = 4;
		byte[] dest = {6, 20, 27, 32};
		
		// NON-ADVERSAY STARTING
		boolean outcome = board.captureByWhite(source, dest);
		assertFalse(outcome);
		assertTrue(board.isFreeCell(dest[0]));
		assertTrue(board.isWhiteCell(source));
		for(int i=1; i<dest.length; i++)
			assertTrue(board.isBlackCell(dest[i]));
		
		// NON-ADVERSARY INTERLEAVING
		dest[0] = 8;
		dest[2] = 18;
		outcome = board.captureByWhite(source, dest);
		assertFalse(outcome);
		assertTrue(board.isBlackCell(dest[0]));
		assertTrue(board.isBlackCell(dest[1]));
		assertTrue(board.isFreeCell(dest[2]));
		assertTrue(board.isBlackCell(dest[3]));
						
		// NON-ADVERSARY ENDING
		dest[2] = 27;
		dest[3] = 34;
		outcome = board.captureByWhite(source, dest);
		assertFalse(outcome);
		for(int i=0; i<dest.length-1; i++)
			assertTrue(board.isBlackCell(dest[i]));
		assertTrue(board.isFreeCell(dest[3]));
	}
}
