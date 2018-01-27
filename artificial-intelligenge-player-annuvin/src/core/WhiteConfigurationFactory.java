package core;

import java.util.LinkedList;


/**
 * Classe Factory che ha la responsabilità di costruire le configurazioni successive ad una data
 * configurazione di scacchiera. Va utilizzato quando il turno è BIANCO.
 */
public class WhiteConfigurationFactory {
	
	/**
	 * Calcola le distanze tra le pedine avversarie (nere)
	 * @param c la configurazione corrente.
	 * @return la matrice delle distanze. 
	 */
	public static byte[][] getDistancesInBlacks(BoardConfiguration c){
		boolean[] blackPositions = c.getBlackPositions();
		byte[][] distances = new byte[37][37];
		for(byte q=0; q<blackPositions.length; q++){
			for(byte t=0; t<blackPositions.length; t++){
				if(blackPositions[q]==true && blackPositions[t]==true && q!=t)
					distances[q][t] = BoardReachings.getMinimumReachability(q, t, (byte)6);
				else
					distances[q][t] = 10;
 			}
		}
		return distances;
	}
	
	
	/**
	 * Produce tutte le configurazioni successive ad una configurazione data.
	 * @param c la configurazione corrente.
	 * @param includeActionString flag che indica se la action string  (azione dalla configurazione
	 * 		  precedente a quella attuale) deve essere inclusa oppure no.
	 * @return la lista delle configurazioni successive.
	 */
	public static LinkedList<BoardConfiguration> createNextConfigurations(BoardConfiguration c, boolean includeActionString){	
		// lista totale di tutte le possibili configurazioni
		LinkedList<BoardConfiguration> nextConfigurations = new LinkedList<BoardConfiguration>();
		
		LinkedList<BoardConfiguration> byMove = getConfigurationsByMove(c, includeActionString);
		
	
		nextConfigurations.addAll(byMove);
		
		LinkedList<BoardConfiguration> byCapture = getConfigurationsByCapture(c, includeActionString);
		
			
		nextConfigurations.addAll(byCapture);
		
		// Settaggio dei puntatori parent e sons della  BoardConfiguration c.
		for(BoardConfiguration config: nextConfigurations){
			config.setParent(c);
		}
		c.setSons(nextConfigurations);
		
		return nextConfigurations;
	}

	
	/**
	 * Genera tutte le configurazioni di scacchiera successive ad una configurazione data, ottenibili
	 * eseguendo uno spostamento semplice sulla scacchiera.
	 * @param c la configurazione di partenza.
	 * @param includeActionString flag che indica se la action string  (azione dalla configurazione
	 * 		  precedente a quella attuale) deve essere inclusa oppure no.
	 * @return la lista delle configurazioni successive.
	 */
	private static LinkedList<BoardConfiguration> getConfigurationsByMove(BoardConfiguration c, boolean includeActionString) {
		LinkedList<BoardConfiguration> configByMove = new LinkedList<BoardConfiguration>();
		
		boolean[] whitePawns = c.getWhitePositions();
		boolean[] blackPawns = c.getBlackPositions();
		byte whiteM = c.getWhiteM();
		
		// l'indice è il codice di cella
		for(byte cellCode=0; cellCode<whitePawns.length; cellCode++){
			if(whitePawns[cellCode]==false) 
				continue;
			
			boolean[] reachablesFromP = BoardReachings.getReachableCells(cellCode, whiteM);
			
			for(byte reachable=0; reachable<reachablesFromP.length; reachable++){
				
				if(reachablesFromP[reachable]==true && c.isFreeCell(reachable)){
					BoardConfiguration nextConfiguration = new BoardConfiguration(whitePawns, blackPawns, c.getTurn());
					nextConfiguration.moveWhite(cellCode, reachable);
					if(includeActionString==true){
						StringBuilder moveString = WhiteConfigurationFactory.getMoveMsg(cellCode, reachable);
						nextConfiguration.setActionToThisConfiguration(moveString);
					}
					configByMove.addLast(nextConfiguration);
				}
			}
		}
		return configByMove;
	}
	
	/**
	 * Costruisce una stringa che codifica una mossa (spostamento).
	 * @param cellCode la cella origine.
	 * @param reachable la cella destinazione.
	 * @return la codifica della move.
	 */
	private static StringBuilder getMoveMsg(byte cellCode, byte reachable){
		StringBuilder actionString = new StringBuilder();
		actionString.append(Translator.translateByteToString(cellCode));
		actionString.append("-");
		actionString.append(Translator.translateByteToString(reachable));
		return actionString;
	}
	

	/**
	 * Costruisce una stringa che codifica una cattura.
	 * @param cellCode la cella origine.
	 * @param reachable le celle destinazione (pedine da catturare).
	 * @return la codifica della cattura.
	 */
	private static StringBuilder getCaptureMsg(byte cellCode, byte... reachable){
		StringBuilder actionString = new StringBuilder();
		actionString.append(Translator.translateByteToString(cellCode));
		actionString.append("-");
		for(byte captured=0; captured<reachable.length; captured++){
			actionString.append(Translator.translateByteToString(reachable[captured]));
			actionString.append("X");
			if(captured!=reachable.length-1)
				actionString.append("-");
		}
		return actionString;
	}
	
	/**
	 * Genera tutte le configurazioni di scacchiera successive ad una configurazione data, ottenibili
	 * eseguendo una cattura singola o multipla.
	 * @param c la configurazione di partenza.
	 * @param includeActionString flag che indica se la action string  (azione dalla configurazione
	 * 		  precedente a quella attuale) deve essere inclusa oppure no.
	 * @return la lista delle configurazioni successive.
	 */
	private static LinkedList<BoardConfiguration> getConfigurationsByCapture(BoardConfiguration c, boolean includeActionString) {
		LinkedList<BoardConfiguration> configByCapture = new LinkedList<BoardConfiguration>();
		LinkedList<Capture> toExpand = new LinkedList<Capture>();
		
		boolean[] whitePawns = c.getWhitePositions();
		boolean[] blackPawns = c.getBlackPositions();
		byte m = c.getWhiteM();
		
		byte[][] distances = getDistancesInBlacks(c);
		
		// Costruzione delle catture con un salto
		for(byte whiteCell=0; whiteCell<whitePawns.length; whiteCell++){
			if(whitePawns[whiteCell]==false)
				continue;
			for(byte blackCell=0; blackCell<blackPawns.length; blackCell++){
				if(blackPawns[blackCell]==false)
					continue;
				if(BoardReachings.isReachable(whiteCell, blackCell, m)){
					byte m_res = (byte) (m - BoardReachings.getMinimumReachability(whiteCell, blackCell, m));
					Capture capture = new Capture(whiteCell, m_res, distances);
					capture.expandWith(blackCell);
					BoardConfiguration configuration = new BoardConfiguration(whitePawns, blackPawns, c.getTurn());
					configuration.captureByWhite(whiteCell, blackCell);
					StringBuilder captureString = WhiteConfigurationFactory.getCaptureMsg(whiteCell, blackCell);
					configuration.setActionToThisConfiguration(captureString);
					configByCapture.addLast(configuration);

					toExpand.addLast(capture);
				}
			}
		}
		while(!toExpand.isEmpty()){
			for(byte capture=0; capture<toExpand.size(); capture++){
				Capture currentCapture = toExpand.get(capture);
				byte old_size  = currentCapture.size();
				for(byte blackCell=0; blackCell<blackPawns.length; blackCell++){
					if(blackPawns[blackCell]==false || currentCapture.contains(blackCell)) 
						continue;
					if(currentCapture.canExpandWith(blackCell)){
						currentCapture.expandWith(blackCell);
						BoardConfiguration configuration = new BoardConfiguration(whitePawns, blackPawns, c.getTurn());
						byte[] destinations = new byte[currentCapture.getCells().size()];
						for(byte i=0; i<destinations.length; i++)
							destinations[i] = currentCapture.getCells().get(i);
						configuration.captureByWhite(currentCapture.getSource(), destinations);
						if(includeActionString==true){
							StringBuilder capturesString = WhiteConfigurationFactory.getCaptureMsg(currentCapture.getSource(), destinations);
							configuration.setActionToThisConfiguration(capturesString);
						}
						configByCapture.addLast(configuration);
					}
				}
				if(currentCapture.size()==old_size) 
					toExpand.remove(currentCapture);
			}
		}		
		return configByCapture;
	}
}
