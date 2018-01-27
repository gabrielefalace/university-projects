package plot;

import java.util.Scanner;

public class Factorial {

	/**
	 * Calcola il fattoriale di numeri anche molto grandi. se i numeri sono abbastanza piccoli il 
	 * fattoriale calcolato sarà esatto. Calcola anche il fattoriale per numeri non interi (virgola mobile). 
	 * @param n Il numero di cui si vuole calcolare il fattoriale.
	 * @return Il fattoriale in notazione scientifica, con le prime 5 cifre significative.
	 */
	public static String compute(double n){
			double ex = 0.00;
			double x = n;
			x = x + x + 1;
			
			if(x > 1){
				x = (Math.log(2.0*Math.PI) + Math.log(x/2.0)*x -x - (1.0 - 7.0/(30.0*x*x))/(6.0*x))/2.0;
				x = x/Math.log(10);
				ex = Math.floor(x);
				x = Math.pow(10, x-ex);
			}
			
			String res = x+"";
			return res.substring(0, 6) +"E"+ ex;
	}
	
	/**
	 * Calcola il fattoriale di numeri anche molto grandi. se i numeri sono abbastanza piccoli il 
	 * fattoriale calcolato sarà esatto. Calcola anche il fattoriale per numeri non interi (virgola mobile). 
	 * @param n Il numero di cui si vuole calcolare il fattoriale.
	 * @return Il fattoriale come numero in virgola mobile doppia precisione (double).
	 */
	public static double computeDouble(double n){
		double ex = 0.00;
		double x = n;
		x = x + x + 1;
		
		if(x > 1){
			x = (Math.log(2.0*Math.PI) + Math.log(x/2.0)*x -x - (1.0 - 7.0/(30.0*x*x))/(6.0*x))/2.0;
			x = x/Math.log(10);
			ex = Math.floor(x);
			x = Math.pow(10, x-ex);
		}
		
		return  x*Math.pow(10, ex);
	}

	
	// MAIN di prova
	public static void main(String[] args){
		Scanner s = new Scanner(System.in);
		while(true){
			System.out.println("digita argomento:");
			String line = s.nextLine();
			if(line.equals("exit"))
				return;
			Double n = Double.parseDouble(line);
			System.out.println(compute(n));
			try{
				System.out.println(computeDouble(n));
			}
			catch(Exception err){
				System.out.println(err);
			}
		}
	}
	
}
