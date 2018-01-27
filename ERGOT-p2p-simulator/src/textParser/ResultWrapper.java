package textParser;

import java.util.LinkedList;

public class ResultWrapper{
	
	LinkedList<Float> timedMessages = new LinkedList<Float>();
	LinkedList<Float> timedServices = new LinkedList<Float>();
	LinkedList<Float> timedRecall   = new LinkedList<Float>();
	
	double[] recall_dev, messages_dev, services_dev;
	
	public ResultWrapper(){	}
	
	public String toString(){
		String s = "ResultWrapper:\n";
		for(int i=0; i<timedRecall.size(); i++){
			s += i+" "+timedServices.get(i)+" "+services_dev[i]+"  "+timedMessages.get(i)+" "+messages_dev[i]+" "+timedRecall.get(i)+" "+recall_dev[i]+"\n";
		}
		return s;
	}
	
	
	public static void main(String[] args){
		ResultWrapper r = new ResultWrapper();
		r.timedMessages.addLast(new Float(4));
		r.timedMessages.addLast(new Float(3));
		r.timedMessages.addLast(new Float(2));
		r.timedServices.addLast(new Float(4));
		r.timedServices.addLast(new Float(3));
		r.timedServices.addLast(new Float(2));
		r.timedRecall.addLast(new Float(4));
		r.timedRecall.addLast(new Float(3));
		r.timedRecall.addLast(new Float(2));
		
		System.out.println(r);
	}
	
	
}