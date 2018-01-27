package mobileApp;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import common.SynchronizedMap;
import common.UserRecord;



public class MobileApp extends JFrame {
	
	private String ip;
	private String logicalName;
	private int searchAgentPort;
	private UserAgent userAgent;
	private SearchResponderAgent searchAgent;
	
	//GUI
	private JTextField field;
	private JButton button;
	private JTextArea area;
	private JScrollPane scroll;

	public MobileApp(String ip,  int searchAgentPort, String logicalName){
		// da riportare
		this.ip = ip;
		this.logicalName = logicalName;
		this.searchAgentPort = searchAgentPort;
		// Starting UserAgent and SearchResponderAgent
		userAgent = new UserAgent(ip, searchAgentPort, logicalName);
		searchAgent = new SearchResponderAgent(searchAgentPort, logicalName);
		ExecutorService executor = Executors.newFixedThreadPool(2);
		executor.execute(userAgent);
		executor.execute(searchAgent);
		
		/*
		 * nell' onStop() killare i thread suddetti con il metodo shutDownNow() 
		 */
		
		
		
		
		field = new JTextField(30);
		button = new JButton("Search");
		area = new JTextArea(30, 50);
		area.setEditable(false);
		scroll = new JScrollPane(area);
		add(field);
		add(button);
		add(scroll);
		button.addActionListener(new ButtonListener());
		setLayout(new FlowLayout());
		setSize(600, 600);
		setResizable(false);
		setTitle("Mobile Application: "+logicalName);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		
		
		
		setVisible(true);
	}
	
	/*
	 * SEARCH FUNCTION.
	 */
	class ButtonListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent event){
			String searchTerm = field.getText();
			
			if(searchTerm.isEmpty()) 
				return;
			
			ExecutorService execService = Executors.newCachedThreadPool();
			
			SynchronizedMap<String, UserRecord> peers = userAgent.getPeers(); 
			if(peers == null)
				return;
			
			// peers
			Set<String> peersId = peers.getKeys();
			if(peersId.isEmpty())
				return;
			
			// remove itself from the set of peers
			peersId.remove(ip+":"+searchAgentPort);
			
			if(peersId.isEmpty()){
				area.append("No mobile device in the smart space ... sorry! \nPlease try later.\n");
				return;
			}
			
			/*
			 * Starting one FileSearcher thread per known peer (in the smart space). 
			 * Results from these threads will be directly shown in the GUI.
			 * 
			 */
			LinkedList<FileSearcher> taskList = new LinkedList<FileSearcher>();
			for(String peerId: peersId){
				FileSearcher fileSearcher = new FileSearcher(peerId, searchTerm);
				taskList.add(fileSearcher);
			}
			
			try {
				ArrayList<Future<LinkedList>> futureList = (ArrayList<Future<LinkedList>>) execService.invokeAll(taskList);
				for(Future<LinkedList> handler: futureList){
					LinkedList<String> peerFiles = (LinkedList<String>)handler.get();
					//visualizzazione risultati
					for(String it:peerFiles)
						// Graphic Updating
						area.append(it+"\n");					
				}
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			} 
			catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}
	
}
