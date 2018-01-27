package core;

import interfaces.GlobalValues;

public class QuietNodeChecker {

	public static boolean isBlackWinning(BoardConfiguration bc) {
		byte numBlacks = bc.getNumberOfBlacks();
		byte numWhites = bc.getNumberOfWhites();
		boolean turn = bc.getTurn();
		
		if(numWhites==1 && numBlacks==1 && turn==GlobalValues.BLACK_TURN)
			return true;
		
		/*
		 * Se ci sono due pedine nere e una bianca:
		 * - se il turno è nero e sono distanti di 2 il bianco è spaccianto
		 * - se il turno è bianco e il bianco non riesce a catturare entrambe le nere, il bianco è spacciato
		 */
		
		if(numBlacks==2 && numWhites==1){
			boolean[] blacks = bc.getBlackPositions();
			boolean[] whites = bc.getWhitePositions();
			byte firstBlack = -1, secondBlack = -1, white = -1;
			for(byte cell=0; cell<GlobalValues.CELLS_NUMBER; cell++){
				if(blacks[cell]==true){
					if(firstBlack==-1)
						firstBlack = cell;
					else
						secondBlack = cell;
				}
				if(whites[cell]==true)
					white = cell;
			}		
			if(turn==GlobalValues.BLACK_TURN && 
					(BoardReachings.getDistanceBetween(firstBlack, white)<=5 ||
					 BoardReachings.getDistanceBetween(secondBlack, white)<=5 ))
				return true;
			if(turn==GlobalValues.WHITE_TURN &&
				BoardReachings.getDistanceBetween(white, firstBlack)+BoardReachings.getDistanceBetween(firstBlack, secondBlack) > 6 &&
				BoardReachings.getDistanceBetween(white, secondBlack)+BoardReachings.getDistanceBetween(secondBlack, firstBlack) > 6){
				return true;
			}
		}		
		
		/*
		 * Se è il turno nero e può mangiare entrambe le bianche, il bianco è spacciato
		 */
		if(numBlacks==1 && numWhites==2 && turn==GlobalValues.BLACK_TURN){
			boolean[] blacks = bc.getBlackPositions();
			boolean[] whites = bc.getWhitePositions();
			byte firstWhite = -1, secondWhite = -1, black = -1;
			for(byte cell=0; cell<GlobalValues.CELLS_NUMBER; cell++){
				if(whites[cell]==true){
					if(firstWhite==-1)
						firstWhite = cell;
					else
						secondWhite = cell;
				}
				if(blacks[cell]==true)
					black = cell;
			}		
			if(BoardReachings.getDistanceBetween(black, firstWhite)+BoardReachings.getDistanceBetween(firstWhite, secondWhite) <= 6 ||
					BoardReachings.getDistanceBetween(black, secondWhite)+BoardReachings.getDistanceBetween(secondWhite, firstWhite) <= 6 )
				return true;
		}		
		
		/*
		 * Se siamo 2 contro 2 ed il turno è nero.
		 */
		if(numBlacks==2 && numWhites==2 && turn==GlobalValues.BLACK_TURN){
				boolean[] blacks = bc.getBlackPositions();
				boolean[] whites = bc.getWhitePositions();
				byte white1 = -1, white2 = -1, black1 = -1, black2 = -1;
				for(byte cell=0; cell<GlobalValues.CELLS_NUMBER; cell++){
					if(whites[cell]==true){
						if(white1==-1)
							white1 = cell;
						else if(white2 ==-1)
							white2 = cell;
					}
					if(blacks[cell]==true){
						if(black1==-1)
							black1 = cell;
						else
							black2 = cell;
					}
				}
				
				// calcolo delle distance
				byte distanceBlack1Black2 = BoardReachings.getDistanceBetween(black1, black2);
				byte distanceWhite1White2 = BoardReachings.getDistanceBetween(white1, white2);
				byte distanceWhite1Black1 = BoardReachings.getDistanceBetween(white1, black1);
				byte distanceWhite1Black2 = BoardReachings.getDistanceBetween(white1, black2);
				byte distanceWhite2Black1 = BoardReachings.getDistanceBetween(white2, black1);
				byte distanceWhite2Black2 = BoardReachings.getDistanceBetween(white2, black2);
				
				// verifica: se le mangia tutte il nero vince.
				if(distanceWhite2Black1+distanceWhite1White2 <= 5) return true;
				if(distanceWhite1Black1+distanceWhite1White2 <= 5) return true;
				if(distanceWhite2Black2+distanceWhite1White2 <= 5) return true;
				if(distanceWhite1Black2+distanceWhite1White2 <= 5) return true;
				
				// verifica: se incastra il bianco, il nero vince.
				if(distanceWhite1White2+distanceWhite1Black2 > 6 && distanceWhite2Black2+distanceWhite1Black2 > 6) return true;
				if(distanceWhite1White2+distanceWhite2Black2 > 6 && distanceWhite1Black2+distanceWhite2Black2 > 6) return true;
				if(distanceWhite1White2+distanceWhite1Black1 > 6 && distanceWhite2Black1+distanceWhite1Black1 > 6) return true;
				if(distanceWhite1White2+distanceWhite2Black1 > 6 && distanceWhite1Black1+distanceWhite2Black1 > 6) return true;
		}
		
		/*
		 * 3 contro 1 per il nero 
		 */
		if(numBlacks==3 && numWhites==1 && turn==GlobalValues.BLACK_TURN){
			boolean[] blacks = bc.getBlackPositions();
			boolean[] whites = bc.getWhitePositions();
			byte black1 = -1, black2 = -1, black3 = -1, white = -1;
			for(byte cell=0; cell<GlobalValues.CELLS_NUMBER; cell++){
				if(blacks[cell]==true){
					if(black1==-1)
						black1 = cell;
					else if(black2 ==-1)
						black2 = cell;
					else
						black3 = cell;
				}
				if(whites[cell]==true)
					white = cell;
			}
			if(BoardReachings.getDistanceBetween(black1, white)<=4 ||
					BoardReachings.getDistanceBetween(black2, white)<=4 ||
					BoardReachings.getDistanceBetween(black3, white)<=4)
				return true;
		}
		
		/*
		 * 3 contro 1 per il bianco 
		 */
		if(numBlacks==1 && numWhites==3 && turn==GlobalValues.BLACK_TURN){
			boolean[] blacks = bc.getBlackPositions();
			boolean[] whites = bc.getWhitePositions();
			byte black = -1;
			byte white1 = -1, white2 = -1, white3 = -1;
			for(byte cell=0; cell<GlobalValues.CELLS_NUMBER; cell++)
				if(blacks[cell]==true)
					black = cell;
			for(byte cell=0; cell<GlobalValues.CELLS_NUMBER; cell++){
				if(whites[cell]==true){
					if(white1==-1)
						white1 = cell;
					else if(white2==-1)
						white2 = cell;
					else
						white3 = cell;
				}
			}
			// Se le mangia tutte ... vanno considerati i 3! = 6 ordini diversi
			byte distanceBlackWhite1 = BoardReachings.getDistanceBetween(black, white1);
			byte distanceBlackWhite2 = BoardReachings.getDistanceBetween(black, white2);
			byte distanceBlackWhite3 = BoardReachings.getDistanceBetween(black, white3);
			byte distanceWhite1White2 = BoardReachings.getDistanceBetween(white1, white2);
			byte distanceWhite2White3 = BoardReachings.getDistanceBetween(white2, white3);
			byte distanceWhite1White3 = BoardReachings.getDistanceBetween(white1, white3);
			if(distanceBlackWhite1 + distanceWhite1White2 + distanceWhite2White3 <= 6)
				return true;
			if(distanceBlackWhite1 + distanceWhite1White3 + distanceWhite2White3 <= 6)
				return true;
			if(distanceBlackWhite2 + distanceWhite1White2 + distanceWhite1White3 <= 6)
				return true;
			if(distanceBlackWhite2 + distanceWhite2White3 + distanceWhite1White3 <= 6)
				return true;
			if(distanceBlackWhite3 + distanceWhite1White3 + distanceWhite1White2 <= 6)
				return true;
			if(distanceBlackWhite3 + distanceWhite2White3 + distanceWhite1White2 <= 6)
				return true;
		}
		
		/*
		 * 4 contro 1 per il nero 
		 */
		if(numBlacks==4 && numWhites==1 && turn==GlobalValues.BLACK_TURN){
			boolean[] blacks = bc.getBlackPositions();
			boolean[] whites = bc.getWhitePositions();
			byte black1 = -1, black2 = -1, black3 = -1, black4 = -1, white = -1;
			for(byte cell=0; cell<GlobalValues.CELLS_NUMBER; cell++){
				if(blacks[cell]==true){
					if(black1==-1)
						black1 = cell;
					else if(black2 ==-1)
						black2 = cell;
					else if(black3==-1)
						black3 = cell;
					else
						black4 = cell;
				}
				if(whites[cell]==true)
					white = cell;
			}
			if(BoardReachings.getDistanceBetween(black1, white)<=3 ||
					BoardReachings.getDistanceBetween(black2, white)<=3 ||
					BoardReachings.getDistanceBetween(black3, white)<=3 ||
					BoardReachings.getDistanceBetween(black4, white)<=3)
				return true;
		}
		
		/*
		 * 3 contro 2 per il bianco
		 *
		if(numWhites==3 && numBlacks==2 && turn==GlobalValues.BLACK_TURN){
			boolean[] blacks = bc.getBlackPositions();
			boolean[] whites = bc.getWhitePositions();
			byte w1 = -1, w2 = -1, w3 = -1, b1 = -1, b2 = -1;
			for(byte cell=0; cell<GlobalValues.CELLS_NUMBER; cell++){
				if(whites[cell]==true){
					if(w1==-1)
						w1 = cell;
					else if(w2 ==-1)
						w2 = cell;
					else 
						w3 = cell;
				}
				if(blacks[cell]==true){
					if(b1==-1)
						b1 = cell;
					else 
						b2 = cell;
				}		
			}
			byte distanceB1W1 = BoardReachings.getDistanceBetween(b1, w1);
			byte distanceB1W2 = BoardReachings.getDistanceBetween(b1, w2);
			byte distanceB1W3 = BoardReachings.getDistanceBetween(b1, w3);
			
			byte distanceB2W1 = BoardReachings.getDistanceBetween(b2, w1);
			byte distanceB2W2 = BoardReachings.getDistanceBetween(b2, w2);
			byte distanceB2W3 = BoardReachings.getDistanceBetween(b2, w3);
			
			byte distanceB1B2 = BoardReachings.getDistanceBetween(b1, b2);
			
			byte distanceW1W2 = BoardReachings.getDistanceBetween(w1, w2);
			byte distanceW1W3 = BoardReachings.getDistanceBetween(w1, w3);
			byte distanceW2W3 = BoardReachings.getDistanceBetween(w2, w3);
			
			// se il nero mangia le tre bianche vince
			if(distanceB1W1 + distanceW1W2 + distanceW2W3 <= 5) return true;
			if(distanceB1W1 + distanceW1W3 + distanceW2W3 <= 5) return true;
			if(distanceB1W2 + distanceW1W2 + distanceW1W3 <= 5) return true;
			if(distanceB1W2 + distanceW2W3 + distanceW1W3 <= 5) return true;
			if(distanceB1W3 + distanceW1W3 + distanceW1W2 <= 5) return true;
			if(distanceB1W3 + distanceW2W3 + distanceW1W2 <= 5) return true;
			
			if(distanceB2W1 + distanceW1W2 + distanceW2W3 <= 5) return true;
			if(distanceB2W1 + distanceW1W3 + distanceW2W3 <= 5) return true;
			if(distanceB2W2 + distanceW1W2 + distanceW1W3 <= 5) return true;
			if(distanceB2W2 + distanceW2W3 + distanceW1W3 <= 5) return true;
			if(distanceB2W3 + distanceW1W3 + distanceW1W2 <= 5) return true;
			if(distanceB2W3 + distanceW2W3 + distanceW1W2 <= 5) return true;
			
			// se il nero mangia 2 bianche - il bianco ribatte?
			if(distanceB1W1 + distanceW1W2 <= 5 && distanceB2W3 + distanceB2W2 > 6 && distanceW2W3 + distanceB2W2 > 6) return true;
			if(distanceB1W1 + distanceW1W3 <= 5 && distanceW2W3 + distanceB2W3 > 6 && distanceB2W2 + distanceB2W3 > 6) return true;
			if(distanceB1W2 + distanceW1W2 <= 5 && distanceW1W3 + distanceB2W1 > 6 && distanceB2W3 + distanceB2W1 > 6) return true;
			if(distanceB1W2 + distanceW2W3 <= 5 && distanceW1W3 + distanceB2W3 > 6 && distanceB2W1 + distanceB2W3 > 6) return true;
			if(distanceB1W3 + distanceW1W3 <= 5 && distanceW1W2 + distanceB2W1 > 6 && distanceB2W2 + distanceB2W1 > 6) return true;
			if(distanceB1W3 + distanceW2W3 <= 5 && distanceW1W2 + distanceB2W2 > 6 && distanceB2W1 + distanceB2W2 > 6) return true;
			
			if(distanceB2W1 + distanceW1W2 <= 5 && distanceW2W3 + distanceB1W2 > 6 && distanceB1W3 + distanceB1W2 > 6) return true;
			if(distanceB2W1 + distanceW1W3 <= 5 && distanceB1W2 + distanceB1W3 > 6 && distanceW2W3 + distanceB1W3 > 6) return true;
			if(distanceB2W2 + distanceW1W2 <= 5 && distanceW1W3 + distanceB1W1 > 6 && distanceB1W3 + distanceW1W3 > 6) return true;
			if(distanceB2W2 + distanceW2W3 <= 5 && distanceW1W3 + distanceB1W3 > 6 && distanceB1W1 + distanceB1W3 > 6) return true;
			if(distanceB2W3 + distanceW1W3 <= 5 && distanceW1W2 + distanceB1W1 > 6 && distanceB1W2 + distanceB1W1 > 6) return true;
			if(distanceB2W3 + distanceW2W3 <= 5 && distanceW1W2 + distanceB1W2 > 6 && distanceB1W1 + distanceB1W2 > 6) return true;
			
		}
		*/
		return false;
	}
	
	
	public static boolean isWhiteWinning(BoardConfiguration bc) {
		byte numBlacks = bc.getNumberOfBlacks();
		byte numWhites = bc.getNumberOfWhites();
		boolean turn = bc.getTurn();

		if(numWhites==1 && numBlacks==1 && turn==GlobalValues.WHITE_TURN)
			return true;

		/*
		 * Se ci sono due pedine bianche e una nera:
		 * - se il turno è bianco e sono distanti di 2 il nero è spacciato sicuro.
		 * - se il turno è nero e il nero non riesce a catturare entrambe le bianche il nero è spacciato
		 */
		
		if(numWhites==2 && numBlacks==1){
			boolean[] whites = bc.getWhitePositions();
			boolean[] blacks = bc.getBlackPositions();
			byte firstWhite = -1, secondWhite = -1, black = -1;
			for(byte cell=0; cell<GlobalValues.CELLS_NUMBER; cell++){
				if(whites[cell]==true){
					if(firstWhite==-1)
						firstWhite = cell;
					else
						secondWhite = cell;
				}
				if(blacks[cell]==true)
					black = cell;
			}		
			
			// è il turno del bianco, la distanza deve essere minore o uguale a 5
			if(turn==GlobalValues.WHITE_TURN && 
			   (BoardReachings.getDistanceBetween(firstWhite, black)<=5 ||
				BoardReachings.getDistanceBetween(secondWhite, black)<=5))
				return true;
			// è turno del nero ma riesce a mangiarne una sola
			if(turn==GlobalValues.BLACK_TURN &&
				BoardReachings.getDistanceBetween(black, firstWhite)+BoardReachings.getDistanceBetween(firstWhite, secondWhite) > 6 &&
				BoardReachings.getDistanceBetween(black, secondWhite)+BoardReachings.getDistanceBetween(secondWhite, firstWhite) > 6){
				return true;
			}
		}
		if(numWhites==1 && numBlacks==2 && turn==GlobalValues.WHITE_TURN){
			boolean[] blacks = bc.getBlackPositions();
			boolean[] whites = bc.getWhitePositions();
			byte firstBlack = -1, secondBlack = -1, white = -1;
			for(byte cell=0; cell<GlobalValues.CELLS_NUMBER; cell++){
				if(blacks[cell]==true){
					if(firstBlack==-1)
						firstBlack = cell;
					else
						secondBlack = cell;
				}
				if(whites[cell]==true)
					white = cell;
			}		
			if(BoardReachings.getDistanceBetween(white, firstBlack)+BoardReachings.getDistanceBetween(firstBlack, secondBlack) <= 6 ||
					BoardReachings.getDistanceBetween(white, secondBlack)+BoardReachings.getDistanceBetween(secondBlack, firstBlack) <= 6 )
				return true;
		}
		
		/*
		 * Se siamo 2 contro 2 ed il turno è bianco.
		 */
		if(numWhites==2 && numBlacks==2 && turn==GlobalValues.WHITE_TURN){
			boolean[] blacks = bc.getBlackPositions();
			boolean[] whites = bc.getWhitePositions();
			byte white1 = -1, white2 = -1, black1 = -1, black2 = -1;
			for(byte cell=0; cell<GlobalValues.CELLS_NUMBER; cell++){
				if(whites[cell]==true){
					if(white1==-1)
						white1 = cell;
					else if(white2 ==-1)
						white2 = cell;
				}
				if(blacks[cell]==true){
					if(black1==-1)
						black1 = cell;
					else
						black2 = cell;
				}
			}
			
			// calcolo delle distance
			byte distanceBlack1Black2 = BoardReachings.getDistanceBetween(black1, black2);
			byte distanceWhite1White2 = BoardReachings.getDistanceBetween(white1, white2);
			byte distanceWhite1Black1 = BoardReachings.getDistanceBetween(white1, black1);
			byte distanceWhite1Black2 = BoardReachings.getDistanceBetween(white1, black2);
			byte distanceWhite2Black1 = BoardReachings.getDistanceBetween(white2, black1);
			byte distanceWhite2Black2 = BoardReachings.getDistanceBetween(white2, black2);
			
			// verifica condizioni: se le mangio tutte e' vittoria
			if(distanceWhite1Black1+distanceBlack1Black2 <= 5) return true;
			if(distanceWhite1Black2+distanceBlack1Black2 <= 5) return true;
			if(distanceWhite2Black1+distanceBlack1Black2 <= 5) return true;
			if(distanceWhite2Black2+distanceBlack1Black2 <= 5) return true;
			
			// verifica se incastro l'avversario vinco
			if(distanceBlack1Black2+distanceWhite2Black1 > 6 && distanceWhite2Black2+distanceWhite2Black1 > 6) return true;
			if(distanceBlack1Black2+distanceWhite2Black2 > 6 && distanceWhite2Black1+distanceWhite2Black2 > 6) return true;
			if(distanceBlack1Black2+distanceWhite1Black1 > 6 && distanceWhite1Black2+distanceWhite1Black1 > 6) return true;
			if(distanceBlack1Black2+distanceWhite1Black2 > 6 && distanceWhite1Black1+distanceWhite1Black2 > 6) return true;
		}
		
		
		/*
		 * 3 contro 1 per il bianco
		 */
		if(numWhites==3 && numBlacks==1 && turn==GlobalValues.WHITE_TURN){
			boolean[] blacks = bc.getBlackPositions();
			boolean[] whites = bc.getWhitePositions();
			byte white1 = -1, white2 = -1, white3 = -1, black = -1;
			for(byte cell=0; cell<GlobalValues.CELLS_NUMBER; cell++){
				if(whites[cell]==true){
					if(white1==-1)
						white1 = cell;
					else if(white2 ==-1)
						white2 = cell;
					else
						white3 = cell;
				}
				if(blacks[cell]==true)
					black = cell;
			}
			if(BoardReachings.getDistanceBetween(white1, black)<=4 ||
					BoardReachings.getDistanceBetween(white2, black)<=4 ||
					BoardReachings.getDistanceBetween(white3, black)<=4)
				return true;
		}
		
		/*
		 * 3 contro 1 per il nero
		 */
		if(numWhites==1 && numBlacks==3 && turn==GlobalValues.WHITE_TURN){
			boolean[] blacks = bc.getBlackPositions();
			boolean[] whites = bc.getWhitePositions();
			byte white = -1;
			byte black1 = -1, black2 = -1, black3 = -1;
			for(byte cell=0; cell<GlobalValues.CELLS_NUMBER; cell++)
				if(whites[cell]==true)
					white = cell;
			for(byte cell=0; cell<GlobalValues.CELLS_NUMBER; cell++){
				if(blacks[cell]==true){
					if(black1==-1)
						black1 = cell;
					else if(black2==-1)
						black2 = cell;
					else
						black3 = cell;
				}
			}
			// Se le mangia tutte ... vanno considerati i 3!=6 ordini diversi
			byte distanceWhiteBlack1 = BoardReachings.getDistanceBetween(white, black1);
			byte distanceWhiteBlack2 = BoardReachings.getDistanceBetween(white, black2);
			byte distanceWhiteBlack3 = BoardReachings.getDistanceBetween(white, black3);
			byte distanceBlack1Black2 = BoardReachings.getDistanceBetween(black1, black2);
			byte distanceBlack2Black3 = BoardReachings.getDistanceBetween(black2, black3);
			byte distanceBlack1Black3 = BoardReachings.getDistanceBetween(black1, black3);
			if(distanceWhiteBlack1 + distanceBlack1Black2 + distanceBlack2Black3 <= 6)
				return true;
			if(distanceWhiteBlack1 + distanceBlack1Black3 + distanceBlack2Black3 <= 6)
				return true;
			if(distanceWhiteBlack2 + distanceBlack1Black2 + distanceBlack1Black3 <= 6)
				return true;
			if(distanceWhiteBlack2 + distanceBlack2Black3 + distanceBlack1Black3 <= 6)
				return true;
			if(distanceWhiteBlack3 + distanceBlack1Black3 + distanceBlack1Black2 <= 6)
				return true;
			if(distanceWhiteBlack3 + distanceBlack2Black3 + distanceBlack1Black2 <= 6)
				return true;
		}
		
		/*
		 * 4 contro 1 per il bianco
		 */
		if(numWhites==4 && numBlacks==1 && turn==GlobalValues.WHITE_TURN){
			boolean[] blacks = bc.getBlackPositions();
			boolean[] whites = bc.getWhitePositions();
			byte white1 = -1, white2 = -1, white3 = -1, white4 = -1, black = -1;
			for(byte cell=0; cell<GlobalValues.CELLS_NUMBER; cell++){
				if(whites[cell]==true){
					if(white1==-1)
						white1 = cell;
					else if(white2 ==-1)
						white2 = cell;
					else if(white3==-1)
						white3 = cell;
					else
						white4 = cell;
				}
				if(blacks[cell]==true)
					black = cell;
			}
			if(BoardReachings.getDistanceBetween(white1, black)<=3 ||
					BoardReachings.getDistanceBetween(white2, black)<=3 ||
					BoardReachings.getDistanceBetween(white3, black)<=3 ||
					BoardReachings.getDistanceBetween(white4, black)<=3)
				return true;
		}
		
		/*
		 * 3 contro 2 per il nero
		 *
		if(numBlacks==3 && numWhites==2 && turn==GlobalValues.WHITE_TURN){
			boolean[] blacks = bc.getBlackPositions();
			boolean[] whites = bc.getWhitePositions();
			byte black1 = -1, black2 = -1, black3 = -1, white1 = -1, white2 = -1;
			for(byte cell=0; cell<GlobalValues.CELLS_NUMBER; cell++){
				if(blacks[cell]==true){
					if(black1==-1)
						black1 = cell;
					else if(black2 ==-1)
						black2 = cell;
					else 
						black3 = cell;
				}
				if(whites[cell]==true){
					if(white1==-1)
						white1 = cell;
					else 
						white2 = cell;
				}		
			}
			byte distanceW1B1 = BoardReachings.getDistanceBetween(white1, black1);
			byte distanceW1B2 = BoardReachings.getDistanceBetween(white1, black2);
			byte distanceW1B3 = BoardReachings.getDistanceBetween(white1, black3);
			
			byte distanceW2B1 = BoardReachings.getDistanceBetween(white2, black1);
			byte distanceW2B2 = BoardReachings.getDistanceBetween(white2, black2);
			byte distanceW2B3 = BoardReachings.getDistanceBetween(white2, black3);
			
			byte distanceW1W2 = BoardReachings.getDistanceBetween(white1, white2);
			
			byte distanceB1B2 = BoardReachings.getDistanceBetween(black1, black2);
			byte distanceB1B3 = BoardReachings.getDistanceBetween(black1, black3);
			byte distanceB2B3 = BoardReachings.getDistanceBetween(black2, black3);
			
			// se il bianco mangia le tre nere vince
			if(distanceW1B1 + distanceB1B2 + distanceB2B3 <= 5) return true;
			if(distanceW1B1 + distanceB1B3 + distanceB2B3 <= 5) return true;
			if(distanceW1B2 + distanceB1B2 + distanceB1B3 <= 5) return true;
			if(distanceW1B2 + distanceB2B3 + distanceB1B3 <= 5) return true;
			if(distanceW1B3 + distanceB1B3 + distanceB1B2 <= 5) return true;
			if(distanceW1B3 + distanceB2B3 + distanceB1B2 <= 5) return true;
			
			if(distanceW2B1 + distanceB1B2 + distanceB2B3 <= 5) return true;
			if(distanceW2B1 + distanceB1B3 + distanceB2B3 <= 5) return true;
			if(distanceW2B2 + distanceB1B2 + distanceB1B3 <= 5) return true;
			if(distanceW2B2 + distanceB2B3 + distanceB1B3 <= 5) return true;
			if(distanceW2B3 + distanceB1B3 + distanceB1B2 <= 5) return true;
			if(distanceW2B3 + distanceB2B3 + distanceB1B2 <= 5) return true;
			
			// se il bianco mangia 2 nere - il nero ribatte?
			if(distanceW1B1 + distanceB1B2 <= 5 && distanceW2B3 + distanceW2B2 > 6 && distanceB2B3 + distanceW2B2 > 6) return true;
			if(distanceW1B1 + distanceB1B3 <= 5 && distanceB2B3 + distanceW2B3 > 6 && distanceW2B2 + distanceW2B3 > 6) return true;
			if(distanceW1B2 + distanceB1B2 <= 5 && distanceB1B3 + distanceW2B1 > 6 && distanceW2B3 + distanceW2B1 > 6) return true;
			if(distanceW1B2 + distanceB2B3 <= 5 && distanceB1B3 + distanceW2B3 > 6 && distanceW2B1 + distanceW2B3 > 6) return true;
			if(distanceW1B3 + distanceB1B3 <= 5 && distanceB1B2 + distanceW2B1 > 6 && distanceW2B2 + distanceW2B1 > 6) return true;
			if(distanceW1B3 + distanceB2B3 <= 5 && distanceB1B2 + distanceW2B2 > 6 && distanceW2B1 + distanceW2B2 > 6) return true;
			
			if(distanceW2B1 + distanceB1B2 <= 5 && distanceB2B3 + distanceW1B2 > 6 && distanceW1B3 + distanceW1B2 > 6) return true;
			if(distanceW2B1 + distanceB1B3 <= 5 && distanceW1B2 + distanceW1B3 > 6 && distanceB2B3 + distanceW1B3 > 6) return true;
			if(distanceW2B2 + distanceB1B2 <= 5 && distanceB1B3 + distanceW1B1 > 6 && distanceW1B3 + distanceB1B3 > 6) return true;
			if(distanceW2B2 + distanceB2B3 <= 5 && distanceB1B3 + distanceW1B3 > 6 && distanceW1B1 + distanceW1B3 > 6) return true;
			if(distanceW2B3 + distanceB1B3 <= 5 && distanceB1B2 + distanceW1B1 > 6 && distanceW1B2 + distanceW1B1 > 6) return true;
			if(distanceW2B3 + distanceB2B3 <= 5 && distanceB1B2 + distanceW1B2 > 6 && distanceW1B1 + distanceW1B2 > 6) return true;	
		}
		*/
		
		return false;
	}
	
}
