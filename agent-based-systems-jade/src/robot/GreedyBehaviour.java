package robot;

import basic.*;

import java.util.LinkedList;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;

public class GreedyBehaviour extends Behaviour{

	Robot robot;
	LinkedList<Position> listaPos;
	Map map;
	boolean done = false;
	
	
	public GreedyBehaviour(Robot robot){
		super();
		this.robot = robot;
		map = robot.getMap();
	}
	
	public void action(){
		try{
			try{Thread.sleep(600);}
			catch(Exception err){err.printStackTrace();}

			
			listaPos = map.getUncheckedSites();
			
			robot.log.write(robot.id, "ricevuta una lista con "+listaPos.size()+" siti");
			
			if(listaPos.isEmpty()){
				done = true;
				return;
			}
			
			Position p = robot.getNearestToCurrentPosition(listaPos);
			
			int decreasing = robot.getBatteryDecreasing(p);
			int newLevel =  map.getBatteryLevel(robot.id) - decreasing;
			if(newLevel >= 0){
				boolean outcome = map.moveToSite(robot.id, p.row, p.col);
				if(outcome==true){
					robot.currentPosition = p;
					double distance = robot.distance(robot.initialPosition, p);
					System.out.println("Percorsa la distanza "+distance);
					robot.distances_from_initial.add(distance);
					robot.log.write(robot.id, "Spostato con successo in posizione "+p);
					map.setBatteryLevel(robot.id, newLevel);
				}
			}
			else{
				robot.getMap().setFinished(robot.id);
				robot.log.write(robot.id, "Finita la batteria!");
				done = true;
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
			
			return true;
		}
		else return false;
	}
	
	
}
