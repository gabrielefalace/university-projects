package basic;

import exceptions.*;
import gui.EnvironmentGUI;
import java.awt.Color;
import java.util.*;
import java.util.concurrent.locks.*;

/**
 * 
 * @author Gabriele
 *
 */
public class Map extends Observable{
	
	ReadWriteLock lock = new ReentrantReadWriteLock();
	Lock writeLock = lock.writeLock();
	Lock readLock = lock.readLock();
	
	boolean[] finished;
	boolean showReport;
	
	double[] distanceAverages;
	
	/**
	 * 
	 */
	HashSet<Position> unAcquiredSites;
	
	/**
	 * 
	 */
	HashSet<Position>[] acquiredSites;
	
	/**
	 * La dimensione della mappa.
	 */
	public int dim;
	
	/**
	 * le posizioni dei robot nella mappa.
	 */
	HashMap<Integer, Position> positions;
	
	/**
	 * Le posizioni in cui ci sono dei siti visitati.
	 */
	HashSet<Position> checkedSites[];
	
	/**
	 * Le posizioni in cui ci sono dei siti attivi.
	 */
	HashSet<Position> uncheckedSites;
	
	/**
	 * 
	 */
	HashMap<Integer, Color> colors;
	
	/**
	 * 
	 */
	LinkedList<Color> colorList;
	
	/**
	 * 
	 */
	int numAgents = 4;
	
	/**
	 * 
	 */
	int[] batteryLevel;
	
	/**
	 * 
	 */
	int numSites = 250;
	
	/**
	 * 
	 */
	static final int MAX_AGENTS = 8;
	
	
	/**
	 * Costruisce una mappa di dimensione n x n.
	 * @param n
	 */
	public Map(int n){
		dim = n;
		
		batteryLevel = new int[MAX_AGENTS];
		for(int h=0; h<batteryLevel.length; h++)
			batteryLevel[h]=100;
		
		finished = new boolean[MAX_AGENTS];
		for(int i=0; i<MAX_AGENTS; i++)
			finished[i] = true;
		
		/*
		 * Trying...
		 */
		distanceAverages = new double[MAX_AGENTS];
		for(int i=0; i<MAX_AGENTS; i++)
			finished[i] = true;
		
		checkedSites = new HashSet[MAX_AGENTS];
		for(int j=0; j<checkedSites.length; j++)
			checkedSites[j] = new HashSet<Position>();
		
		acquiredSites = new HashSet[MAX_AGENTS];
		for(int j=0; j<acquiredSites.length; j++)
			acquiredSites[j] = new HashSet<Position>();
		
		positions = new HashMap<Integer, Position>();
		colorList = new LinkedList<Color>();
		
		colorList.add(Color.BLUE);
		colorList.add(Color.CYAN);
		colorList.add(Color.RED);
		colorList.add(Color.MAGENTA);
		colorList.add(Color.ORANGE);
		colorList.add(Color.GREEN);
		colorList.add(Color.YELLOW);
		colorList.add(Color.PINK);
		
		uncheckedSites = new HashSet<Position>();
		colors = new HashMap<Integer, Color>();
		
		showReport = false;
		
		unAcquiredSites = new HashSet<Position>();
				
		setChanged();
		notifyObservers(this);
	}
	
	
	/**
	 * Verifica la presenza di un robot in una data coppia di coordinate.
	 * @param x
	 * @param y
	 * @return true se in (<i>x</i>,<i>y</i>) c'è un robot.
	 *
	public boolean isRobotAt(int row, int col){
		boolean res = false;
		try{
			readLock.lock();
			res = positions.containsValue(new Position(row, col));
		}
		catch(Exception err){
			err.printStackTrace();
		}
		finally{
			readLock.unlock();
		}
		return res;
	}
	
	/**
	 * Verifica la presenza di un robot in una data Position.
	 * @param p Position che si vuole verificare
	 * @return true se in <i>p</i> c'è un robot.
	 *
	public boolean isRobotAt(Position p){
		boolean res = false;
		try{
			readLock.lock();
			res =  positions.containsValue(p);
		}
		catch(Exception err){
			err.printStackTrace();
		}
		finally{
			readLock.unlock();
		}
		return res;
	}
	*/ 
	
	/**
	 * Fa accedere alla posizione del robot.
	 * @param id
	 * @return La posizione del robot id.
	 */
	public Position getRobotPosition(int id){
		Position res = null;
		try{
			readLock.lock();
			res = positions.get(id);
		}
		catch(Exception err){
			err.printStackTrace();
		}
		finally{
			readLock.unlock();
		}
		return res;
	}
	
	/**
	 * posiziona il robot id nella coppia di coordinate specificate <i>x</i>,<i>y</i>.
	 * @param id
	 * @param x
	 * @param y
	 */
	public void setRobot(int id, int row, int col){
		try{
			writeLock.lock();
			finished[id] = false;
			Color c = colorList.getFirst();
			colorList.remove(c);
			Position p = new Position(row,col);
			positions.put(id, p);
			colors.put(id, c);
			setChanged();
			notifyObservers(this);
		}
		catch(Exception err){
			err.printStackTrace();
		}
		finally{
			writeLock.unlock();
		}
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public Color getRobotColor(int id){
		Color c = null;
		try{
			readLock.lock();
			c = colors.get(id);
		}
		catch(Exception err){
			err.printStackTrace();
		}
		finally{
			readLock.unlock();
		}
		return c;
	}
	
	/**
	 * Serve (in fase di inizializzazione) ad attivare un sito sulla mappa.
	 * @param x
	 * @param y
	 */
	public void setActiveSite(int row, int col){
		try{
			writeLock.lock();
			uncheckedSites.add(new Position(row,col));
			unAcquiredSites.add(new Position(row, col));
			setChanged();
			notifyObservers(this);
		}
		catch(Exception err){
			err.printStackTrace();
		}
		finally{
			writeLock.unlock();
		}
	}
	
	
	/**
	 * Verifica che una coppia di coordinate sia la posizione di un sito <u>attivo</u>
	 * @param x
	 * @param y
	 * @return true se in (<i>x</i>,<i>y</i>) vi è un sito <u>attivo</u>.
	 */
	public boolean isActiveSite(int row, int col){
		boolean res = false;
		try{
			readLock.lock();
			res = uncheckedSites.contains(new Position(row, col));
		}
		catch(Exception err){
			err.printStackTrace();
		}
		finally{
			readLock.unlock();
		}
		return res;
	}

	
	/**
	 * Consente di ottenere il set dei siti non visitati.
	 * @return
	 */
	public LinkedList<Position> getUncheckedSites(){
		LinkedList<Position> list = new LinkedList<Position>();
		try{
			readLock.lock();
			for(Position p: uncheckedSites)
				list.add(new Position(p.row, p.col));
		}
		catch(Exception err){
			err.printStackTrace();
		}
		finally{
			readLock.unlock();
		}
		return list;
	}
	
	/**
	 * 
	 * @return
	 */
	public HashSet<Position>[] getCheckedSites(){
		HashSet<Position>[] cs = null;
		try{
			readLock.lock();
			cs = checkedSites;
		}
		catch(Exception err){
			err.printStackTrace();
		}
		finally{
			readLock.unlock();
		}
		return cs;
	}
	
	/**
	 * Sposta l'agente id nel sito (<i>row</i>,<i>col</i>) indicato, impostandolo come visitato.
	 * @param id
	 * @param row
	 * @param col
	 */
	public boolean moveToSite(int id, int row, int col){
		boolean res = false;
		try{
			writeLock.lock();
			if(!isActiveSite(row, col))
				res = false;
			else{
				Position site = new Position(row, col);
				uncheckedSites.remove(site);
				checkedSites[id].add(site);
				positions.remove(id);
				positions.put(id, site);
				setChanged();
				notifyObservers(this);
				res = true;
			}
		}
		catch(Exception err){
			err.printStackTrace();
		}
		finally{
			writeLock.unlock();
		}
		return res;
	}
	
	/**
	 * 
	 */
	public void reset(){
		try{
			writeLock.lock();
			positions = new HashMap<Integer, Position>();
			colors = new HashMap<Integer, Color>();
			
			acquiredSites = new HashSet[MAX_AGENTS];
			for(int k=0; k<acquiredSites.length; k++)
				acquiredSites[k] = new HashSet<Position>();
			
			unAcquiredSites = new HashSet<Position>();
			
			checkedSites = new HashSet[MAX_AGENTS];
			for(int k=0; k<checkedSites.length; k++)
				checkedSites[k] = new HashSet<Position>();
			
			uncheckedSites = new HashSet<Position>();
			for(int i=0; i<batteryLevel.length; i++)
				batteryLevel[i] = 100;
			for(int i=0; i<MAX_AGENTS; i++)
				finished[i] = true;
			for(int i=0; i<MAX_AGENTS; i++)
				distanceAverages[i] = 0.00;
			colorList.add(Color.BLUE);
			colorList.add(Color.CYAN);
			colorList.add(Color.RED);
			colorList.add(Color.MAGENTA);
			colorList.add(Color.ORANGE);
			colorList.add(Color.GREEN);
			colorList.add(Color.YELLOW);
			colorList.add(Color.PINK);
			System.gc();
			System.out.println("Unchecked sites = "+getUncheckedSites());
			System.out.println("Number of agents = "+getNumAgents());
			setChanged();
			notifyObservers();
		}
		catch(Exception err){
			err.printStackTrace();
		}
		finally{
			writeLock.unlock();
		}
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public int getBatteryLevel(int id){
		int level = 0;
		try{
			readLock.lock();
			level = batteryLevel[id];
		}
		catch(Exception err){err.printStackTrace();}
		finally{
			readLock.unlock();
		}
		return level;
	}
	
	/**
	 * 
	 * @param id
	 * @param level
	 */
	public void setBatteryLevel(int id, int level){
		try{
			writeLock.lock();
			batteryLevel[id] = level;
			setChanged();
			notifyObservers(this);
		}
		catch(Exception err){
			err.printStackTrace();
		}
		finally{
			writeLock.unlock();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public int getNumSites(){
		int num = 0;
		try{
			readLock.lock();
			num = numSites;
		}
		catch(Exception err){err.printStackTrace();}
		finally{
			readLock.unlock();
		}
		return num;
	}

	/**
	 * 
	 * @param numSites
	 */
	public void setNumSites(int numSites){
		try{
			writeLock.lock();
			this.numSites = numSites;
			setChanged();
			notifyObservers(this);
		}
		catch(Exception err){err.printStackTrace();}
		finally{
			writeLock.unlock();
		}
	}

	/**
	 * 
	 * @return
	 */
	public int getNumAgents(){
		int num = 0;
		try{
			readLock.lock();
			num = numAgents;
		}
		catch(Exception err){
			err.printStackTrace();
		}
		finally{
			readLock.unlock();
		}
		return num;
	}
	
	/**
	 * 
	 * @param numAgents
	 */
	public void setNumAgents(int numAgents){
		try{
			writeLock.lock();
			this.numAgents = numAgents;
			setChanged();
			notifyObservers(this);
		}
		catch(Exception err){
			err.printStackTrace();
		}
		finally{
			writeLock.unlock();
		}
	}
	
	public void setFinished(int id){
		try{
			writeLock.lock();
			finished[id] = true;
		}
		catch(Exception err){
			err.printStackTrace();
		}
		finally{
			writeLock.unlock();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean areAllFinished(){
		boolean res = true;
		try{
			readLock.lock();
			for(int j=0; j<MAX_AGENTS; j++)
				if(finished[j]==false)
					res=false;
		}
		catch(Exception err){
			err.printStackTrace();
		}
		finally{
			readLock.unlock();
		}
		return res;
	}
	
	/**
	 * 
	 * @param aValue
	 */
	public void setShowReport(boolean aValue){
		showReport = aValue;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getShowReport(){
		return showReport;
	}
	
	/**
	 * 
	 * @param robotID
	 * @return
	 */
	public double getDistance(int robotID){
		double res = 0.00;
		try{
			readLock.lock();
			res = distanceAverages[robotID];
		}
		catch(Exception err){
			err.printStackTrace();
		}
		finally{
			readLock.unlock();
		}
		return res;
	}
	
	/**
	 * 
	 * @param robotID
	 * @param distance
	 */
	public void setDistance(int robotID, double distance){
		try{
			writeLock.lock();
			distanceAverages[robotID] = distance;
			setChanged();
			notifyObservers();
		}
		catch(Exception err){
			err.printStackTrace();
		}
		finally{
			writeLock.unlock();
		}
	}
	
	
	/**
	 * 
	 * @param p
	 */
	public boolean acquire(int robotID, Position p){
		boolean outcome = false;
		try{
			writeLock.lock();
			
			unAcquiredSites.remove(p);
			acquiredSites[robotID].add(p);
			outcome = true;
			
			setChanged();
			notifyObservers();
		}
		catch(Exception err){
			err.printStackTrace();
		}
		finally{
			writeLock.unlock();
		}
		return outcome;
	}

	
	/**
	 * 
	 * @param robotID
	 * @return
	 */
	public HashSet<Position> getAcquiredSites(int robotID){
		HashSet<Position> as = new HashSet<Position>();
		try{
			readLock.lock();
			
			as = acquiredSites[robotID];
			
			setChanged();
			notifyObservers();
		}
		catch(Exception err){
			err.printStackTrace();
		}
		finally{
			readLock.unlock();
		}
		return as;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public HashSet<Position> getUnacquiredSites(){
		HashSet<Position> us = new HashSet<Position>();
		try{
			readLock.lock();
			
			us = unAcquiredSites;
			
			setChanged();
			notifyObservers();
		}
		catch(Exception err){
			err.printStackTrace();
		}
		finally{
			readLock.unlock();
		}
		return us;
	}
	
}
