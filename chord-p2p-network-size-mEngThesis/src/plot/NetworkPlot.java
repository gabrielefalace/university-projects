package plot;

import java.util.LinkedList;
import java.util.Scanner;

import core.ChordNetwork;
import core.NetworkFunctions;


/**
 * Classe per l'esperimento di plot della distribuzione concreta sulla rete.
 * @author Gabriele
 *
 */
public class NetworkPlot {

	public static int[] seeds = {11, 27, 34, 331, 789, 876, 1021, 2345, 2999, 3451, 
							     2374, 4848, 42346, 54373, 23757, 55678, 15267, 14235, 274, 1111,
							     23586, 54697, 113, 55, 91, 9786, 33311, 45600, 1, 102938485,
							     2766, 212121, 234523, 120987, 1231231, 129, 1114680, 707070, 808080, 190190};
	
	public static void main(String[] args){
		// Get parameter from user
		Scanner scanner = new Scanner(System.in);
		System.out.println("Digita n");
		int peerNumber = scanner.nextInt();
		
		System.out.println("Digita m");
		int bits = scanner.nextInt();
		
	
		
		LinkedList<int[]> simResultsBucket = new LinkedList<int[]>();
		
		for(int i=0; i<seeds.length; i++){
			// doing i-th simulation
			
			ChordNetwork chord = new ChordNetwork(bits, peerNumber, seeds[i]);
			NetworkFunctions netFunctions = new NetworkFunctions(chord);
			
			int fingers[] = new int[bits+1];
			fingers[0] = 0;
			for(int j=1; j<=bits; j++)
				fingers[j] = netFunctions.countPeerWithFingers(j);
			simResultsBucket.add(fingers);
		}
		
		double averages[] = new double[bits+1];
		averages[0] = 0.0;
		
		for(int k=1; k<=bits; k++){
			for(int[] simResult: simResultsBucket){
				averages[k] += simResult[k];
			}
		}
		
		for(int k=1; k<=bits; k++){
			averages[k] = averages[k]/seeds.length;
		}
		
		for(int k=0; k<=bits; k++){
			System.out.println(k+" - "+averages[k]);
		}
	}
	
}
