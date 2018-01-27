package mobileApp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

import common.Message;
import common.SynchronizedMap;
import common.UserRecord;



public class UserAgent implements Runnable {
	
	private volatile SynchronizedMap<String, UserRecord> peersMap;
	private int port;
	private String logicalName;
	private String ip;
	
	public UserAgent(String ip, int port, String logicalName){
		this.ip = ip;
		this.port = port;
		this.logicalName = logicalName;
	}
	
	public SynchronizedMap<String, UserRecord> getPeers(){
		return peersMap;
	}
	
	@SuppressWarnings("unchecked")
	public void run(){
		Socket s = null;
		System.out.println("Client connected!");
		ObjectInputStream input = null;
		ObjectOutputStream output = null;
		
		try {
			s = new Socket("10.0.2.2", 6000);	
			UserRecord record = new UserRecord(ip, port, logicalName);
			output = new ObjectOutputStream(s.getOutputStream());
			output.writeObject(record);
			output.flush();
			System.out.println("Ho inviato la richiesta di REGISTER");
			Thread.sleep(500);
			
			input = new ObjectInputStream(s.getInputStream());
			while(true){
				for(int i=0; i<3; i++){
					Message pingMsg = new Message("PING");
					output.writeObject(pingMsg);
					output.flush();
					System.out.println("Ho inviato un messaggio di PING");
					Thread.sleep(4000);
				}
				Message updateMsg = new Message("PEERS_UPDATE");
				output.writeObject(updateMsg);
				output.flush();
				System.out.println("Ho inviato la richiesta di UPDATE");
				
				// Receive a list of UserRecord -> Creates a SynchroMap
				
				LinkedList list = (LinkedList)input.readObject();
				
				SynchronizedMap<String, UserRecord> peersMapReceived = new SynchronizedMap<String, UserRecord>();
				
				for(Object iter: list){
					UserRecord rec = (UserRecord)iter;
					peersMapReceived.put(rec.getIp()+":"+rec.getPort(), rec);
				}
				
				peersMap = peersMapReceived;
				
				System.out.println(logicalName+" : I peer sono "+peersMap.size()+"["+peersMapReceived.size()+"]");
				
				System.out.println("Ho aggiornato la mia peer-list");
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try {
				s.close();
				output.close();
				input.close();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
}
