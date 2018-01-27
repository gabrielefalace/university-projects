package mobileApp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SearchResponderAgent implements Runnable {
	
	private int myPort;
	private String logicalName;
	private ExecutorService exec;
	
	public SearchResponderAgent(int myPort, String logicalName){
		this.myPort = myPort;
		this.logicalName = logicalName;
		// accepted maximum of 5 simultaneous requests
		exec = Executors.newFixedThreadPool(5);
	}

	
	@Override
	public void run() {
		ServerSocket ss = null;
		Socket connection = null;
		ObjectInputStream input = null;
		try {
			ss = new ServerSocket(myPort);
			while(!isBelowThreshold()){
				// used to receive a term search
				connection = ss.accept();
				input = new ObjectInputStream(connection.getInputStream());
				String searchTerm = (String)input.readObject();
				exec.execute(new SingleRequestManager(connection, searchTerm, logicalName));
			}			
			stopServingRequests();
		} 
		catch (IOException e) {
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		finally{
			try {
				input.close();
				connection.close();
			} 
			catch(Exception e){
				e.printStackTrace();
			}		
		}
	}
	
	public void stopServingRequests(){
		try {
			exec.shutdown();
			exec.awaitTermination(60, TimeUnit.SECONDS);
			exec.shutdownNow();
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isBelowThreshold(){
		/*
		 * TODO Battery level from OS should be retrieved
		 * (batteryLevel < THRESHOLD);
		 */
		return false; 
	}

}
