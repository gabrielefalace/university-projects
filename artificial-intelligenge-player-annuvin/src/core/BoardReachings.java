package core;

import interfaces.Cell;
import interfaces.GlobalValues;

import java.util.Arrays;

/**
 * Classe statica che rappresenta le raggiungibilità da ciascuna cella della scacchiera verso tutte le 
 * altre e per ciascun livello di raggiungibilità <i>m</i> da 1 a 6.
 */
public final class BoardReachings {
	
	/**
	 * L'array a tre dimensioni in cui se l'elemento reach[m][x][y] è true allora la cella y è raggiugibile 
	 * dalla cella x percorrendo <b>al più</b> m celle adiacenti. 
	 */
	private static boolean[][][] reach = new boolean[6][GlobalValues.CELLS_NUMBER][GlobalValues.CELLS_NUMBER];
	
	/**
	 * Blocco di inizializzazione statico. Inizializza le matrici di raggiungibilità 
	 * per ogni <i>m</i> da 1 a 6. 
	 */
	static{
		// Inizializzazione a 0 (false)
		for(int i=0; i<6; i++){
			for(int j=0; j<GlobalValues.CELLS_NUMBER; j++){
				Arrays.fill(reach[i][j], false);
			}
		}
				
		// A passo 1  => matrice reach[0]
		fillReach1();
		reach[1] = fillReach(reach[0]);
		reach[2] = fillReach(reach[1]);
		reach[3] = fillReach(reach[2]);
		reach[4] = fillReach(reach[3]);
		reach[5] = fillReach(reach[4]);
		
		setNoSelfReachability();
	}
	
	/**
	 * Inizializza la matrice a raggiungibilità 1 (adiacenze dirette tra le celle).
	 */
	private static void fillReach1(){
		reach[0][Cell.A1][Cell.A2] = true;
		reach[0][Cell.A1][Cell.B1] = true;
		reach[0][Cell.A1][Cell.B2] = true;
		
		reach[0][Cell.A2][Cell.A1] = true; 
		reach[0][Cell.A2][Cell.A3] = true; 
		reach[0][Cell.A2][Cell.B2] = true; 
		reach[0][Cell.A2][Cell.B3] = true;
		
		reach[0][Cell.A3][Cell.A2] = true;
		reach[0][Cell.A3][Cell.A4] = true;
		reach[0][Cell.A3][Cell.B3] = true;
		reach[0][Cell.A3][Cell.B4] = true;
		
		reach[0][Cell.A4][Cell.A3] = true;
		reach[0][Cell.A4][Cell.B4] = true;
		reach[0][Cell.A4][Cell.B5] = true;
		
		reach[0][Cell.B1][Cell.A1] = true;
		reach[0][Cell.B1][Cell.B2] = true;
		reach[0][Cell.B1][Cell.C1] = true;
		reach[0][Cell.B1][Cell.C2] = true;
		
		reach[0][Cell.B2][Cell.B1] = true;
		reach[0][Cell.B2][Cell.B3] = true;
		reach[0][Cell.B2][Cell.A1] = true;
		reach[0][Cell.B2][Cell.A2] = true;
		reach[0][Cell.B2][Cell.C2] = true;
		reach[0][Cell.B2][Cell.C3] = true;
		
		reach[0][Cell.B3][Cell.B2] = true;
		reach[0][Cell.B3][Cell.B4] = true;
		reach[0][Cell.B3][Cell.A2] = true;
		reach[0][Cell.B3][Cell.A3] = true;
		reach[0][Cell.B3][Cell.C3] = true;
		reach[0][Cell.B3][Cell.C4] = true;
		
		reach[0][Cell.B4][Cell.A3] = true;
		reach[0][Cell.B4][Cell.A4] = true;
		reach[0][Cell.B4][Cell.B3] = true;
		reach[0][Cell.B4][Cell.B5] = true;
		reach[0][Cell.B4][Cell.C4] = true;
		reach[0][Cell.B4][Cell.C5] = true;
		
		reach[0][Cell.B5][Cell.A4] = true;
		reach[0][Cell.B5][Cell.B4] = true;
		reach[0][Cell.B5][Cell.C5] = true;
		reach[0][Cell.B5][Cell.C6] = true;
		
		reach[0][Cell.C1][Cell.B1] = true;
		reach[0][Cell.C1][Cell.C2] = true;
		reach[0][Cell.C1][Cell.D2] = true;
		reach[0][Cell.C1][Cell.D1] = true;
		
		reach[0][Cell.C2][Cell.C1] = true;
		reach[0][Cell.C2][Cell.C3] = true;
		reach[0][Cell.C2][Cell.B1] = true;
		reach[0][Cell.C2][Cell.B2] = true;
		reach[0][Cell.C2][Cell.D2] = true;
		reach[0][Cell.C2][Cell.D3] = true;
		
		reach[0][Cell.C3][Cell.C2] = true;
		reach[0][Cell.C3][Cell.C4] = true;
		reach[0][Cell.C3][Cell.B2] = true;
		reach[0][Cell.C3][Cell.B3] = true;
		reach[0][Cell.C3][Cell.D3] = true;
		reach[0][Cell.C3][Cell.D4] = true;
		
		reach[0][Cell.C4][Cell.B3] = true;
		reach[0][Cell.C4][Cell.B4] = true;
		reach[0][Cell.C4][Cell.D4] = true;
		reach[0][Cell.C4][Cell.D5] = true;
		reach[0][Cell.C4][Cell.C3] = true;
		reach[0][Cell.C4][Cell.C5] = true;
		
		reach[0][Cell.C5][Cell.B4] = true;
		reach[0][Cell.C5][Cell.B5] = true;
		reach[0][Cell.C5][Cell.C4] = true;
		reach[0][Cell.C5][Cell.C6] = true;
		reach[0][Cell.C5][Cell.D5] = true;
		reach[0][Cell.C5][Cell.D6] = true;
		
		reach[0][Cell.C6][Cell.B5] = true;
		reach[0][Cell.C6][Cell.C5] = true;
		reach[0][Cell.C6][Cell.D6] = true;
		reach[0][Cell.C6][Cell.D7] = true;
		
		reach[0][Cell.D1][Cell.C1] = true;
		reach[0][Cell.D1][Cell.D2] = true;
		reach[0][Cell.D1][Cell.E2] = true;
		
		reach[0][Cell.D2][Cell.C1] = true;
		reach[0][Cell.D2][Cell.C2] = true;
		reach[0][Cell.D2][Cell.D1] = true;
		reach[0][Cell.D2][Cell.D3] = true;
		reach[0][Cell.D2][Cell.E2] = true;
		reach[0][Cell.D2][Cell.E3] = true;
		
		reach[0][Cell.D3][Cell.C2] = true;
		reach[0][Cell.D3][Cell.C3] = true;
		reach[0][Cell.D3][Cell.D2] = true;
		reach[0][Cell.D3][Cell.D4] = true;
		reach[0][Cell.D3][Cell.E3] = true;
		reach[0][Cell.D3][Cell.E4] = true;
		
		reach[0][Cell.D4][Cell.C3] = true;
		reach[0][Cell.D4][Cell.C4] = true;
		reach[0][Cell.D4][Cell.D3] = true;
		reach[0][Cell.D4][Cell.D5] = true;
		reach[0][Cell.D4][Cell.E4] = true;
		reach[0][Cell.D4][Cell.E5] = true;
		
		reach[0][Cell.D5][Cell.C4] = true;
		reach[0][Cell.D5][Cell.C5] = true;
		reach[0][Cell.D5][Cell.D4] = true;
		reach[0][Cell.D5][Cell.D6] = true;
		reach[0][Cell.D5][Cell.E5] = true;
		reach[0][Cell.D5][Cell.E6] = true;
		
		reach[0][Cell.D6][Cell.C5] = true;
		reach[0][Cell.D6][Cell.C6] = true;
		reach[0][Cell.D6][Cell.D5] = true;
		reach[0][Cell.D6][Cell.D7] = true;
		reach[0][Cell.D6][Cell.E6] = true;
		reach[0][Cell.D6][Cell.E7] = true;
		
		reach[0][Cell.D7][Cell.C6] = true;
		reach[0][Cell.D7][Cell.D6] = true;
		reach[0][Cell.D7][Cell.E7] = true;
		
		reach[0][Cell.E2][Cell.D1] = true;
		reach[0][Cell.E2][Cell.D2] = true;
		reach[0][Cell.E2][Cell.E3] = true;
		reach[0][Cell.E2][Cell.F3] = true;
		
		reach[0][Cell.E3][Cell.D2] = true;
		reach[0][Cell.E3][Cell.D3] = true;
		reach[0][Cell.E3][Cell.E2] = true;
		reach[0][Cell.E3][Cell.E4] = true;
		reach[0][Cell.E3][Cell.F3] = true;
		reach[0][Cell.E3][Cell.F4] = true;
		
		reach[0][Cell.E4][Cell.D3] = true;
		reach[0][Cell.E4][Cell.D4] = true;
		reach[0][Cell.E4][Cell.E3] = true;
		reach[0][Cell.E4][Cell.E5] = true;
		reach[0][Cell.E4][Cell.F4] = true;
		reach[0][Cell.E4][Cell.F5] = true;
		
		reach[0][Cell.E5][Cell.D4] = true;
		reach[0][Cell.E5][Cell.D5] = true;
		reach[0][Cell.E5][Cell.E4] = true;
		reach[0][Cell.E5][Cell.E6] = true;
		reach[0][Cell.E5][Cell.F5] = true;
		reach[0][Cell.E5][Cell.F6] = true;
		
		reach[0][Cell.E6][Cell.D5] = true;
		reach[0][Cell.E6][Cell.D6] = true;
		reach[0][Cell.E6][Cell.E5] = true;
		reach[0][Cell.E6][Cell.E7] = true;
		reach[0][Cell.E6][Cell.F6] = true;
		reach[0][Cell.E6][Cell.F7] = true;
		
		reach[0][Cell.E7][Cell.D6] = true;
		reach[0][Cell.E7][Cell.D7] = true;
		reach[0][Cell.E7][Cell.E6] = true;
		reach[0][Cell.E7][Cell.F7] = true;
		
		reach[0][Cell.F3][Cell.E2] = true;
		reach[0][Cell.F3][Cell.E3] = true;
		reach[0][Cell.F3][Cell.F4] = true;
		reach[0][Cell.F3][Cell.G4] = true;
		
		reach[0][Cell.F4][Cell.E3] = true;
		reach[0][Cell.F4][Cell.E4] = true;
		reach[0][Cell.F4][Cell.F3] = true;
		reach[0][Cell.F4][Cell.F5] = true;
		reach[0][Cell.F4][Cell.G4] = true;
		reach[0][Cell.F4][Cell.G5] = true;
		
		reach[0][Cell.F5][Cell.E4] = true;
		reach[0][Cell.F5][Cell.E5] = true;
		reach[0][Cell.F5][Cell.F4] = true;
		reach[0][Cell.F5][Cell.F6] = true;
		reach[0][Cell.F5][Cell.G5] = true;
		reach[0][Cell.F5][Cell.G6] = true;
		
		reach[0][Cell.F6][Cell.E5] = true;
		reach[0][Cell.F6][Cell.E6] = true;
		reach[0][Cell.F6][Cell.F5] = true;
		reach[0][Cell.F6][Cell.F7] = true;
		reach[0][Cell.F6][Cell.G6] = true;
		reach[0][Cell.F6][Cell.G7] = true;
		
		reach[0][Cell.F7][Cell.E6] = true;
		reach[0][Cell.F7][Cell.E7] = true;
		reach[0][Cell.F7][Cell.F6] = true;
		reach[0][Cell.F7][Cell.G7] = true;
		
		reach[0][Cell.G4][Cell.F3] = true;
		reach[0][Cell.G4][Cell.F4] = true;
		reach[0][Cell.G4][Cell.G5] = true;
		
		reach[0][Cell.G5][Cell.G4] = true;
		reach[0][Cell.G5][Cell.G6] = true;
		reach[0][Cell.G5][Cell.F4] = true;
		reach[0][Cell.G5][Cell.F5] = true;
		
		reach[0][Cell.G6][Cell.G5] = true;
		reach[0][Cell.G6][Cell.G7] = true;
		reach[0][Cell.G6][Cell.F5] = true;
		reach[0][Cell.G6][Cell.F6] = true;
		
		reach[0][Cell.G7][Cell.G6] = true;
		reach[0][Cell.G7][Cell.F6] = true;
		reach[0][Cell.G7][Cell.F7] = true;
	}

	/**
	 * 
	 */
	private static void setNoSelfReachability() {
		for(int i=0; i<6; i++){
			for(int j=0; j<GlobalValues.CELLS_NUMBER; j++)
				reach[i][j][j] = false;
		}
	}

	/**
	 * Consente l'inizializzazione  delle matrici di raggiungibilità, a passo <i>m > 1</i>. 
	 * La matrice delle raggiungibilità a passo k viene costruita progressivamente a partire da quella a 
	 * passo k-1, combinandola con quella di passo 1.   
	 * @param reachK_1 la matrice di raggiungibilità a passo k-1.
	 * @return la matrice delle raggiungibilità a passo k.
	 */
	private static boolean[][] fillReach(boolean[][] reachK_1){
		boolean[][] reachK = new boolean[GlobalValues.CELLS_NUMBER][GlobalValues.CELLS_NUMBER];
		for(int i=0; i<GlobalValues.CELLS_NUMBER; i++){
			for(int j=0; j<GlobalValues.CELLS_NUMBER; j++){
				for(int h=0; h<GlobalValues.CELLS_NUMBER; h++){
					if(reach[0][i][j] == true && reachK_1[j][h] == true)
						reachK[i][h] = true;
				}
			}
		}
		return  reachK;
	}

	/**
	 * Consente di accedere all'intera matrice di raggiungibilità a passo 'step'.
	 * @param step il numero massimo di adiacenze percorribili.
	 * @return la matrice di boolean che rappresenta le raggiungibilità.
	 */
	public static boolean[][] getReachingsMap(byte step){
		return reach[step-1];
	}
	
	/**
	 * Consente di accedere ad un array caratteristico contenente le adiacenze di una data cella.
	 * @param from la cella di origine.
	 * @param step il numero massimo di adiacenze percorribili.
	 * @return il vettore caratteristico della raggiungibilità.
	 */
	public static boolean[] getReachableCells(byte from, byte step){
		return reach[step-1][from];
	}
	
	/**
	 * Verifica che una cella sia raggiungibile da un altra in un determinato numero di adiacenze.
	 * @param source la cella di partenza.
	 * @param dest la cella da raggiungere.
	 * @param step il numero di passi massimo.
	 * @return true se dest è raggiungibile da source con 'step' adiacenze, false altrimenti.
	 */
	public static boolean isReachable(byte source, byte dest, byte step){
		return reach[step-1][source][dest];
	}
	
	/**
	 * Consente di ottenere l'm minimo per raggiungere una cella a partire da un'altra, 
	 * nel rispetto dell'm max a disposizione.
	 * @param source la cella di partenza.
	 * @param dest la cella da raggiungere.
	 * @param m_max il massimo numero consentito di passi.
	 * @return il minimo numero di passi necessari.
	 */
	public static byte getMinimumReachability(byte source, byte dest, byte m_max){
		for(byte i=1; i<=m_max; i++)
			if(isReachable(source, dest, i))
				return i;
		throw new RuntimeException("il valore di m (passato da parametro) non è valido");
	}
	
	/**
	 * Consente di ottenere la distanza esatta tra due celle.
	 * @param aCell il codice di una cella.
	 * @param anotherCell il codice dell'altra cella.
	 * @return la distaza tra le celle.
	 */
	public static byte getDistanceBetween(byte aCell, byte anotherCell){
		return getMinimumReachability(aCell, anotherCell, (byte)6);
	}
	
	/**
	 * Consente di stampare a console una data matrice di raggiungibilità.
	 * @param matrixIndex il passo per cui si vuole stampare la matrice.
	 */
	@SuppressWarnings("unused")
	private static void printReach(int matrixIndex){
		boolean[][] reach2 = reach[matrixIndex];
		System.out.print("   ");
		for(int i=0; i<GlobalValues.CELLS_NUMBER; i++){
			String cellField = Cell.class.getFields()[i].toString();
			String[] cellToken = cellField.split("\\.");
			String cellName = cellToken[2];
			System.out.print(cellName+" ");
		}
		System.out.println();
	
		for(int i=0; i<GlobalValues.CELLS_NUMBER; i++){
			String cellField = Cell.class.getFields()[i].toString();
			String[] cellToken = cellField.split("\\.");
			String cellName = cellToken[2];
			System.out.print(cellName+" ");
			for(int j=0; j<GlobalValues.CELLS_NUMBER; j++){
				
				if(reach2[i][j] == true)
					System.out.print(1+"  ");
				else
					System.out.print(0+"  ");
			}
			System.out.println();
		}
	}
}
