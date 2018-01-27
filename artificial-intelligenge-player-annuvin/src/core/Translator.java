package core;

import java.util.HashMap;


/**
 * Classe che permette di tradurre nomi di cella (stringhe) in codici di cella.
 */
public class Translator {

	/**
	 * array per la corrispondenza codice-nome
	 */
	private static String[] byteToString = new String[37];
	
	/**
	 * mappa per la corrispondenza nome-codice
	 */
	private static HashMap<String, Byte> stringToByte = new HashMap<String, Byte>();
	
	/**
	 * Inizializza le mappe delle corrispondenze.
	 */
	static{
		byteToString[0] = "A1";
		byteToString[1] = "A2";
		byteToString[2] = "A3";
		byteToString[3] = "A4";
		
		byteToString[4] = "B1";
		byteToString[5] = "B2";
		byteToString[6] = "B3";
		byteToString[7] = "B4";
		byteToString[8] = "B5";
		
		byteToString[9] = "C1";
		byteToString[10] = "C2";
		byteToString[11] = "C3";
		byteToString[12] = "C4";
		byteToString[13] = "C5";
		byteToString[14] = "C6";
		
		byteToString[15] = "D1";
		byteToString[16] = "D2";
		byteToString[17] = "D3";
		byteToString[18] = "D4";
		byteToString[19] = "D5";
		byteToString[20] = "D6";
		byteToString[21] = "D7";
		
		byteToString[22] = "E2";
		byteToString[23] = "E3";
		byteToString[24] = "E4";
		byteToString[25] = "E5";
		byteToString[26] = "E6";
		byteToString[27] = "E7";
		
		byteToString[28] = "F3";
		byteToString[29] = "F4";
		byteToString[30] = "F5";
		byteToString[31] = "F6";
		byteToString[32] = "F7";
		
		byteToString[33] = "G4";
		byteToString[34] = "G5";
		byteToString[35] = "G6";
		byteToString[36] = "G7";
		
		//inizializzazione di stringToByte
		for(int i=0; i<byteToString.length; i++){
			stringToByte.put(byteToString[i], (byte)i);
		}
	}
	
	/**
	 * Effettua la traduzione da nome di cella a codice di cella.
	 * @param cellName il nome della cella.
	 * @return il codice della cella.
	 */
	public static byte translateStringToByte(String cellName){
		if(stringToByte.containsKey(cellName))
			return stringToByte.get(cellName);
		else
			return -1;
	}
	
	/**
	 * Effettua la traduzione da codice di cella a nome di cella.
	 * @param cellCode il codice della cella.
	 * @return cellName il nome della cella.
	 */
	public static String translateByteToString(byte cellCode){
		return byteToString[cellCode];
	}	
}
