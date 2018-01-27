package textParser;

import java.util.ArrayList;
import semanticOverlayNetwork.*;


public class DeviationComputer {

	
	public double[] getServicesDeviation(ArrayList<ResultWrapper> results, float[] services_report){
		double[] deviations = new double[services_report.length];
		for(int currentHop=0; currentHop < services_report.length; currentHop++){
			double mean_sq = Math.pow(services_report[currentHop], 2);
			double N = 0;
			double sum = 0;
			for(int i=0; i<results.size(); i++){
				if(results.get(i).timedServices.size() > currentHop){
					double tmp = results.get(i).timedServices.get(currentHop);
					double term = Math.pow(tmp, 2) - mean_sq ;
					sum += term;
					N++;
				}
			}
			deviations[currentHop] = Math.sqrt(sum/N);
		}
		return deviations;
	}
	
	
	public double[] getMessagesDeviation(ArrayList<ResultWrapper> results, float[] message_report){
		double[] deviations = new double[message_report.length];
		for(int currentHop=0; currentHop < message_report.length; currentHop++){
			double mean_sq = Math.pow(message_report[currentHop], 2);
			double N = 0;
			double sum = 0;
			for(int i=0; i<results.size(); i++){
				if(results.get(i).timedMessages.size() > currentHop){
					double tmp = results.get(i).timedMessages.get(currentHop);
					double term = Math.pow(tmp, 2) - mean_sq ;
					sum += term;
					N++;
				}
			}
			deviations[currentHop] = Math.sqrt(sum/N);
		}
		return deviations;
	}
	
	public double[] getRecallDeviation(ArrayList<ResultWrapper> results, float[] recall_report){
		double[] deviations = new double[recall_report.length];
		for(int currentHop=0; currentHop < recall_report.length; currentHop++){
			double mean_sq = Math.pow(recall_report[currentHop], 2);
			double N = 0;
			double sum = 0;
			for(int i=0; i<results.size(); i++){
				if(results.get(i).timedRecall.size() > currentHop){
					double tmp = results.get(i).timedRecall.get(currentHop);
					double term = Math.pow(tmp, 2) - mean_sq ;
					sum += term;
					N++;
				}
			}
			deviations[currentHop] = Math.sqrt(sum/N);
		}
		return deviations;
	}
}
