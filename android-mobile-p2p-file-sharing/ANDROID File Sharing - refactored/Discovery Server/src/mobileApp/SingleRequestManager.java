package mobileApp;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SingleRequestManager implements Runnable {
	
	private Socket connection;
	private String searchTerm;
	private String logicalName;
	
	public SingleRequestManager(Socket connection, String searchTerm, String logicalName){
		this.connection = connection;
		this.searchTerm = searchTerm;
		this.logicalName = logicalName;
	}
	
	
	@Override
	public void run() {		

		ObjectOutputStream output = null;
		
		// recupera la lista dei file
		
		/*
		 * TODO Retrieve real matching files in Android File System!
		 */
		
		LinkedList fileList = new LinkedList();
		fileList.add("Cuore Matto");
		fileList.add("Bamboleo");
		fileList.add("Living in America");
		fileList.add("Le ro la");
		fileList.add("Tu vo fa l'americano");
		fileList.add("Call me");
		fileList.add("Forever Young");
		fileList.add("Rose rosse");
		// seleziona quelli che matchano la richiesta
		
		LinkedList<String> files = (LinkedList<String>)fileList;
		LinkedList results = new LinkedList();
		
		Pattern searchTermPattern = Pattern.compile(searchTerm, Pattern.CASE_INSENSITIVE);
		Matcher matcher = searchTermPattern.matcher(files.getFirst());
		
		for(String iter: files){
			matcher.reset(iter);	// il testo in cui cercare il searchTerm è il titolo del file.
			if(matcher.find()==true)
				results.add(iter + " ("+logicalName+")");
		}
			
		// invia la lista dei risultati
		try {
			output = new ObjectOutputStream(connection.getOutputStream());
			output.writeObject(results);
			output.flush();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				output.close();
				connection.close();
			} 
			catch(Exception e){
				e.printStackTrace();
			}		
		}
	}

}
