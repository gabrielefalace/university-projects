package discovery;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedList;

import common.Message;
import common.SynchronizedMap;
import common.UserRecord;

public class UserManager extends Thread {

	// socket già disponibile per la comunicazione
	private Socket connection;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private String userId;
	private volatile SynchronizedMap<String, UserRecord> peersMap;
	
	private ServerGUI serverGui;
	
	public UserManager(SynchronizedMap<String, UserRecord> map, Socket connection, ServerGUI serverGui){
		peersMap = map;
		this.connection = connection;
		this.serverGui = serverGui;
	}
	
	public void run(){
		UserRecord record = null;
		int index = -1;
		// Register new user
		try{
			input = new ObjectInputStream(connection.getInputStream());
			output = new ObjectOutputStream(connection.getOutputStream());
			System.out.println("UM::Ho aperto gli stream di in e di out");
			record = (UserRecord)input.readObject();
			userId = record.getIp()+":"+record.getPort();
			peersMap.put(userId, record);
			
			index = serverGui.addNewLog(record.getLogicalName()+"@"+userId);
			serverGui.writeOnLog(index, "["+System.currentTimeMillis()+"] Registration OK");
		} 
		catch (IOException e) {
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		// listen for PING and PEERS_UPDATE messages
		try {
			connection.setSoTimeout(Server.OFFLINE_TIME);
			while(true){
				Message message = (Message)input.readObject();
				if(message.getMessageType().equals("PING")){
					long currentTime = System.currentTimeMillis();
					record.setLastTimestamp(currentTime);
					serverGui.writeOnLog(index, "["+currentTime+"] Received a PING MESSAGE");
					serverGui.writeOnLog(index, "new time_stamp is "+record.getLastTimestamp());
				}
				if(message.getMessageType().equals("PEERS_UPDATE")){
					serverGui.writeOnLog(index, "SERVER-THREAD::La size della mappa lato Server è"+peersMap.size());
					
					LinkedList list = new LinkedList();
					Collection<UserRecord> peersRecords = peersMap.getValues();
					for(UserRecord rec: peersRecords)
						list.add(rec);
					
					output.writeObject(list);
					
					output.flush();
					serverGui.writeOnLog(index, "["+System.currentTimeMillis()+"] Received an UPDATE MESSAGE");
				}
			}
		} 
		catch(Exception e){
			// timeout is expired
			peersMap.remove(userId);
			serverGui.writeOnLog(index, "DISCONNECTED!");
		} 
		finally{
			try {
				input.close();
				output.close();
				connection.close();
			} 
			catch(Exception e){
				e.printStackTrace();
			}		
		}
	}
	
}
