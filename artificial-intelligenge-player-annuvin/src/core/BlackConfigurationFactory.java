package core;

import java.util.LinkedList;


/**
 * Classe Factory che ha la responsabilità di costruire le configurazioni successive ad una data
 * configurazione di scacchiera. Va utilizzato quando il turno è NERO.
 */
public class BlackConfigurationFactory {
	
	/**
	 * Calcola le distanze tra le pedine avversarie (bianche)
	 * @param c la configurazione corrente.
	 * @return la matrice delle distanze. 
	 */
	public static byte[][] getDistancesInWhites(BoardConfiguration c){
		boolean[] whitePositions = c.getWhitePositions();
		byte[][] distances = new byte[37][37];
		for(byte q=0; q<whitePositions.length; q++){
			for(byte t=0; t<whitePositions.length; t++){
				if(whitePositions[q]==true && whitePositions[t]==true && q!=t)
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
		byte blackM = c.getBlackM();
		
		// l'indice è il codice di cella
		for(byte cellCode=0; cellCode<blackPawns.length; cellCode++){
			if(blackPawns[cellCode]==false) 
				continue;
			
			boolean[] reachablesFromP = BoardReachings.getReachableCells(cellCode, blackM);
			
			for(byte reachable=0; reachable<reachablesFromP.length; reachable++){
				
				if(reachablesFromP[reachable]==true && c.isFreeCell(reachable)){
					BoardConfiguration nextConfiguration = new BoardConfiguration(whitePawns, blackPawns, c.getTurn());
					nextConfiguration.moveBlack(cellCode, reachable);
					if(includeActionString==true){
						StringBuilder moveString = BlackConfigurationFactory.getMoveMsg(cellCode, reachable);
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
		byte m = c.getBlackM();
		
		byte[][] distances = getDistancesInWhites(c);
		
		// Costruzione delle catture con un salto
		for(byte blackCell=0; blackCell<blackPawns.length; blackCell++){
			if(blackPawns[blackCell]==false)
				continue;
			for(byte whiteCell=0; whiteCell<whitePawns.length; whiteCell++){
				if(whitePawns[whiteCell]==false)
					continue;
				if(BoardReachings.isReachable(blackCell, whiteCell, m)){
					byte m_res = (byte) (m - BoardReachings.getMinimumReachability(blackCell, whiteCell, m));
					Capture capture = new Capture(blackCell, m_res, distances);
					capture.expandWith(whiteCell);
					BoardConfiguration configuration = new BoardConfiguration(whitePawns, blackPawns, c.getTurn());
					configuration.captureByBlack(blackCell, whiteCell);
					StringBuilder captureString = BlackConfigurationFactory.getCaptureMsg(blackCell, whiteCell);
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
				for(byte whiteCell=0; whiteCell<whitePawns.length; whiteCell++){
					if(whitePawns[whiteCell]==false || currentCapture.contains(whiteCell)) 
						continue;
					if(currentCapture.canExpandWith(whiteCell)){
						currentCapture.expandWith(whiteCell);
						BoardConfiguration configuration = new BoardConfiguration(whitePawns, blackPawns, c.getTurn());
						byte[] destinations = new byte[currentCapture.getCells().size()];
						for(byte i=0; i<destinations.length; i++)
							destinations[i] = currentCapture.getCells().get(i);
						configuration.captureByBlack(currentCapture.getSource(), destinations);
						if(includeActionString==true){
							StringBuilder capturesString = BlackConfigurationFactory.getCaptureMsg(currentCapture.getSource(), destinations);
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
