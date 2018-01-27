package common;

/**
 * Classe di utilità che consente di controllare le sottostringhe di una stringa. 
 */
public class TextMatcher {

	/**
	 * Verifica che una data parola sia presente all'interno di un certo testo.
	 * @param word la stringa di cui si vuole verificare la presenza.
	 * @param text la stringa in cui cercare.
	 * @param caseSensitive se true è sensibile a maiuscole/minuscole.
	 * @return true se il testo contiene la parola, false altrimenti.
	 */
	public boolean contains(String word, String text, boolean caseSensitive){
		String w = word;
		String t = text;
		if(caseSensitive==false){
			w = word.toLowerCase();
			t = text.toLowerCase();
		}
		boolean res = false;
		StringBuffer s = new StringBuffer(w);
		CharSequence cs = s.subSequence(0, s.length());
		if(t.contains(cs))
			res = true;
		return res;
	}
	
}
