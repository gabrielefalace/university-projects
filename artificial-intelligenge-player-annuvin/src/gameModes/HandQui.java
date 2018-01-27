package gameModes;


import interfaces.Player;
import java.util.Scanner;
import players.BlackQuiescencePlayer;
import players.WhiteQuiescencePlayer;
import common.TextMatcher;
import core.BoardConfiguration;
import core.BoardReachings;
import core.OpeningBlackMove;
import core.OpeningWhiteMove;
import core.Translator;
import heuristics.ExpertHeuristicBlack;
import heuristics.ExpertHeuristicWhite;


/**
 * Classe che implementa la modalità di gioco manuale.
 */
public class HandQui {

	/**
	 * Verifica se una data configurazione è di vittoria (a prescindere dal giocatore vincente). 
	 * @param gameBoard la configurazione da verificare.
	 * @return true se è una configurazione di vittoria, false altrimenti.
	 */
	private static boolean checkWinningConfiguration(BoardConfiguration gameBoard){
		if(gameBoard.isBlackWinningConfiguration()){
			System.out.println("Configurazione di vittoria per il NERO!!!");
			return true;
		}
		if(gameBoard.isWhiteWinningConfiguration()){
			System.out.println("Configurazione di vittoria per il BIANCO!!!");
			return true;
		}
		return false;
	}
	
	/**
	 * Verifica che una move avversaria rispetti l'm avversario
	 * @param gameBoard la configurazione su cui eseguire la mossa
	 * @param src la cella origine
	 * @param dest la cella destinazione
	 * @param playerColor il colore del protagonista per ricavare l'm avversario
	 * @return true se la move avversaria rispetta l'm, false altrimenti
	 */
	private static boolean isCorrectMove(BoardConfiguration gameBoard, byte src, byte dest, String playerColor){
		byte adversaryM;
		if(playerColor.equalsIgnoreCase("B"))
			adversaryM = gameBoard.getBlackM();
		else 
			adversaryM = gameBoard.getWhiteM();
		System.out.print("L'm avversario è "+adversaryM+" - ");
		System.out.println("La distanza fra "+Translator.translateByteToString(src)+" e "+Translator.translateByteToString(dest)+" "+BoardReachings.getDistanceBetween(src, dest));
		return BoardReachings.isReachable(src, dest, adversaryM);
	}
	
	/**
	 * Verifica che la cattura segnalata rispetti la mobilità massima
	 * @param gameBoard la configurazione di scacchiera attuale
	 * @param src la cella sorgente
	 * @param dest la cella destinazione
	 * @param playerColor il colore del protagonista
	 * @return se la capture avversaria rispetta l'm, false altrimenti
	 */
	private static boolean isCorrectCapture(BoardConfiguration gameBoard, byte src, byte[] dest, String playerColor){
		byte adversaryM;
		if(playerColor.equalsIgnoreCase("B"))
			adversaryM = gameBoard.getBlackM();
		else 
			adversaryM = gameBoard.getWhiteM();
		byte sumDist = BoardReachings.getDistanceBetween(src, dest[0]);
		System.out.println("ci sono "+dest.length+" destinazioni");
		for(int d=1; d<dest.length; d++){
			sumDist += BoardReachings.getDistanceBetween(dest[d-1], dest[d]);
			System.out.print("La distanza fra "+Translator.translateByteToString(dest[d-1])+" e "+Translator.translateByteToString(dest[d])+" "+BoardReachings.getDistanceBetween(dest[d-1], dest[d])+" - ");
		}
		System.out.println("L'm avversario è "+adversaryM);
		return (sumDist <= adversaryM);
	}
	
	
	/**
	 * Entry point della modalità manuale di gioco.
	 * Esegue le seguenti fasi:
	 * <ol>
	 * 	<li>inizializzazione dei componenti di base;</li>
	 * 	<li>acquisizione tipo giocatore;</li>
	 * 	<li>setup del giocatore;</li>
	 * 	<li>se il giocatore è bianco: esegue la prima mossa;</li>
	 * 	<li>attesa mossa avversaria;</li>
	 * 	<li>elaborazione ed esecuzione della mossa avversaria (verifica casi di errore);</li>
	 * 	<li>se la configurazione è finale: END;</li>
	 * 	<li>elaborazione e invio della propria mossa;</li>
	 * 	<li>aggiornamento scacchiera;</li>
	 * 	<li>se la configurazione è finale: END;</li>
	 * 	<li>goto 5.</li>
	 * </ol>
	 * @param args
	 */
	public static void main(String[] args){
		
		TextMatcher matcher = new TextMatcher();
		Player player = null;
		BoardConfiguration gameBoard = new BoardConfiguration();
		Scanner in = new Scanner(System.in);
		
		int cutThreshold = 35000;
		
		System.out.println("modificare il valore [35000] cutThreshold? (S) si");
		String response = in.nextLine();
		if(response.equalsIgnoreCase("S")){
			System.out.println("inserire il nuovo valore di cutThreshold");
			cutThreshold = Integer.parseInt(in.nextLine());
		}
		
		System.out.println("Il computer giocherà come: (B) Bianco (N) Nero");
		
		String playerColor = in.nextLine();
		byte myPly = 0;
		byte adversaryPly = 0;
				
		if(playerColor.equalsIgnoreCase("B")){
			//init BIANCO
			ExpertHeuristicWhite heuristic = new ExpertHeuristicWhite();
			player = new WhiteQuiescencePlayer(heuristic, cutThreshold);
						
			// Prima mossa del BIANCO
			
			BoardConfiguration whiteBoard = new BoardConfiguration(gameBoard);
			byte source = OpeningWhiteMove.getSources((byte)(myPly+1));
			byte dest = OpeningWhiteMove.getDestinations((byte)(myPly+1));
			StringBuilder action = new StringBuilder(Translator.translateByteToString(source)+"-"+Translator.translateByteToString(dest));
			whiteBoard.setActionToThisConfiguration(action);
			whiteBoard.moveWhite(source, dest);

			whiteBoard.setParent(null);
			System.out.println("Mossa iniziale Bianco: "+whiteBoard.getActionToThisConfiguration()+"\n");
			gameBoard = whiteBoard;
			System.out.println(gameBoard);
			myPly++;
		}
		else{
			//init NERO
			ExpertHeuristicBlack heuristic = new ExpertHeuristicBlack();
			player = new BlackQuiescencePlayer(heuristic, cutThreshold);
		}
				
		// ciclo partita
		while(myPly<=100 || adversaryPly<=100){
			if(checkWinningConfiguration(gameBoard)){
				System.out.println(gameBoard);
				return;
			}
			
			if(adversaryPly<=100){
				System.out.println("Inserire mossa: ");
				String adversaryActionRaw = in.nextLine();
				System.out.println("E' stata inserita la mossa: "+adversaryActionRaw);
				
				if(matcher.contains("X", adversaryActionRaw, true)){
					
					String adversaryActionTmp = adversaryActionRaw.replace('X', ' ');
					String adversaryActionTm2 = adversaryActionTmp.replace('-', ' ');
					String adversaryAction = adversaryActionTm2.trim();
					String[] tokens = adversaryAction.split("\\s+");
					byte sourceCell = Translator.translateStringToByte(tokens[0]);
					
					if(sourceCell == -1){
						System.out.println("Mossa avversaria contiene cella sorgente inesistente: VITTORIA!");
						System.out.println(gameBoard);
						return;
					}
									
					byte[] destinations = new byte[tokens.length-1];
					for(byte i=1; i<tokens.length; i++){
						destinations[i-1] = Translator.translateStringToByte(tokens[i]);
						if(destinations[i-1] == -1){
							System.out.println("Mossa avversaria contiene cella destinazione inesistente: VITTORIA!");
							System.out.println(gameBoard);
							return;
						}
					}
					if(!isCorrectCapture(gameBoard, sourceCell, destinations, playerColor)){
						System.out.println("Cattura fallita, l'avversario non ha rispettato l'm: VITTORIA!");
						System.out.println(gameBoard);
						return;
					}
		
					boolean outcome;
					
					if(playerColor.equalsIgnoreCase("B"))
						outcome = gameBoard.captureByBlack(sourceCell, destinations);
					else
						outcome = gameBoard.captureByWhite(sourceCell, destinations);
						
					if(outcome == false){
						System.out.println("Cattura avversaria fallita: VITTORIA!");
						System.out.println(gameBoard);
						return;
					}			
				}
				else{
					// Si tratta di una move
					
					String[] tokens = adversaryActionRaw.split("-");
					if(tokens.length != 2){
						System.out.println("Stringa malformata: Vittoria!!!");
						System.out.println(gameBoard);
						return;
					}	
					byte sourceCell = Translator.translateStringToByte(tokens[0]);
					if(sourceCell == -1){
						System.out.println("Mossa avversaria contiene cella sorgente inesistente: VITTORIA!");
						System.out.println(gameBoard);
						return;
					}
					byte destination = Translator.translateStringToByte(tokens[1]);
					if(destination == -1){
						System.out.println("Mossa avversaria contiene cella destinazione inesistente: VITTORIA!");
						System.out.println(gameBoard);
						return;
					}
				
					
					if(!isCorrectMove(gameBoard, sourceCell, destination, playerColor)){
						System.out.println("Move fallita, l'avversario non ha rispettato l'm: VITTORIA!");
						System.out.println(gameBoard);
						return;
					}
					
					
					boolean outcome;
					if(playerColor.equalsIgnoreCase("B"))
						outcome = gameBoard.moveBlack(sourceCell, destination);
					else
						outcome = gameBoard.moveWhite(sourceCell, destination);
					
					if(outcome == false){
						System.out.println("Ricevuta mossa errata: Vittoria!");
						System.out.println(gameBoard);
						return;
					}	
				}
				// Fine ultima mossa avversario
				adversaryPly++;
			}
			
			System.out.println(gameBoard);
			
			if(checkWinningConfiguration(gameBoard)){ 
				System.out.println(gameBoard);
				return;
			}
			
			if(myPly<=100){
				// Nuova mossa Protagonista
				
				BoardConfiguration newGameBoard = new BoardConfiguration(gameBoard);
					
				if((myPly+1)<=5){
					byte source, dest;
					if(playerColor.equalsIgnoreCase("B")){
						source = OpeningWhiteMove.getSources((byte)(myPly+1));
						dest = OpeningWhiteMove.getDestinations((byte)(myPly+1));
					}
					else{
						source = OpeningBlackMove.getSources((byte)(myPly+1));
						dest = OpeningBlackMove.getDestinations((byte)(myPly+1));
					}
					if(newGameBoard.isFreeCell(dest)){
						StringBuilder action = new StringBuilder(Translator.translateByteToString(source)+"-"+Translator.translateByteToString(dest));
						newGameBoard.setActionToThisConfiguration(action);
						if(playerColor.equalsIgnoreCase("B"))
							newGameBoard.moveWhite(source, dest);
						else 
							newGameBoard.moveBlack(source, dest);
					}
					else{
						StringBuilder action = new StringBuilder(Translator.translateByteToString(source)+"-"+Translator.translateByteToString(dest)+"X");
						newGameBoard.setActionToThisConfiguration(action);
						if(playerColor.equalsIgnoreCase("B"))
							newGameBoard.captureByWhite(source, dest);
						else
							newGameBoard.captureByBlack(source, dest);
					}
				}
				else{
					newGameBoard = player.getBestNextConfiguration(gameBoard);
				}
				
				
				newGameBoard.setParent(null);
				gameBoard = newGameBoard;		// aggiornamento scacchiera vera e propria
				
				if(playerColor.equalsIgnoreCase("B"))
					System.out.println("BIANCO: "+gameBoard.getActionToThisConfiguration()+"\n");
				else
					System.out.println("NERO: "+gameBoard.getActionToThisConfiguration()+"\n");
				myPly++;
			}
			
			System.out.println(gameBoard);
			
		}//while
		
		if(myPly >= 100 && adversaryPly >= 100){
			System.out.println("Partita Patta!!!");
			System.out.println(gameBoard);
		}
		
	} // main
}
