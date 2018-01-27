package core;

import java.util.HashMap;

public class OpeningBlackMove {

	private static HashMap<Byte, Byte> sources = new HashMap<Byte, Byte>();
	private static HashMap<Byte, Byte> destinations = new HashMap<Byte, Byte>();
	

	static{
		sources.put((byte)1, Translator.translateStringToByte("F7"));
		sources.put((byte)2, Translator.translateStringToByte("B5"));
		sources.put((byte)3, Translator.translateStringToByte("D6"));
		sources.put((byte)4, Translator.translateStringToByte("C6"));
		sources.put((byte)5, Translator.translateStringToByte("E7"));
		sources.put((byte)6, Translator.translateStringToByte("D7"));
		
		destinations.put((byte)1, Translator.translateStringToByte("F6"));
		destinations.put((byte)2, Translator.translateStringToByte("B4"));
		destinations.put((byte)3, Translator.translateStringToByte("D5"));
		destinations.put((byte)4, Translator.translateStringToByte("C5"));
		destinations.put((byte)5, Translator.translateStringToByte("E6"));
		destinations.put((byte)6, Translator.translateStringToByte("E7"));
	}

	public static byte getSources(byte src){
		return sources.get(src);
	}
	
	public static byte getDestinations(byte dest){
		return destinations.get(dest);
	}
}
