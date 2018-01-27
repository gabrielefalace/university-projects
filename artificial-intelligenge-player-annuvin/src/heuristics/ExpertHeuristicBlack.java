package heuristics;

import java.util.LinkedList;

import core.BlackConfigurationFactory;
import core.BoardConfiguration;
import core.BoardReachings;
import core.Capture;
import core.QuietNodeChecker;
import core.WhiteConfigurationFactory;
import interfaces.GlobalValues;
import interfaces.Heuristic;

public class ExpertHeuristicBlack implements Heuristic {

	@Override
	public final byte evaluate(BoardConfiguration bc) {
		
		// Vittoria e sconfitta esplicita
		if(bc.isWhiteWinningConfiguration())
			return -10;
		else if(bc.isBlackWinningConfiguration())
			return 120;
		
		if(bc.getNumberOfBlacks()+bc.getNumberOfWhites() <= 4){
			if(QuietNodeChecker.isWhiteWinning(bc))
				return -5;
			else if(QuietNodeChecker.isBlackWinning(bc))
				return 110;
		}
		
		byte rule1 = evaluateDistanceInBlacks(bc);
		byte rule2 = evaluateBlackWhiteDifference(bc);
//		byte rule3 = evalutateDistanceInWhites(bc);
//		byte rule4;
//		if(bc.getTurn()==GlobalValues.WHITE_TURN)
//			rule4 = evaluateOffensiveReachability(bc);
//		else
//			rule4 = evaluateDefensiveReachability(bc);
		
		return (byte)(rule1 + rule2);
	}

	
	private final byte evaluateDistanceInBlacks(BoardConfiguration bc){
		byte meanValue = 0;
		byte varianceValue = 0;
		byte blackNumber = bc.getNumberOfBlacks();
		if(blackNumber==1)
			return 0;		// o return 5, per essere imparziale?
		
		byte[][] distancesInBlacks = WhiteConfigurationFactory.getDistancesInBlacks(bc);
		byte averageDistance = 0;
		byte totalDistance = 0;
		byte numberOfPairs = 0;
		//Calcolo distanza media
		for(byte i=0; i<37; i++){
			for(byte j=(byte)(i+1); j<37; j++){
				if(distancesInBlacks[i][j] != 10){
					totalDistance += distancesInBlacks[i][j];
					numberOfPairs++;
				}
			}
		}
		
		averageDistance = (byte)(totalDistance/numberOfPairs);
		// Scoring per la distanza media. I valori sono compresi tra 0 e 11.
		switch(averageDistance){
		case 6: meanValue = 45;	//35;		
		case 5: meanValue = 40;	//26;		
		case 4: meanValue = 35;	//21;		
		case 3: meanValue = 30;	//20;		
		case 2: meanValue = 25;	//15;
		default: meanValue = 1;
		}
		
		/*
		//Calcolo varianza 
		byte summary = 0;
		byte variance = 0; 
		for(byte pi=0; pi<37; pi++){
			for(byte pj=(byte)(pi+1); pj<37; pj++){
				if(distancesInBlacks[pi][pj] != 10){
					summary += Math.abs(averageDistance-distancesInBlacks[pi][pj]);
				}
			}
		}
		
		variance = (byte)(summary/numberOfPairs);
		
		// Scoring per la varianza. I valori sono compresi tra 0 e 14.
		switch(variance){
		case 0: varianceValue = 4;
		case 1: varianceValue = 2;
		case 2: varianceValue = 1;
		case 3: varianceValue = 0;
		case 4: varianceValue = 0;
		case 5: varianceValue = 0;
		default: varianceValue = 0;
		}
		return (byte)(meanValue+varianceValue);
		*/
		return meanValue;
	}
	
	private final byte evaluateBlackWhiteDifference(BoardConfiguration bc){
		byte numberOfWhite = bc.getNumberOfWhites();
		byte numberOfBlack = bc.getNumberOfBlacks();
		byte difference = (byte)(numberOfBlack - numberOfWhite);
		
		// Scoring
		switch(difference){
		case 4: return 38;//1; //25;
		case 3: return 32;//20;//12; //23;
		case 2: return 26;//14; //15;
		case 1: return 18;//12; //12;
		case 0: return 10;
		case -1: return 3;
		case -2: return 1;
		case -3: return 0;
		case -4: return 0;
		default: return 0;
		}
	}

	@Deprecated
	private final byte evalutateDistanceInWhites(BoardConfiguration bc){
		byte whiteNumber = bc.getNumberOfWhites();
		if(whiteNumber==1)
			return 2;		// una sola pedina è molto raggruppata cioè vulnerabile, perciò il massimo
							// ... o return 5, per essere imparziale?
		
		byte[][] distancesInWhites = BlackConfigurationFactory.getDistancesInWhites(bc);
		byte averageDistance = 0;
		byte totalDistance = 0;
		byte numberOfPairs = 0;
		
		for(byte i=0; i<37; i++){
			for(byte j=(byte)(i+1); j<37; j++){
				if(distancesInWhites[i][j] != 10){
					totalDistance += distancesInWhites[i][j];
					numberOfPairs++;
				}
			}
		}
		
		averageDistance = (byte)(totalDistance/numberOfPairs);
		
		// Scoring
		switch(averageDistance){
		case 6: return 0;
		case 5: return 2;
		case 4: return 4;
		case 3: return 6;
		case 2: return 8;
		default: return 10;
		}
	}
	
	@Deprecated
	private final byte evaluateOffensiveReachability(BoardConfiguration bc){
		byte totalReachables = 0;
		byte numberOfWhites = bc.getNumberOfWhites();
		
		boolean[] blackPositions = bc.getBlackPositions();
		boolean[] whitePositions = bc.getWhitePositions();
		
		byte blackM = bc.getBlackM();
		
		/*
		 * Conteggio delle pedine bianche sotto attacco. Per ogni pedina bianca
		 * si scorrono le nere e se si trova una nera che la raggiunge,
		 * si incrementa il contatore e si passa alla bianca successiva (perciò il "break loop").
		 */
		for(byte white=0; white<GlobalValues.CELLS_NUMBER; white++){
			if(whitePositions[white]==true){
				loop: for(byte black=0; black<GlobalValues.CELLS_NUMBER; black++){
					if(blackPositions[black]==true && BoardReachings.isReachable(black, white, blackM)){
						totalReachables++;
						break loop;
					}
				}
			}
		}
		
		byte percentage = (byte)(Math.round(totalReachables/numberOfWhites)*10);
		
		// Scoring
		switch(percentage){
			case 10: return 15;
			case 9: return 6;
			case 8: return 5;
			case 7: return 5;
			case 6: return 5;
			case 5: return 5;
			case 4: return 5;
			case 3: return 4;
			case 2: return 3;
			case 1: return 2;
			default: return 0;
		}
	}

	@Deprecated
	private final byte evaluateDefensiveReachability(BoardConfiguration bc) {
		byte totalReachables = 0;
		byte numberOfBlacks = bc.getNumberOfBlacks();
		
		boolean[] blackPositions = bc.getBlackPositions();
		boolean[] whitePositions = bc.getWhitePositions();
		
		byte whiteM = bc.getWhiteM();
		
		/*
		 * Conteggio delle pedine nere sotto attacco. Per ogni pedina nera
		 * si scorrono le bianche e se si trova una bianca che la raggiunge,
		 * si incrementa il contatore e si passa alla nera successiva (perciò il "break loop").
		 */
		for(byte black=0; black<GlobalValues.CELLS_NUMBER; black++){
			if(blackPositions[black]==true){
				loop: for(byte white=0; white<GlobalValues.CELLS_NUMBER; white++){
					if(whitePositions[white]==true && BoardReachings.isReachable(white, black, whiteM)){
						totalReachables++;
						break loop;
					}
				}
			}
		}
		
		byte percentage = (byte)(Math.round(totalReachables/numberOfBlacks)*10);
		
		// Scoring
		switch(percentage){
			case 0: return 15; //25;
			case 1: return 13; //23;
			case 2: return 12; //21;
			case 3: return 11; //16;
			case 4: return 10; //12;
			case 5: return 7; //10;
			case 6: return 3; //8;
			case 7: return 2; //5;
			case 8: return 0; //3;
			case 9: return 0; //2;
			default: return 0;
		}
	}
	
}
