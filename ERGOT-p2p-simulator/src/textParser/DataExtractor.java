package textParser;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class DataExtractor {

	
	public static void main(String[] args){		
		
		FileReader fr = null;
		BufferedReader reader = null;
		
		LinkedList<ResultWrapper> result_list = new LinkedList<ResultWrapper>();
		
		try{
			int SERVICES = 1, MESSAGES = 2, RECALL = 3;
			
			fr = new FileReader(new File("c:\\users\\gabriele\\desktop\\ERGOT Simulation RUNS\\old - wrong\\round 5.txt"));
			reader = new BufferedReader(fr);
			
			ResultWrapper res = new ResultWrapper();
			
			while(true){
				
				String line = reader.readLine();
				
				
				if(line.equals("EOF")){ 
					// va inserito l'ultimo risultato!
					result_list.addLast(res);
					break;
				}
				
				if(line.startsWith("hop")){
					String cond0 = "hop:\\s*\\d*\\s*-\\s*trovati\\s*";
					String cond1 = "\\s*servizi\\s*-\\s*sono\\s*in\\s*giro"; 
					String cond2 = "\\s*messaggi\\s*-\\s*il\\s*recall\\s*attuale\\s*è\\s*";
					String regex = cond0+"|"+cond1+"|"+cond2;
					String data[] = line.split(regex);
					
					for(String s: data)
						s = s.trim();
					
					// L'INDICE 0 HA UNA STRINGA VUOTA!!!
					float serv_tmp = Float.parseFloat(data[SERVICES].trim());
					float mess_tmp = Float.parseFloat(data[MESSAGES].trim());
					float rec_tmp = Float.parseFloat(data[RECALL].trim());

					res.timedServices.addLast(new Float(serv_tmp));
					res.timedMessages.addLast(new Float(mess_tmp));
					res.timedRecall.addLast(new Float(rec_tmp));
				}
				else if(line.startsWith("Lookup")){
					result_list.addLast(res);					
					res = new ResultWrapper();
				}
				else
					continue;
			}
			result_list.removeFirst();
			
			//System.out.println("Risultati : "+result_list.size());
			
			//for(ResultWrapper r : result_list)
				//System.out.println(r.toString());
			
			/*
			 * 
			 *  CALCOLO DELLE MEDIE
			 *  
			 */
			
			ResultWrapper report = new ResultWrapper();
			
			int maxHop = 0;
			
			for(int i=0; i<result_list.size(); i++){
				if(result_list.get(i).timedMessages.size() > maxHop)
					maxHop = result_list.get(i).timedMessages.size();
			}
			
			//System.out.println("Max hop = "+maxHop);

			
			for(int currentHop=0; currentHop < maxHop; currentHop++){
				
				float totalMessages = 0;
				float ok_message = 0;
				
				float totalServices = 0;
				float ok_service = 0;
				
				float totalRecall = 0;
				float ok_recall = 0;

				for(int k=0; k<result_list.size(); k++){
					
					ResultWrapper result = result_list.get(k);
					
					if(result.timedMessages.size() > currentHop){
						totalMessages += result.timedMessages.get(currentHop);
						ok_message++;
					}
					if(result.timedServices.size() > currentHop){
						totalServices += result.timedServices.get(currentHop);
						ok_service++;
					}
					if(result.timedRecall.size() > currentHop){
						totalRecall += result.timedRecall.get(currentHop);
						ok_recall++;
					}
				}
				report.timedMessages.addLast(new Float(totalMessages/ok_message));
				report.timedServices.addLast(new Float(totalServices/ok_service));
				report.timedRecall.addLast(new Float(totalRecall/ok_recall));
			}
			
			DeviationComputer comp = new DeviationComputer();
			
			ArrayList<ResultWrapper> result_array = new ArrayList<ResultWrapper>();
			result_array.addAll(result_list);
			
			float[] message_report = new float[report.timedMessages.size()];
			for(int j=0; j<message_report.length; j++)
				message_report[j] = report.timedMessages.get(j);
			
			float[] services_report = new float[report.timedServices.size()];
			for(int j=0; j<services_report.length; j++)
				services_report[j] = report.timedServices.get(j);
			
			float[] recall_report = new float[report.timedRecall.size()];
			for(int j=0; j<recall_report.length; j++)
				recall_report[j] = report.timedRecall.get(j);
			
			report.messages_dev = comp.getMessagesDeviation(result_array, message_report);
			report.services_dev = comp.getServicesDeviation(result_array, services_report);
			report.recall_dev = comp.getRecallDeviation(result_array, recall_report);
			
			
			System.out.println(report);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(fr!=null) fr.close();
				if(reader!=null) reader.close();
			}
			catch(Exception err){
				err.printStackTrace();
			}
		}
		
	}
	
}
