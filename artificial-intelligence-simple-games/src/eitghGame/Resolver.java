package eitghGame;

import java.util.HashMap;
import java.util.LinkedList;

public class Resolver {

	LinkedList<Direction> path;
	//lista tabù, cioè configurazioni già viste
	private LinkedList<Table> tabùList;
	private HashMap<Table, Integer> counters;
	private boolean stop;
	
	private static int[][] matrix = {{1, 2, 3}, {8, Table.WHITE, 4}, {7, 6, 5}};
	private static final Table solux = new Table(matrix); 
	
	public Resolver(){
		path = new LinkedList<Direction>();
		tabùList  =  new LinkedList<Table>();
		counters = new HashMap<Table, Integer>();
		stop = false;
	}
	
	public void updateCounters(Table t){
		if(counters.containsKey(t)){
			int count = counters.get(t);
			counters.remove(t);
			count++;
			counters.put(t, count);
		}
		else
			counters.put(t, 1);
	}
	
	public boolean resolve(Table t){
		if(stop == true) 
			return false;
		
		boolean result = false;
		
		if(t.equals(solux)){
			stop = true;
			result = true;			
		}
		else{
			
			result = moveToward(t, Direction.UP);
			
			if(result == false){
				result = moveToward(t, Direction.DOWN);
				
				if(result == false){
					result = moveToward(t, Direction.RIGHT);
					
					if(result == false)
						result = moveToward(t, Direction.LEFT);
				}
			}
		}
		return result;
	}
	
	
	private boolean moveToward(Table t, Direction direction){
		if(t.isFeasible(direction)){
			Table t_dir = t.move(direction);
			
			if(tabùList.contains(t_dir))
				return false;
			
			updateCounters(t_dir);
			
			if(counters.get(t_dir) > Table.BRANCHING_FACTOR){
				tabùList.add(t_dir);
				return false;
			}
			
			boolean outcome = resolve(t_dir);	// chiamata ricorsiva
			
			if(outcome==true){
				path.addLast(direction);
				return true;
			}
			else
				return false;
		}
		else
			return false;
	}
	
	
	public void printSolution(){
		for(int i=0; i<path.size(); i++){
			System.out.println((i+1)+") "+path.get(i));
		}
	}
	
}
