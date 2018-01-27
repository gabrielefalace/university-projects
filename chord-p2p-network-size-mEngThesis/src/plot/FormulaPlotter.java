package plot;

import java.util.Scanner;

public class FormulaPlotter {
	
	public static double[] computeValues(int n, int m){
		double prob[] = new double[m+1];
		prob[0] = 0;
		
		for(int u=1; u<=m; u++){
			double share[] = new double[u];
			double fact[] = new double[u];
			double staticProb[] = new double[u];
			
			for(int i=0; i<u; i++)
				staticProb[i] = Math.pow(0.5, i+1);
			
			double total = AggregateFunctions.sum(staticProb);
//			System.out.println("totale calcolato = "+total);
			
			for(int i=0; i<u; i++){
				share[i] = (n-1)*staticProb[i]/total;
				fact[i] = Factorial.computeDouble(share[i]);
//				System.out.println("sP("+i+") = "+staticProb[i]+" -> quota = "+share[i]+" fact = "+fact[i]);
			}
				
			double productP = 1.0;
			for(int i=0; i<u; i++)
					productP *= Math.pow(staticProb[i], share[i]);
			
			double productF = AggregateFunctions.product(fact);
			
			prob[u] = Factorial.computeDouble(n-1)*productP/productF;
		}
		
//		return prob;
		return AggregateFunctions.normalize(prob);
	}
	
	
	// MAIN di prova
	public static void main(String[] args){
		Scanner s = new Scanner(System.in);
		while(true){
			System.out.println("digita n:");
			String line = s.nextLine();
			if(line.equals("exit"))
				return;
			System.out.println("digita m:");
			String line2 = s.nextLine();
			if(line2.equals("exit"))
				return;
			int n = Integer.parseInt(line);
			int m = Integer.parseInt(line2);
			
			double res[] = computeValues(n, m);
			for(int u=0; u<res.length; u++)
				System.out.println("P("+u+") = "+res[u]);
			
			System.out.print("type 'exit' to quit ... \n");
		}
	}
}
