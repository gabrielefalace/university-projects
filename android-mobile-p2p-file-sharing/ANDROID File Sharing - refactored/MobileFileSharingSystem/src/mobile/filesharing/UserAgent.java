package mobile.filesharing;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import android.util.Log;
import common.Message;
import common.SynchronizedMap;
import common.UserRecord;



public class UserAgent implements Runnable {
	
	private volatile SynchronizedMap<String, UserRecord> peersMap;
	private int port;
	private String logicalName;
	private String ip;
	private MobileAppActivity appReference;
	
	public UserAgent(String ip, int port, String logicalName, MobileAppActivity reference){
		this.ip = ip;
		this.port = port;
		this.logicalName = logicalName;
		this.appReference = reference;
	}
	
	public SynchronizedMap<String, UserRecord> getPeers(){
		return peersMap;
	}
	
	@SuppressWarnings("unchecked")
	public void run(){
		Socket s = null;
		ObjectInputStream input = null;
		ObjectOutputStream output = null;
		
		try {
			s = new Socket("10.0.2.2", 6000);			
			UserRecord record = new UserRecord(ip, port, logicalName);
			output = new ObjectOutputStream(s.getOutputStream());
			output.writeObject(record);
			output.flush();
			Log.d(logicalName+"::UserAgent", "inviato la richiesta di registrazione "+ip+":"+port);
			
			Thread.sleep(500);
			
			input = new ObjectInputStream(s.getInputStream());
			
			while(true){
				Message updateMsg = new Message("PEERS_UPDATE");
				output.writeObject(updateMsg);
				output.flush();
				
				Log.d(logicalName+"::UserAgent", "in attesa della peer-list update");
				appReference.guiHandler.post(appReference.new TextViewMessage("waiting for peer-list update"));
				
				// Receive a list of UserRecord -> Creates a SynchroMap
				
				LinkedList list = (LinkedList)input.readObject();
				
				SynchronizedMap<String, UserRecord> peersMapReceived = new SynchronizedMap<String, UserRecord>();
				
				appReference.guiHandler.post(appReference.new TextViewMessage("peer-list update received"));
				
				for(Object iter: list){
					UserRecord rec = (UserRecord)iter;
					peersMapReceived.put(rec.getIp()+":"+rec.getPort(), rec);
				}
				
				peersMap = peersMapReceived;
				
				if(peersMap != null)
					appReference.guiHandler.post(appReference.new TextViewMessage(logicalName+" online - "+(peersMapReceived.size() - 1)+" peers connected"));
				else
					appReference.guiHandler.post(appReference.new TextViewMessage(logicalName+" online - no other peers connected"));
					
				
				for(int i=0; i<3; i++){
					Message pingMsg = new Message("PING");
					output.writeObject(pingMsg);
					output.flush();
					System.out.println("Ho inviato un messaggio di PING");
					Thread.sleep(4000);
				}
				
				
			}
		} 
		catch (Exception e) {
			Log.e(logicalName+"::UserAgent", "Errore:\n"+e.getStackTrace());
		}
		finally{
			try {
				s.close();
				output.close();
				input.close();
				Log.d(logicalName+"::UserAgent", "Chiusi gli stream di I/O e la socket");
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
}
