package discovery;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import common.SynchronizedMap;
import common.UserRecord;

public class Server implements Runnable{
	
	public static final int SERVERPORT = 6000;
	public static final int OFFLINE_TIME = 60000;
	
	private SynchronizedMap<String, UserRecord> peersMap = new SynchronizedMap<String, UserRecord>();
	
	public void run(){
		try {
			ServerGUI serverGui = new ServerGUI();
			int serverIndex = serverGui.addNewLog("Server main process");
			System.out.println("Index = "+serverIndex);
			
			ServerSocket ss = new ServerSocket(SERVERPORT);
			serverGui.writeOnLog(serverIndex, "Server Started.");
			
			while(true){
				serverGui.writeOnLog(serverIndex, "Listening on port: "+SERVERPORT+" ...");
				Socket clientConnection = ss.accept();
				serverGui.writeOnLog(serverIndex, "New client accepted: starting new server thread.");
				
				UserManager manager = new UserManager(peersMap, clientConnection, serverGui);
				manager.start();
				serverGui.writeOnLog(serverIndex, "New User Manager Started");
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		Thread mainThread = new Thread(new Server());
		mainThread.start();
	}
	
}
