package robot;

import basic.Log;
import basic.Map;
import basic.Position;

import java.util.LinkedList;
import java.util.Random;
import jade.core.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;


public class Robot extends Agent{

	private int row;
	private int col;
	Position initialPosition;
	Position currentPosition;
	AID[] otherAgents;
	private Map map;
	int id;
	
	Log log;
	LinkedList<Double> distances_from_initial;
	
	protected void setup(){
		
		Random r = new Random();
		
		row = r.nextInt(100);
		col = r.nextInt(100);
				
		initialPosition = new Position(row, col);
		currentPosition = initialPosition;
		
		map = (Map)(getArguments()[0]);
		log = (Log)(getArguments()[2]);
		
		String tmp = getLocalName().split("@")[0];
		id = Integer.parseInt(tmp.split("t")[1]);
		
		map.setRobot(id, row, col);
		
		distances_from_initial = new LinkedList<Double>();
		
		//registra presso il DF
	
		DFAgentDescription desc = new DFAgentDescription();
		desc.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Robot");
		sd.setName(this.getName());	
		desc.addServices(sd);
		try{
			DFService.register(this, desc);
		}
		catch(FIPAException fipa){
			fipa.printStackTrace();
		}
		
		//aspetta 7 secondi
		try{
			Thread.sleep(7000);
		}
		catch(Exception err){
			err.printStackTrace();
		}
		
		// Contatta gli altri agenti
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription description = new ServiceDescription();
		description.setType("Robot");
		template.addServices(description);
		try{
			DFAgentDescription[] result = DFService.search(this, template);
			otherAgents = new AID[result.length-1];
			int j = 0;
			for(int i=0; i<result.length; i++){
				if(result[i].getName().getLocalName().equals(this.getLocalName()))
					continue;
				else{
					otherAgents[j] = result[i].getName();
					j++;
				}
			}
		}
		catch(Exception err){
			err.printStackTrace();
		}
		
		
		String agentType = (String)getArguments()[1];
		if(agentType.equals("NAIVE"))
			addBehaviour(new GreedyBehaviour(this));
		if(agentType.equals("CLUSTERING"))
			addBehaviour(new ClusteredBehaviour(this));
		if(agentType.equals("INTELLIGENT"))
			addBehaviour(new IntelligentBehaviour(this));
		
		log.write(id, " ho contattato "+otherAgents.length+" altri agenti");
	}
	
	protected void takeDown(){
		try{			
			DFService.deregister(this);
		}
		catch(Exception err){
			err.printStackTrace();
		}
	}
	
	/**
	 * Calcola la distanza euclidea tra due punti. 
	 * @param p1
	 * @param p2
	 * @return
	 */
	double distance(Position p1, Position p2){
		double x1 = p1.col;
		double x2 = p2.col;
		double y1 = 100 - p1.row;
		double y2 = 100 - p2.row;
		
		double deltaX = Math.abs(x1 - x2);
		double deltaY = Math.abs(y1 - y2);
		
		double sumOfSquare = (deltaX*deltaX) + (deltaY*deltaY);
		return Math.sqrt(sumOfSquare);
	}
	
	Position getInitialPosition(){
		return initialPosition;
	}

	Position getCurrentPosition(){
		return currentPosition;
	}
	
	Map getMap(){
		return map;
	}
	
	AID[] getOtherActiveAgents(){
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription description = new ServiceDescription();
		description.setType("Robot");
		template.addServices(description);
		try{
			DFAgentDescription[] result = DFService.search(this, template);
			otherAgents = new AID[result.length-1];
			int j = 0;
			for(int i=0; i<result.length; i++){
				if(result[i].getName().getLocalName().equals(this.getLocalName()))
					continue;
				else{
					otherAgents[j] = result[i].getName();
					j++;
				}
			}
		}
		catch(Exception err){
			err.printStackTrace();
		}
		return otherAgents;
	}
	
	
	/**
	 * 
	 * @param p
	 * @return
	 */
	public int getBatteryDecreasing(Position p){
		double d = distance(getCurrentPosition(), p);
		return (int)Math.round(d/10);
	}
	
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	public Position getNearestToInitialPosition(LinkedList<Position> list){
		Position bestPos = list.getFirst();
		double currentDist = distance(getInitialPosition(), bestPos);
		for(int i=0; i<list.size(); i++){
			Position currPos = list.get(i);
			double dist = distance(currPos, getInitialPosition());
			if(dist < currentDist){
				currentDist = dist;
				bestPos = currPos;
			}
		}
		return bestPos;
	}
	
	

	/**
	 * Calcola il sito più vicino alla posizione in cui si trova il robot attualmente.
	 * @param list la lista dei siti in cui il robot può muoversi.
	 * @return il sito più vicino.
	 */
	public Position getNearestToCurrentPosition(LinkedList<Position> list){
		
		Position bestPos = list.getFirst();
		double currentDist = distance(getCurrentPosition(), bestPos);
		
		for(int i=0; i<list.size(); i++){
			Position listElement = list.get(i);
			double dist = distance(listElement, getCurrentPosition());
			if(dist < currentDist){
				currentDist = dist;
				bestPos = listElement;
			}
		}
		
		return bestPos;
	}

	
}
