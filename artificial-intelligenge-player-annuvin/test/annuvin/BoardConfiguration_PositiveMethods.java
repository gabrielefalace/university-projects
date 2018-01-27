package annuvin;


import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.BoardConfiguration;

public class BoardConfiguration_PositiveMethods {

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
	public void correct_DirectMoveTest(){
		// WHITE
		byte source = 4;
		byte dest = 5;
		boolean outcome = board.moveWhite(source, dest);
		assertTrue(outcome);
		assertTrue(board.isFreeCell(source));
		assertTrue(board.isWhiteCell(dest));
		
		// BLACK
		source = 14;
		dest = 13;
		outcome = board.moveBlack(source, dest);
		assertTrue(outcome);
		assertTrue(board.isBlackCell(dest));
		assertTrue(board.isFreeCell(source));
	}
	
	
	
	
	@Test
	public void correct_captureTest(){
		// WHITE
		byte source = 4;
		byte dest1 = 14, dest2 = 20, dest3 = 32;
		boolean outcome = board.captureByWhite(source, dest1, dest2, dest3);
		assertTrue(outcome);
		assertTrue(board.isFreeCell(source));
		assertTrue(board.isFreeCell(dest1));
		assertTrue(board.isFreeCell(dest2));
		assertTrue(board.isWhiteCell(dest3));
		assertEquals(3, board.getNumberOfBlacks());
		
		board = new BoardConfiguration();
		
		// BLACK
		source = 8;
		dest1 = 4; dest2 = 16; dest3 = 28;
		outcome = board.captureByBlack(source, dest1, dest2, dest3);
		assertTrue(outcome);
		assertTrue(board.isFreeCell(source));
		assertTrue(board.isFreeCell(dest1));
		assertTrue(board.isFreeCell(dest2));
		assertTrue(board.isBlackCell(dest3));
		assertEquals(3, board.getNumberOfWhites());
	}
	
	@Test
	public void correct_AllCapturedTest(){
		// WHITE
		byte source = 4;
		byte dest1 = 8, dest2 = 14, dest3 = 20, dest4 = 21, dest5 = 27, dest6 = 32;
		boolean outcome = board.captureByWhite(source, dest1, dest2, dest3, dest4, dest5, dest6);
		assertTrue(outcome);
		assertTrue(board.isFreeCell(source));
		assertTrue(board.isFreeCell(dest1));
		assertTrue(board.isFreeCell(dest2));
		assertTrue(board.isFreeCell(dest3));
		assertTrue(board.isFreeCell(dest4));
		assertTrue(board.isFreeCell(dest5));
		assertTrue(board.isWhiteCell(dest6));
		assertEquals(0, board.getNumberOfBlacks());
		
		board = new BoardConfiguration();
		
		// BLACK
		source = 8;
		dest1 = 4;
		dest2 = 9;
		dest3 = 15;
		dest4 = 16;
		dest5 = 22;
		dest6 = 28;
		outcome = board.captureByBlack(source, dest1, dest2, dest3, dest4, dest5, dest6);
		assertTrue(outcome);
		assertTrue(board.isFreeCell(source));
		assertTrue(board.isFreeCell(dest1));
		assertTrue(board.isFreeCell(dest2));
		assertTrue(board.isFreeCell(dest3));
		assertTrue(board.isFreeCell(dest4));
		assertTrue(board.isFreeCell(dest5));
		assertTrue(board.isBlackCell(dest6));
		assertEquals(0, board.getNumberOfWhites());
	}

}
