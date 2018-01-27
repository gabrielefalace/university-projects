package robot;

import basic.*;

import java.util.HashSet;
import java.util.LinkedList;
import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class IntelligentBehaviour extends Behaviour{

	Robot robot;
	LinkedList<Position> listaPos;
	Map map;
	boolean done = false;
	
	
	public IntelligentBehaviour(Robot robot){
		super();
		this.robot = robot;
		map = robot.getMap();
	}
	
	public void action(){
		try{
			try{Thread.sleep(600);}
			catch(Exception err){err.printStackTrace();}

			// Rispondiamo agli altri
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
			ACLMessage req = robot.blockingReceive(mt, 2000);
			if(req!=null){
				String[] tokens = req.getContent().split(":");
				Position site = new Position(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
				double itsDist = Double.parseDouble(tokens[0]);
				double myDist = robot.distance(robot.getInitialPosition(), site);
				if(myDist < itsDist){
					ACLMessage reply = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
					reply.addReceiver(req.getSender());
					reply.setContent("YES");	// inutile
					robot.send(reply);
				}
				return;
			}
			
			listaPos = new LinkedList<Position>();
			listaPos.addAll(map.getUnacquiredSites());
						
			robot.log.write(robot.id, " - ricevuta una lista con "+listaPos.size()+" siti");
			
			if(listaPos.isEmpty()){
				done = true;
				return;
			}
			
			Position p = robot.getNearestToInitialPosition(listaPos);
			
			// Ci arrivo con la batteria? Altrimenti chiudo!			
			int newLevel =  100 - batteryConsumptionSoFar();
			
			if(newLevel < 0){
				robot.log.write(robot.id, "Finita la batteria!");
				robot.getMap().setFinished(robot.id);
				done = true;
				
				return;
			}

			// C'è qualcuno più vicino?
			ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
			robot.log.write(robot.id, " - Chiedo se c'e qualcuno piu vicino per "+p);
			String content = ""+robot.distance(p, robot.getInitialPosition())+":"+p.row+":"+p.col;
			msg.setContent(content);
			AID[] otherActive = robot.getOtherActiveAgents(); 
			for(AID aid: otherActive)
				msg.addReceiver(aid);
			robot.send(msg);
			
			MessageTemplate t = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
			ACLMessage resp = robot.blockingReceive(t, 3000);
			if(resp!=null){
				robot.log.write(robot.id, "Ricevuto: "+resp.getSender().getLocalName()+" è più vicino!");
				return;
			}
			else{
				boolean outcome = map.acquire(robot.id, p);
				if(outcome==true){
					double distance = robot.distance(robot.initialPosition, p);
					System.out.println("Percorsa la distanza "+distance);
					robot.distances_from_initial.add(distance);
					
					//map.setBatteryLevel(robot.id, newLevel);
					//fare in fase di vera acquisizione
					
					robot.log.write(robot.id, "Prenotato il sito "+p);
				}
			}
		}
		catch(Exception err){
			err.printStackTrace();
		}
	}
	

	
	public boolean done(){
		if(done==true){
			// Calcolo distanza media dalla partenza
			double average = 0.00;
			double sum = 0.00;
			for(Double dist: robot.distances_from_initial)
				sum += dist;
			average = sum/robot.distances_from_initial.size();
			
			map.setDistance(robot.id, average);
			
			// Effettuo le visite vere e proprie dei siti acquisiti
			LinkedList<Position> toVisit = new LinkedList<Position>();
			toVisit.addAll(map.getAcquiredSites(robot.id));
			
			while(!toVisit.isEmpty()){
				Position toGo = robot.getNearestToCurrentPosition(toVisit);
				
				int newBatteryLevel = map.getBatteryLevel(robot.id) - robot.getBatteryDecreasing(toGo);
				
				map.moveToSite(robot.id, toGo.row, toGo.col);
				
				map.setBatteryLevel(robot.id, newBatteryLevel);
				
				robot.log.write(robot.id, "Conquistato il sito "+toGo);
				
				toVisit.remove(toGo);
			}
						
			robot.doDelete();
			
			return true;
		}
		return false;
	}
	
	
	/**
	 * A partire dalla lista di siti prenotati, valuta il consumo della batteria fino ad 
	 * ora, considerando l'ordine di esplorazione dei siti.
	 * 
	 * @return
	 */
	public int batteryConsumptionSoFar(){
		int bc = 0;
		
		LinkedList<Position> toVisit = new LinkedList<Position>();
		toVisit.addAll(map.getAcquiredSites(robot.id));
		
		while(!toVisit.isEmpty()){
			Position toGo = robot.getNearestToCurrentPosition(toVisit);
			
			bc += robot.getBatteryDecreasing(toGo);
			
			toVisit.remove(toGo);
		}
		
		return bc;
	}

}
