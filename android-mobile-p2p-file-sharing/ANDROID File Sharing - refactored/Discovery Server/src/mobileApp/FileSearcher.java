package mobileApp;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.Callable;

public class FileSearcher implements Callable<LinkedList> {
	
	private String userID;
	private String searchTerm;
	
	public FileSearcher(String userID, String searchTerm){
		this.userID = userID;
		this.searchTerm = searchTerm;
	}
	
	@SuppressWarnings("unchecked")
	public LinkedList call(){
		Socket connect = null;
		LinkedList searchResult = new LinkedList();
		// used to receive search results
		ObjectInputStream input = null;
		
		// used to send a file search 
		ObjectOutputStream output = null;
		
		try{
			String[] tokens = userID.split(":");
			String ip = tokens[0];
			int port = Integer.parseInt(tokens[1]);
			System.out.println("Cerco di contattare IP = "+ip+" e port = "+port);
			connect = new Socket(ip, port);
			output = new ObjectOutputStream(connect.getOutputStream());
			output.writeObject(searchTerm);
			output.flush();
			input = new ObjectInputStream(connect.getInputStream());
			searchResult = (LinkedList)input.readObject();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try {
				input.close();
				output.close();
				connect.close();
			} 
			catch(Exception e){
				e.printStackTrace();
			}		
		}
		return searchResult;
	}
	
	
}
