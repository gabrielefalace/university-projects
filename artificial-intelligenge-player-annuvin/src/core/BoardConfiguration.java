package core;

import java.util.LinkedList;


import interfaces.GlobalValues;

/**
 * Rappresenta una configurazione di scacchiera.
 */
@SuppressWarnings("unchecked")
public final class BoardConfiguration implements Comparable{

	/**
	 * Rappresenta il turno corrente del giocatore.
	 * @init il valore iniziale è WHITE_TURN (giocatore ad aprire è il BIANCO).
	 */
	private boolean turn = GlobalValues.WHITE_TURN;
	
	/**
	 * Posizioni delle pedine nere sulla scacchiera, come vettore caratteristico.
	 * @init il valore iniziale sono le posizioni iniziali delle pedine nere.
	 */
	private boolean[] blackPositions = {
									false, false, false, false, false, false, false, false, true, false,
									false, false, false, false, true, false, false, false, false, false, 
									true, true, false, false, false, false, false, true, false, false, 
									false, false, true, false, false, false, false
								   };
	
	/**
	 * Posizioni delle pedine bianche sulla scacchiera, come vettore caratteristico.
	 * @init il valore iniziale sono le posizioni iniziali delle pedine bianche.
	 */
	private boolean[] whitePositions = {
									false, false, false, false, true, false, false, false, false, true,
									false, false, false, false, false, true, true, false, false, false, 
									false, false, true, false, false, false, false, false, true, false, 
									false, false, false, false, false, false, false
		   						   };
	/**
	 * Il genitore della BoardConfiguration this.
	 */
	private BoardConfiguration parent;
	
	/**
	 * I figli della BoardConfiguration this.
	 */
	private LinkedList<BoardConfiguration> sons;
	
	/**
	 * Stringa che codifica la mossa da eseguire per arrivare in questa configurazione
	 * partendo dalla configurazione parent.
	 */
	private StringBuilder actionToThisConfiguration;
	
	/**
	 * Valore euristico di questa configurazione. 
	 */
	private byte value;
	
	/**
	 * Costruttore di Default, crea la configurazione iniziale della scacchiera.
	 */
	public BoardConfiguration(){
		setActionToThisConfiguration(new StringBuilder());
		setParent(null);
		setSons(new LinkedList<BoardConfiguration>());
		setValue(GlobalValues.UNLABELED);
	}

	
	/**
	 * Crea una configurazione di scacchiera generica. 
	 * @param whitePositions le posizioni delle pedine bianche.
	 * @param blackPositions le posizioni delle pedine nere.
	 */
	public BoardConfiguration(boolean[] whitePositions, boolean[] blackPositions, boolean turn){
		this.whitePositions = new boolean[whitePositions.length];
		this.blackPositions = new boolean[blackPositions.length];
		for(int i=0; i<whitePositions.length; i++){
			this.whitePositions[i] = whitePositions[i];
			this.blackPositions[i] = blackPositions[i];
		}
		this.turn = turn;
		setActionToThisConfiguration(new StringBuilder());
		setParent(null);
		setSons(new LinkedList<BoardConfiguration>());
		setValue(GlobalValues.UNLABELED);
	}
	
	/**
	 * Crea una copia profonda della configurazione.
	 * @param board La scacchiera da copiare.
	 */
	public BoardConfiguration(BoardConfiguration board){
		for(int i=0; i<GlobalValues.CELLS_NUMBER; i++){
			if(board.getWhitePositions()[i]==true)
				this.whitePositions[i] = true;
			else
				this.whitePositions[i] = false;
		}
		for(int i=0; i<GlobalValues.CELLS_NUMBER; i++){
			if(board.getBlackPositions()[i]==true)
				this.blackPositions[i] = true;
			else
				this.blackPositions[i] = false;
		}
		if(board.getTurn()==true)
			this.turn = true;
		else
			this.turn = false;
	}
	
	
	/**
	 * Imposta il valore euristico di questa configurazione.
	 * @param value il nuovo valore.
	 */
	public void setValue(byte value) {
		this.value = value;
	}

	/**
	 * Consente di accedere al valore euristico di questa configurazione.
	 * @return il valore di di questa configurazione.
	 */
	public byte getValue() {
		return value;
	}


	/**
	 * Consente di conoscere il giocatore di turno.
	 * @return true se è il turno del BIANCO, false se è il turno del NERO.
	 */
	public boolean getTurn(){
		return turn;
	}
	
	/**
	 * Modifica il valore del campo turn.
	 * @param turn il nuovo giocatore di turno.
	 */
	public void setTurn(boolean turn){
		this.turn = turn;
	}
	
	/**
	 * Imposta l'azione da eseguire per arrivare in questa configurazione.
	 * @param actionToThisConfiguration l'azione da eseguire.
	 */
	public void setActionToThisConfiguration(StringBuilder actionToThisConfiguration) {
		this.actionToThisConfiguration = actionToThisConfiguration;
	}

	/**
	 * Consente di accedere alla mossa che porta dalla configurazione parent a questa.
	 * @return la mossa da eseguire.
	 */
	public StringBuilder getActionToThisConfiguration() {
		return actionToThisConfiguration;
	}


	/**
	 * Consente di aggiornare i figli della BoardConfiguration this.
	 * @param sons i figli della BoardConfiguration this.
	 */
	public void setSons(LinkedList<BoardConfiguration> sons) {
		this.sons = sons;
	}

	/**
	 * Consente di ottenere i figli della BoardConfiguration this.
	 * @return i figli.
	 */
	public LinkedList<BoardConfiguration> getSons() {
		return sons;
	}

	/**
	 * Consente di aggiornare il genitore della BoardConfiguration.
	 * @param parent il nuovo genitore.
	 */
	public void setParent(BoardConfiguration parent) {
		this.parent = parent;
	}

	/**
	 * Consente di ottenere il genitore della BoardConfiguration.
	 * @return il genitore della BoardConfiguration.
	 */
	public BoardConfiguration getParent() {
		return parent;
	}


	/**
	 * Sposta una pedina nera.	
	 * @pre la cella source deve essere occupata da una pedina nera e la cella destination deve essere libera.
	 * @param source il codice della cella attualmente occupata dalla pedina nera.
	 * @param destination il codice della cella dove si vuole posizionare la pedina nera.
	 * @return true se la mossa viene eseguita correttamente, false altrimenti. 
	 */
	public final boolean moveBlack(byte source, byte destination){
		if(blackPositions[source]==true && isFreeCell(destination)){
			blackPositions[source] = false;
			blackPositions[destination] = true;
			turn = !turn;
			return true;
		}
		else
			return false;	
	}
	
	/**
	 * Sposta una pedina bianca.	
	 * @pre la cella source deve essere occupata da una pedina bianca e la cella destination deve essere libera.
	 * @param source il codice della cella attualmente occupata dalla pedina bianca.
	 * @param destination il codice della cella dove si vuole posizionare la pedina bianca.
	 * @return true se la mossa viene eseguita correttamente, false altrimenti. 
	 */
	public final boolean moveWhite(byte source, byte destination){
		if(whitePositions[source]==true && isFreeCell(destination)){
			whitePositions[source] = false;
			whitePositions[destination] = true;
			turn = !turn;
			return true;
		}
		else
			return false;	
	}
	
	/**
	 * Esegue una cattura di una o più pedine nere da parte di una pedina bianca. 
	 * @pre <ul><li>la cella source deve essere occupata da una pedina bianca;</li>
	 * 			<li> l'elenco dei codici deve essere non vuoto;</li>
	 * 			<li> <b>tutti</b> i codici delle celle destinazione devono essere relativi a pedine nere.</li>
	 * 		</ul>
	 * @param source il codice della cella attualmente occupata dalla pedina bianca.
	 * @param destinations l'elenco dei codici delle celle occupate dalle pedine nere.
	 * @return true se la cattura viene eseguita correttamente, false altrimenti.
	 */
	public final boolean captureByWhite(byte source, byte...destinations){
		if(whitePositions[source]==false || destinations.length==0)
			return false;
		for(byte dest: destinations)
			if(!isBlackCell(dest))
				return false;
		
		byte lastDest = destinations[destinations.length - 1];
		for(byte dest: destinations)
			blackPositions[dest] = false;
		whitePositions[source] = false;
		whitePositions[lastDest] = true;
		turn = !turn;
		return true;
	}
	
	/**
	 * Esegue una cattura di una o più pedine nere da parte di una pedina bianca. 
	 * @pre <ul><li>la cella source deve essere occupata da una pedina bianca;</li>
	 * 			<li> l'elenco dei codici deve essere non vuoto;</li>
	 * 			<li> <b>tutti</b> i codici delle celle destinazione devono essere relativi a pedine nere.</li>
	 * 		</ul>
	 * @param source il codice della cella attualmente occupata dalla pedina bianca.
	 * @param destinations l'elenco dei codici delle celle occupate dalle pedine nere.
	 * @return true se la cattura viene eseguita correttamente, false altrimenti.
	 */
	public final boolean captureByBlack(byte source, byte...destinations){
		if(blackPositions[source]==false || destinations.length==0)
			return false;
		for(byte dest: destinations)
			if(!isWhiteCell(dest))
				return false;
		
		byte lastDest = destinations[destinations.length - 1];
		for(byte dest: destinations)
			whitePositions[dest] = false;
		blackPositions[source] = false;
		blackPositions[lastDest] = true;
		turn = !turn;
		return true;
	}
	
	
	/* **********************************************
	 * 												*
	 * 				Metodi di utilità				*
	 * 												*
	 ************************************************/
	
	/**
	 * Consente di ottenere le posizioni delle pedine bianche.
	 * @return le posizioni delle pedine bianche.
	 */
	public final boolean[] getWhitePositions(){ return whitePositions; }
	
	/**
	 * Consente di ottenere le posizioni delle pedine nere.
	 * @return le posizioni delle pedine nere.
	 */
	public final boolean[] getBlackPositions(){ return blackPositions; }
	
	/**
	 * Consente di ottenere il numero corrente delle pedine bianche presenti sulla scacchiera.
	 * @return il numero delle pedine bianche.
	 */
	public final byte getNumberOfWhites(){ 
		byte total = 0;
		for(boolean boole: whitePositions)
			if(boole==true)
				total++;
		return total;
	}
	
	/**
	 * Consente di ottenere il numero corrente delle pedine nere presenti sulla scacchiera.
	 * @return il numero delle pedine nere.
	 */
	public final byte getNumberOfBlacks(){
		byte total = 0;
		for(boolean boole: blackPositions)
			if(boole==true)
				total++;
		return total;
	}
	
	/**
	 * Verifica se una cella della scacchiera è libera, ovvero non è occupata da alcuna pedina bianca nè da alcuna pedina nera.
	 * @param cellCode la cella da verificare.
	 * @return true se la cella è libera, false altrimenti.
	 */
	public final boolean isFreeCell(byte cellCode){	return (blackPositions[cellCode] == false) && (whitePositions[cellCode] == false); }
	
	/**
	 * Verifica se una cella è occupata da una pedina nera.
	 * @param cellCode la cella da verificare. 
	 * @return true se la cella è occupata da una pedina nera, false altrimenti.
	 */
	public final boolean isBlackCell(byte cellCode){ return blackPositions[cellCode]==true;	}
	
	/**
	 * Verifica se una cella è occupata da una pedina bianca.
	 * @param cellCode la cella da verificare. 
	 * @return true se la cella è occupata da una pedina bianca, false altrimenti.
	 */
	public final boolean isWhiteCell(byte cellCode){ return whitePositions[cellCode]==true;	}
	
	/**
	 * Ricava il numero <i>m</i> di celle percorribili dalle pedine bianche.
	 * @return l'<i>m</i> del bianco.
	 */
	public final byte getWhiteM(){ 
		byte m = (byte)(1 + ( 6 - getNumberOfWhites()));
		return m; 
	}
	
	/**
	 * Ricava il numero <i>m</i> di celle percorribili dalle pedine nere.
	 * @return l'<i>m</i> del nero.
	 */
	public final byte getBlackM(){ return (byte)(1 + ( 6 - getNumberOfBlacks())); }
	
	/**
	 * Verifica se la configurazione this è vincente per il NERO (e dunque perdente per il bianco).
	 * @return true se il nero vince, false altrimenti. 
	 */
	public final boolean isBlackWinningConfiguration(){
		return (getNumberOfBlacks()>=1 && getNumberOfWhites()==0) ||
			   (getNumberOfBlacks()==6 && getNumberOfWhites()==1);
	}
	
	/**
	 * Verifica se la configurazione this è vincente per il BIANCO (e dunque perdente per il nero).
	 * @return true se il bianco vince, false altrimenti. 
	 */
	public final boolean isWhiteWinningConfiguration(){
		return (getNumberOfWhites()>=1 && getNumberOfBlacks()==0) ||
			   (getNumberOfWhites()==6 && getNumberOfBlacks()==1);
	}
	
	@Override
	public String toString(){
		String s = "whites = ";
		for(int i=0; i<whitePositions.length; i++)
			if(whitePositions[i]==true)
				s += " "+Translator.translateByteToString((byte)i)+" ";
		s += "\nblacks = ";
		for(int i=0; i<blackPositions.length; i++)
			if(blackPositions[i]==true)
				s += " "+Translator.translateByteToString((byte)i)+" ";
		if(turn==GlobalValues.WHITE_TURN)
			s += "\nturn = WHITE";
		else
			s += "\nturn = BLACK";
		return s;
	}
	

	@Override
	public int compareTo(Object arg0) {
		BoardConfiguration otherBoard = (BoardConfiguration)arg0;
		// per avere l'ordinamento decrescente!
		return otherBoard.value - this.value;
	}
}
