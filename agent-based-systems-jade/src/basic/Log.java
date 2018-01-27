package basic;

import java.util.Observable;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Log extends Observable{

	private LinkedList<String>[] log;
	private int[] currentIndex;
	
	ReadWriteLock lock;
	Lock writeLock ;
	Lock readLock;
	
	public Log(){
		lock = new ReentrantReadWriteLock();
		writeLock = lock.writeLock();
		readLock =  lock.readLock();
		log = new LinkedList[Map.MAX_AGENTS];
		currentIndex = new int[Map.MAX_AGENTS];
		for(int i=0; i<Map.MAX_AGENTS; i++){
			log[i] = new LinkedList<String>();
			currentIndex[i] = 0;
		}
	}	
	
	
	public void reset(){
		try{
			writeLock.lock();
			for(int i=0; i<Map.MAX_AGENTS; i++){
				log[i] = new LinkedList<String>();
				currentIndex[i] = 0;
			}
			setChanged();
			notifyObservers("LOG_RESET");
		}
		catch(Exception err){
			err.printStackTrace();
		}
		finally{
			writeLock.unlock();
		}
	}
	
	public void write(int robotID, String msg){
		try{
			writeLock.lock();
			log[robotID].addLast(msg);
			setChanged();
			notifyObservers("UPDATE:"+robotID);
		}
		catch(Exception err){
			err.printStackTrace();
		}
		finally{
			writeLock.unlock();
		}
	}
	
	public String getLastMessage(int robotID){
		String res = "";
		try{
			readLock.lock();
			res = log[robotID].get(currentIndex[robotID]);
			currentIndex[robotID]++;
		}
		catch(Exception err){
			err.printStackTrace();
		}
		finally{
			readLock.unlock();
		}
		return res;
	}
	
}
