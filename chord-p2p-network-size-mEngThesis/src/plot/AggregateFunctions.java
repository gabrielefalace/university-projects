package plot;

public class AggregateFunctions {

	/**
	 * Calcola la somma degli elementi di un vettore.
	 * @param terms il vettore dei termini da sommare.
	 * @return la somma dei termini.
	 */
	public static double sum(double[] terms){
		double res = 0.0;
		for(int i=0; i<terms.length; i++)
			res += terms[i];
		return res;
	}
	
	/**
	 * Effettua la produttoria di un vettore di fattori.
	 * @param factors i fattori da moltiplicare tra loro.
	 * @return il prodotto dei fattori.
	 */
	public static double product(double[] factors){
		double res = 1.0;
		for(double factor: factors)
			res *= factor;
		return res;
	}
	
	/**
	 * Normalizza degli elementi rispetto al loro totale.
	 * @param input gli elementi da normalizzare.
	 * @return gli elementi normalizzati.
	 */
	public static double[] normalize(double[] input){
		double out[] = new double[input.length];
		double total = 0.0;
		for(double element: input)
			total += element;
		for(int i=0; i<out.length; i++)
			out[i] = input[i]/total;
		return out;
	}
}
