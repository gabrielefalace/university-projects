package gameModes;


import heuristics.ExpertHeuristicBlack;
import heuristics.ExpertHeuristicWhite;
import interfaces.Player;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import players.BlackQuiescencePlayer;
import players.WhiteQuiescencePlayer;
import common.TextMatcher;
import core.BoardConfiguration;
import core.BoardReachings;
import core.OpeningBlackMove;
import core.OpeningWhiteMove;
import core.Translator;

/**
 * Classe che implementa la modalità di gioco via socket.
 */
public class NetQui {
	
	/**
	 * Verifica se una data configurazione è di vittoria (a prescindere dal giocatore vincente). 
	 * @param gameBoard la configurazione da verificare.
	 * @return true se è una configurazione di vittoria, false altrimenti.
	 */
	public static boolean checkWinningConfiguration(BoardConfiguration gameBoard){
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
		for(byte d=0; d<dest.length-1; d++)
			sumDist += BoardReachings.getDistanceBetween(dest[d], dest[d+1]);
		return (sumDist <= adversaryM);
	}
	
	
	
	/**
	 * Entry point della modalità di gioco via socket.
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
		BufferedReader in = null;
		PrintWriter out = null;
		ServerSocket ss = null;
		Socket connection = null;
		int myPly = 0;
		int adversaryPly = 0;
		
		try{
			TextMatcher matcher = new TextMatcher();
			int maxConfigurationNumber = 25000;
			BoardConfiguration board = new BoardConfiguration();
			Scanner scanner = new Scanner(System.in);
			System.out.println("modificare il valore [25000] cutThreshold? (S) si");
			String response = scanner.nextLine();
			if(response.equalsIgnoreCase("S")){
				System.out.println("inserire il nuovo valore di cutThreshold");
				maxConfigurationNumber = Integer.parseInt(scanner.nextLine());
			}
			System.out.println("Il tuo giocatore e': (B) Bianco (N) Nero");
			String playerColor = scanner.nextLine();
			Player player = null;
			
			// setup BIANCO
			if(playerColor.equalsIgnoreCase("B")){
				final int PORT_NUMBER = 44000; 
				ExpertHeuristicWhite heuristic = new ExpertHeuristicWhite();
				player = new WhiteQuiescencePlayer(heuristic, maxConfigurationNumber);
				System.out.println("La PORT del giocatore BIANCO è "+PORT_NUMBER);
				ss = new ServerSocket(PORT_NUMBER);
				connection = ss.accept();
				out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), "ISO-8859-1"),true);

				// elaborazione mossa
				BoardConfiguration whiteBoard = new BoardConfiguration(board); 
				byte source = OpeningWhiteMove.getSources((byte)(myPly+1));
				byte dest = OpeningWhiteMove.getDestinations((byte)(myPly+1));
				StringBuilder action = new StringBuilder(Translator.translateByteToString(source)+"-"+Translator.translateByteToString(dest));
				
				// Worm up
				player.getBestNextConfiguration(board);
				
				// invio azione
				out.println(action.toString());
				System.out.println(action);
				// aggiorna la scacchiera
				whiteBoard.setActionToThisConfiguration(action);
				whiteBoard.moveWhite(source, dest);
				board = whiteBoard;
				board.setParent(null);
				System.out.println(board);
				myPly++;
				//Apre gli stream di input per ricevere mossa del nero
				in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "ISO-8859-1"));
			}
			// setup NERO
			else{
				ExpertHeuristicBlack heuristic = new ExpertHeuristicBlack();
				player = new BlackQuiescencePlayer(heuristic, maxConfigurationNumber);
				System.out.println("Attendo IP dal Server");
				String ip = scanner.nextLine();
				
				System.out.println("Attendo PORT dal Server");
				int port = Integer.parseInt(scanner.nextLine());
				
				connection = new Socket(ip, port);
				out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), "ISO-8859-1"),true);
				in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "ISO-8859-1"));
			}
			
			// ciclo partita 
			while(myPly<=100 || adversaryPly<=100){
				if(checkWinningConfiguration(board)){
					System.out.println(board);
					return;
				}
					
				if(adversaryPly <= 100){
					// lettura azione avversario
					String adversaryActionRaw = in.readLine();
					System.out.println("Ricevuta mossa: "+adversaryActionRaw);
					
					// l'azione è una CATTURA
					if(matcher.contains("X", adversaryActionRaw, true)){
						String adversaryActionTmp = adversaryActionRaw.replace('X', ' ');
						String adversaryActionTm2 = adversaryActionTmp.replace('-', ' ');
						String adversaryAction = adversaryActionTm2.trim();

						String[] tokens = adversaryAction.split("\\s+");
						byte sourceCell = Translator.translateStringToByte(tokens[0]);
						if(sourceCell == -1){
							System.out.println("Mossa avversaria contiene cella sorgente inesistente: VITTORIA!");
							System.out.println(board);
							return;
						}

						byte[] destinations = new byte[tokens.length-1];
						for(byte i=1; i<tokens.length; i++){
							destinations[i-1] = Translator.translateStringToByte(tokens[i]);
							if(destinations[i-1] == -1){
								System.out.println("Mossa avversaria contiene cella destinazione inesistente: VITTORIA!");
								System.out.println(board);
								return;
							}
						}

//						if(!isCorrectCapture(board, sourceCell, destinations, playerColor)){
//							System.out.println("Cattura fallita, l'avversario non ha rispettato l'm: VITTORIA!");
//							return;
//						}
						
						boolean outcome;
						
						if(playerColor.equalsIgnoreCase("B"))
							outcome = board.captureByBlack(sourceCell, destinations);
						else
							outcome = board.captureByWhite(sourceCell, destinations);
							
						if(outcome == false){
							System.out.println("Cattura fallita: Vittoria dell'avversario!!!");
							System.out.println(board);
							return;
						}		
					}
					else{
						// l'azione è una move					
						String[] tokens = adversaryActionRaw.split("-");
						if(tokens.length != 2){
							System.out.println("Stringa malformata: Vittoria!!!");
							System.out.println(board);
							return;
						}	
						byte sourceCell = Translator.translateStringToByte(tokens[0]);
						byte destination = Translator.translateStringToByte(tokens[1]);
										
						if(sourceCell == -1){
							System.out.println("Mossa avversaria contiene cella sorgente inesistente: VITTORIA!");
							System.out.println(board);
							return;
						}
						if(destination == -1){
							System.out.println("Mossa avversaria contiene cella destinazione inesistente: VITTORIA!");
							System.out.println(board);
							return;
						}
						
//						if(!isCorrectMove(board, sourceCell, destination, playerColor)){
//							System.out.println("Cattura fallita, l'avversario non ha rispettato l'm: VITTORIA!");
//							return;
//						}
						
						boolean outcome;
						if(playerColor.equalsIgnoreCase("B"))
							outcome = board.moveBlack(sourceCell, destination);
						else
							outcome = board.moveWhite(sourceCell, destination);
						
						if(outcome == false){
							System.out.println("Mossa errata: Vittoria!");
							System.out.println(board);
							return;
						}	
						else{
							System.out.println("Mossa eseguita con successo! ");
							System.out.println(board);
						}
					} // fine elaborazione mossa avversaria
					adversaryPly++;
				}
				
				System.out.println(board);
				
				if(checkWinningConfiguration(board)){
					System.out.println(board);
					return;
				}
				
				if(myPly<=100){
					// Nuova mossa Protagonista
					BoardConfiguration newGameBoard;
					
					if((myPly+1)<=5){
						if(myPly==0)	//worm up
							player.getBestNextConfiguration(board);
						
						newGameBoard = new BoardConfiguration(board);
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
						newGameBoard = player.getBestNextConfiguration(board);
					}
					
					
					// invio mossa all'avversario
					out.println(newGameBoard.getActionToThisConfiguration().toString());

					// aggiornamento board protagonista
					board = newGameBoard;
					board.setParent(null);

					System.out.println(board);
					myPly++;
				}
			} // fine ciclo partita
		} // fine try
		catch(Exception exc){
			exc.printStackTrace();
		}
		finally{
			try{
				if(in != null) in.close();
				if(out != null) out.close();
				if(connection != null) connection.close();
				if(ss != null) ss.close();
			}
			catch(Exception e){e.printStackTrace();}
		}
	}
}
