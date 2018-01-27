package robot;

import basic.*;
import java.util.LinkedList;
import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ClusteredBehaviour extends Behaviour{

	Robot robot;
	LinkedList<Position> listaPos;
	Map map;
	boolean done = false;
	
	
	public ClusteredBehaviour(Robot robot){
		super();
		this.robot = robot;
		map = robot.getMap();
	}
	
	public void action(){
		try{
			try{ 
				Thread.sleep(600); 
			}
			catch(Exception err){
				err.printStackTrace();
			}

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
			
			listaPos = map.getUncheckedSites();
			
			robot.log.write(robot.id, " - ricevuta una lista con "+listaPos.size()+" siti");
			
			if(listaPos.isEmpty()){
				done = true;
				return;
			}
			
			Position p = robot.getNearestToInitialPosition(listaPos);
			
			// Ci arrivo con la batteria? Altrimenti chiudo!
			int decreasing = robot.getBatteryDecreasing(p);
			int newLevel =  map.getBatteryLevel(robot.id) - decreasing;
			
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
				boolean outcome = map.moveToSite(robot.id, p.row, p.col);
				if(outcome==true){
					double distance = robot.distance(robot.initialPosition, p);
					System.out.println("Percorsa la distanza "+distance);
					robot.distances_from_initial.add(distance);
					map.setBatteryLevel(robot.id, newLevel);
					robot.log.write(robot.id, "Acquisisco il sito "+p);
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
			
			robot.doDelete();
			
			return true;
		}
		return false;
	}
	
	

}
