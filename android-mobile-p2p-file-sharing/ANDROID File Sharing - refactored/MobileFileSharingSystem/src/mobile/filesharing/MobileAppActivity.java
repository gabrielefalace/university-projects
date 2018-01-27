package mobile.filesharing;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import common.SynchronizedMap;
import common.UserRecord;


public class MobileAppActivity extends Activity {
	
	private String searchAgentIp;
	private String logicalName;
	private int searchAgentPort;
	private UserAgent userAgent;
	private SearchResponderAgent searchAgent;
	private ExecutorService executor;
	
	private Bundle bundle = null;
	private EditText searchField = null;
	private TextView textView;
	private Button searchButton = null;
	private ListView resultListView = null;
	private ArrayAdapter<String> resultAdapter;
	private int resourceId;
	private ArrayList<String> currentResults;
	
	Handler guiHandler;
	
	private BluetoothAdapter bluetoothAdapter;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.mobile_app);
	
		resourceId = R.layout.list_item;
		currentResults = new ArrayList<String>();
        bundle = this.getIntent().getExtras();
        logicalName = bundle.getString("LOGICAL_NAME");
        searchAgentIp = getLocalIpAddress();		// andrebbe ripetuto con delle sleep inframmezzanti ...
        
        // default per l'emulatore - codice eseguito solo su emulatore
        if(searchAgentIp == null || searchAgentIp.equals("10.0.2.15"))
        	searchAgentIp = "10.0.2.2";		

        searchAgentPort = new Random().nextInt(50000)+6001;
	       
        // 	startBluetoothService();
               
        startWorkerThreads();
        guiHandler = new Handler();
		setGUIcomponents();
		setListeners();
    }
    
    
    private void startWorkerThreads(){
		userAgent = new UserAgent(searchAgentIp, searchAgentPort, logicalName, this);
		searchAgent = new SearchResponderAgent(searchAgentPort, logicalName);
		executor = Executors.newFixedThreadPool(2);
		executor.execute(userAgent);
		executor.execute(searchAgent);
    }
  
    private void setGUIcomponents(){
    	textView = (TextView)findViewById(R.id.connection_status);
		textView.setText("Online @ "+searchAgentIp+":"+searchAgentPort);
        
		searchField = (EditText)findViewById(R.id.search_field);
	
		searchButton = (Button)findViewById(R.id.search_button);
		
		resultListView = (ListView)findViewById(R.id.result_list);
		resultAdapter = new ArrayAdapter<String>(this, resourceId, currentResults);
		resultListView.setAdapter(resultAdapter);	
    }
    
    private void setListeners(){
    	searchButton.setOnClickListener(new SearchListener());
		/*
		 * resultListView.setOnItemClickListener(new BluetoothCheckListener());
		 */
    }
    
    /*
     * Enables the bluetooth adapter and sets the logical name. Also starts the discovery.
     */
    private void startBluetoothService() {
    	Log.d(logicalName, "starting Bluetooth");
    	bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(!bluetoothAdapter.isEnabled())
			bluetoothAdapter.enable();
		bluetoothAdapter.setName(logicalName);

		bluetoothAdapter.startDiscovery();		
	}



	protected void onStop(){
    	super.onStop();
    }
    
    class SearchListener implements OnClickListener {
    	
		@Override
		public void onClick(View v) {
			currentResults.clear();
			resultAdapter.notifyDataSetChanged();
			String searchTerm = (String)searchField.getText().toString();
			if(searchTerm.equals("")) 
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
			peersId.remove(searchAgentIp+":"+searchAgentPort);
			
			if(peersId.isEmpty())
				return;
			
			/*
			 * Starting one FileSearcher thread per known peer (in the smart space). 
			 * Results from these threads will be directly shown in the GUI.
			 * 
			 */
			Collection<Callable<LinkedList>> taskList = new LinkedList<Callable<LinkedList>>();
			for(String peerId: peersId){
				Callable<LinkedList> fileSearcher = new FileSearcher(peerId, searchTerm);
				taskList.add(fileSearcher);
			}
			
			try {
				List<Future<LinkedList>> futureList = (List<Future<LinkedList>>) execService.invokeAll(taskList);
				for(Future<LinkedList> handler: futureList){
					LinkedList<String> peerFiles = (LinkedList<String>)handler.get();
					//visualizzazione risultati
					for(String it:peerFiles){
						Log.d(logicalName, it);
						currentResults.add(it);
					}
				}
				resultAdapter.notifyDataSetChanged();
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			} 
			catch (ExecutionException e) {
				e.printStackTrace();
			}
		
		}
    }
    
    // Get the IP address of your phone's network
    private String getLocalIpAddress() {
		try{
			for(Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();){
				NetworkInterface net = en.nextElement();
				for(Enumeration<InetAddress> enumIPAddress = net.getInetAddresses(); enumIPAddress.hasMoreElements();){
					InetAddress inetAddress = enumIPAddress.nextElement();
					if(!inetAddress.isLoopbackAddress())
						return inetAddress.getHostAddress().toString();
				}
			}
		}
		catch(SocketException exc){
			Log.e("MobileAppActivity", exc.toString());
		}
		return null;
	}
    

    /*
     * Classe che consente ad altri thread di cambiare il messaggio di stato
     * nella textview utilizzando il metodo "post" dell'oggetto Handler.
     */
    public class TextViewMessage implements Runnable{
    	
    	String message;

    	public TextViewMessage(String aMessage){
    		message=aMessage;
    	}
    	
    	public void run(){
    		textView.setText(message);
    	}
    }


    public class BluetoothCheckListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> listView, View arg1, int arg2, long arg3) {
			String listEntry = (String)listView.getSelectedItem();
			String[] tokens = listEntry.split("(");
			
			String peerName = tokens[1].substring(0, tokens[1].length()-1);
			
			Set<BluetoothDevice> peerDevices = bluetoothAdapter.getBondedDevices();
			
			AlertDialog.Builder dialog = new AlertDialog.Builder(MobileAppActivity.this);
			String title, message;
			
			if(peerDevices.contains(peerName)){
				title = "Connection OK";
				message = "You can connect to "+peerName;
			}
			else{
				title = "Bad connection";
				message = "cannot connect to "+peerName;
			}
			dialog.setCancelable(true);
			dialog.show();
		}
    }
}