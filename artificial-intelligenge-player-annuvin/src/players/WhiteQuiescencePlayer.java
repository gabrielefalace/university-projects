package players;

import interfaces.GlobalValues;
import interfaces.Heuristic;
import interfaces.Player;
import java.util.LinkedList;
import java.util.Random;

import core.BlackConfigurationFactory;
import core.BoardConfiguration;
import core.QuietNodeChecker;
import core.WhiteConfigurationFactory;

/**
 * Implementazione del giocatore BIANCO che utilizza il MIN-MAX.
 */
public class WhiteQuiescencePlayer implements Player {
	
	/**
	 * L'euristica utilizzata dal giocatore.
	 */
	private Heuristic heuristic;
	
	/**
	 * Il numero massimo di configurazioni da esplorare.
	 */
	private int maxConfigurationNumber;
	
	/**
	 * Costruttore parametrico.
	 * @param heuristic l'euristica da usare.
	 * @param maxConfigurationNumber il numero massimo di configurazioni.
	 */
	public WhiteQuiescencePlayer(Heuristic heuristic, int maxConfigurationNumber){
		this.heuristic = heuristic;
		this.maxConfigurationNumber = maxConfigurationNumber;
	}
	
	@Override
	public final BoardConfiguration getBestNextConfiguration(BoardConfiguration c){
		BoardConfiguration[] treeExpansion = expandTree(c);
		System.out.println("CONFIGURAZIONI VISITATE: "+treeExpansion.length);
		labelTree(treeExpansion);
		
		// Scelta della prossima configurazione
		LinkedList<BoardConfiguration> sons = c.getSons();
		BoardConfiguration currentbestConfig = sons.getFirst();
		byte currentMax = currentbestConfig.getValue();
		LinkedList<BoardConfiguration> sameValue = new LinkedList<BoardConfiguration>();
		for(BoardConfiguration son: sons){
			byte sonValue = son.getValue();
			if(sonValue == currentMax){
				sameValue.add(son);
			}
			if(sonValue>currentMax){
				sameValue.clear();
				sameValue.add(son);
				currentMax = sonValue;
				currentbestConfig = son;
			}
		}
		if(currentMax >= 10){
			Random r = new Random();
			currentbestConfig = sameValue.get(r.nextInt(sameValue.size()));
			System.out.println("Applicato criterio Random");
		}
		else{
			currentbestConfig = sameValue.getFirst();
			byte bestDifference = (byte)(currentbestConfig.getNumberOfWhites() - currentbestConfig.getNumberOfBlacks());
			for(BoardConfiguration config: sameValue){
				byte currentDifference = (byte)(config.getNumberOfWhites() - config.getNumberOfBlacks());
				if(currentDifference > bestDifference){
					bestDifference = currentDifference;
					currentbestConfig = config;
				}
			}
			System.out.println("Applicato criterio ottimizzazione differenza pedine");
			System.out.println("la lista sameValue conteneva "+sameValue.size()+ " elementi, il cui massimo è "+currentMax);
		}
		
		// IMPORTANTISSIMO:
		currentbestConfig.getSons().clear();		
		return currentbestConfig;
	}
	
	/**
	 * Etichetta l'albero di gioco, mediante una singola scansione all'indietro.
	 * Procedendo all'indietro nella lista, ogni elemento è una foglia o un nodo con tutti i figli etichettati.
	 * @param treeExpansion la lista contenente l'albero.
	 */
	private final void labelTree(BoardConfiguration[] treeExpansion){
		for(int k=treeExpansion.length-1; k>=0; k--){
			BoardConfiguration bc = treeExpansion[k];
			LinkedList<BoardConfiguration> sons = bc.getSons();
			if(sons.isEmpty()){
				bc.setValue(heuristic.evaluate(bc));
			}
			else if(bc.getTurn()==GlobalValues.WHITE_TURN)
				bc.setValue(selectMaximumValue(sons));
			else 
				bc.setValue(selectMinimumValue(sons));
		}
	}

	/**
	 * Seleziona il valore minimo di etichetta in una lista BoardConfiguration.
	 * @param sons la lista di configurazioni.
	 * @return il valore minimo di euristica.
	 */
	private byte selectMinimumValue(LinkedList<BoardConfiguration> sons) {
		byte currentMin = sons.getFirst().getValue();
		for(BoardConfiguration son: sons){
			byte sonValue = son.getValue();
			if(sonValue<currentMin) currentMin = sonValue;
		}
		return currentMin;
	}

	/**
	 * Seleziona il valore massimo di etichetta in una lista BoardConfiguration.
	 * @param sons la lista di configurazioni.
	 * @return il valore massimo di euristica.
	 */
	private byte selectMaximumValue(LinkedList<BoardConfiguration> sons) {
		byte currentMax = sons.getFirst().getValue();
		for(BoardConfiguration son: sons){
			byte sonValue = son.getValue();
			if(sonValue>currentMax) currentMax = sonValue;
		}
		return currentMax;
	}

	/**
	 * Espande l'intero albero radicato in una BoardConfiguration data.
	 * @param c la BoardCOnfiguration (root) da cui espandere.
	 * @return la lista di BoardConfiguration che rappresenta l'albero.
	 */
	private final BoardConfiguration[] expandTree(BoardConfiguration c){
		BoardConfiguration[] treeExpansion = new BoardConfiguration[maxConfigurationNumber];
		
		//Arrays.fill(treeExpansion, null);
		
		LinkedList<BoardConfiguration> toVisit = new LinkedList<BoardConfiguration>();
		toVisit.addFirst(c);
		int arrayIndex = 0;
		
		// Prima iterazione a parte per consentire la memorizzazione delle 
		// sole action-string utili: quelle dei figli della radice!
		
		BoardConfiguration root = toVisit.removeFirst();
		byte rootBlacks = root.getNumberOfBlacks();
		byte rootWhites = root.getNumberOfWhites();
		LinkedList<BoardConfiguration> root_sons;
		// Creazione delle configurazioni successive
		if(root.getTurn()==GlobalValues.WHITE_TURN)
			root_sons = WhiteConfigurationFactory.createNextConfigurations(root, true);
		else
			root_sons = BlackConfigurationFactory.createNextConfigurations(root, true);
		
		treeExpansion[arrayIndex] = root;
		arrayIndex++;
		
		// Verifica se i figli non eccedono la soglia
		if(root_sons.size()+arrayIndex+toVisit.size() <= maxConfigurationNumber){
			// Aggiunta dei figli a partire dalla coda
			for(int j = root_sons.size()-1; j>=0; j--)
				toVisit.addFirst(root_sons.get(j));
		}	
		else 
			root.getSons().clear();
		
		// Altre iterazioni senza action-string
		while(!toVisit.isEmpty()){
			// Albero linearizzato nell'ordine di visita
			BoardConfiguration bc = toVisit.removeFirst();
			
			treeExpansion[arrayIndex] = bc;
			arrayIndex++;
			
			if(rootBlacks>=4 && rootWhites>=4 && bc.getNumberOfBlacks()+bc.getNumberOfWhites()<=4){
				if(bc.isBlackWinningConfiguration() || bc.isWhiteWinningConfiguration() || QuietNodeChecker.isBlackWinning(bc) || QuietNodeChecker.isWhiteWinning(bc))
					continue;
			}
			
			LinkedList<BoardConfiguration> sons;
			// Creazione delle configurazioni successive
			if(bc.getTurn()==GlobalValues.WHITE_TURN)
				sons = WhiteConfigurationFactory.createNextConfigurations(bc, false);
			else
				sons = BlackConfigurationFactory.createNextConfigurations(bc, false);
			
			// Verifica se i figli non eccedono la soglia
			if(sons.size()+arrayIndex+toVisit.size() <= maxConfigurationNumber){
				// Aggiunta dei figli a partire dalla coda
				for(int j = sons.size()-1; j>=0; j--){
					toVisit.addFirst(sons.get(j));
				}
			}	
			else{ 
				bc.getSons().clear();
				break;
			}
		}
		// Finiamo di copiare gli elementi
		while(!toVisit.isEmpty()){
			treeExpansion[arrayIndex] = toVisit.removeFirst();
			arrayIndex++;
		}
		
		BoardConfiguration[] tree = new BoardConfiguration[arrayIndex];
		//Arrays.fill(tree, null);
		System.arraycopy(treeExpansion, 0, tree, 0, arrayIndex);
		return tree;
	}
}
