package mobile.filesharing;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Environment;

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
		LinkedList fileList = new LinkedList();

		File rootDirectory = Environment.getExternalStorageDirectory();
		File[] rootContent = rootDirectory.listFiles();
		
		LinkedList results = new LinkedList();
		
		Pattern searchTermPattern = Pattern.compile(searchTerm, Pattern.CASE_INSENSITIVE);
		Matcher matcher = searchTermPattern.matcher(rootContent[0].getName());
		
		for(File file: rootContent){
			String fileName = file.getName();
			matcher.reset(fileName);
			if(matcher.find()==true)
				results.add(fileName+" ("+logicalName+")");
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
