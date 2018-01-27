package core;

import java.util.HashMap;

public class OpeningWhiteMove {
	
	private static HashMap<Byte, Byte> sources = new HashMap<Byte, Byte>();
	private static HashMap<Byte, Byte> destinations = new HashMap<Byte, Byte>();
	

	static{
		sources.put((byte)1, Translator.translateStringToByte("B1"));
		sources.put((byte)2, Translator.translateStringToByte("F3"));
		sources.put((byte)3, Translator.translateStringToByte("D2"));
		sources.put((byte)4, Translator.translateStringToByte("C1"));
		sources.put((byte)5, Translator.translateStringToByte("E2"));
		sources.put((byte)6, Translator.translateStringToByte("D1"));
		
		destinations.put((byte)1, Translator.translateStringToByte("B2"));
		destinations.put((byte)2, Translator.translateStringToByte("F4"));
		destinations.put((byte)3, Translator.translateStringToByte("D3"));
		destinations.put((byte)4, Translator.translateStringToByte("C2"));
		destinations.put((byte)5, Translator.translateStringToByte("E3"));
		destinations.put((byte)6, Translator.translateStringToByte("C1"));
	}

	public static byte getSources(byte src){
		return sources.get(src);
	}
	
	public static byte getDestinations(byte dest){
		return destinations.get(dest);
	}
}
