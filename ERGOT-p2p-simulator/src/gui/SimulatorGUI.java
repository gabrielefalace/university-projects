package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import exceptions.ConceptNotFoundException;
import exceptions.PeerNotFoundException;

import parameters.LookupDTO;
import parameters.NetworkDTO;
import parameters.OntologyDTO;
import parameters.ServicesDTO;

import semanticNode.SemanticNode;
import semanticOverlayNetwork.SemanticOverlayNetwork;
import semanticOverlayNetwork.SemanticOverlayNetwork.SemanticBasedResult;
import simulation.LookupHandler;
import simulation.SimulationEnvironmentBuilder;
import util.Service;

public class SimulatorGUI extends JFrame{

	private JTable table;
	private JScrollPane scroll;
	private JButton createNetwork, publish, search;
	private ParameterModel model;
	
	private SemanticOverlayNetwork SON;
	private SimulationEnvironmentBuilder builder;
	private LookupHandler lookupHandler;
	
	public SimulatorGUI(){
		
		SON = null;
		builder = null;
		lookupHandler = null;
		
		setFrame();
		initComponents();
		setComponents();
		locateComponents();
		setListeners();
		setVisible(true);
	}
	
	private void setFrame(){
		setTitle("ERGOT - Simulator");
		setSize(500, 550);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void initComponents(){
		model = new ParameterModel();
		
		table = new JTable(model);
		table.setCellSelectionEnabled(true);
		table.setRowHeight(25);
		table.setFont(new Font("Arial", Font.PLAIN, 16));
		table.getTableHeader().setFont(new Font("Arial", Font.PLAIN, 16));
		table.getTableHeader().setReorderingAllowed(false);
		
		scroll = new JScrollPane(table);

		createNetwork = new JButton();
		publish = new JButton();
		search = new JButton();

	}
	
	private void setComponents(){
		add(scroll);
		add(createNetwork);
		add(publish);
		add(search);
		
		createNetwork.setIcon(new ImageIcon("./icons/connect.png"));
		createNetwork.setToolTipText("Crea una Semantic Overlay Network");
		
		publish.setIcon(new ImageIcon("./icons/publish.png"));
		publish.setToolTipText("Pubblica i servizi");
		
		search.setIcon(new ImageIcon("./icons/search.png"));
		search.setToolTipText("Avvia una ricerca");
	}
	
	private void locateComponents(){
		setLayout(new FlowLayout());
	}
	
	private void setListeners(){
		createNetwork.addActionListener(new CreateNetworkListener());
		publish.addActionListener(new PublishListener());
		search.addActionListener(new SearchListener());
	}
	
	
	//******************************************************************** //
	//************************* ASCOLTATORI ****************************** //
	//******************************************************************** //
	
	private class CreateNetworkListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(checkCreationParameters()==false){
				JOptionPane.showMessageDialog(null, "Alcuni parametri sono errati", "Errore", JOptionPane.ERROR_MESSAGE);
				return;
			}
			else{
				try{
					int bits = Integer.parseInt((String)table.getValueAt(0, 1));
					int peers = Integer.parseInt((String)table.getValueAt(1, 1));
					int link_th = Integer.parseInt((String)table.getValueAt(2, 1));
					int num_cat = Integer.parseInt((String)table.getValueAt(3, 1));
					int num_dom = Integer.parseInt((String)table.getValueAt(4, 1));
					int cat_depth = Integer.parseInt((String)table.getValueAt(5, 1));
					int dom_depth = Integer.parseInt((String)table.getValueAt(6, 1));
					int numServ = Integer.parseInt((String)table.getValueAt(7, 1));
					int catServ = Integer.parseInt((String)table.getValueAt(8, 1));
					int inServ = Integer.parseInt((String)table.getValueAt(9, 1));
					int outServ = Integer.parseInt((String)table.getValueAt(10, 1));
					ServicesDTO serviceDTO = new ServicesDTO(numServ, catServ, inServ, outServ);
					NetworkDTO networkDTO = new NetworkDTO(bits, peers, link_th);
					OntologyDTO ontoDTO = new OntologyDTO(num_cat, num_dom, cat_depth, dom_depth);
					builder = new  SimulationEnvironmentBuilder(serviceDTO, networkDTO, ontoDTO);
					SON = builder.getSemanticOverlayNetwork();
					
					/*
					
					ArrayList<Service> services = builder.getServices();
					
					for(Service service: services){
						SemanticNode publisher = SON.getRandomPeer();
						SON.publish(publisher, service);
					}
					
					*/
					
				}
				catch(Exception err){
					JOptionPane.showMessageDialog(null, "Problemi durante la creazione della rete", "Errore!", JOptionPane.ERROR_MESSAGE);
				}
				finally{
					JOptionPane.showMessageDialog(null, "SON creata", "Ok", JOptionPane.INFORMATION_MESSAGE);
				}					
			}
		}
	}
	
	private class PublishListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(SON==null){
				JOptionPane.showMessageDialog(null, "Bisogna prima creare una SON", "Rete inesistente!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			try{
				ArrayList<Service> services = builder.getServices();			
				for(Service service: services){
					SemanticNode publisher = SON.getRandomPeer();
					SON.publish(publisher, service);
				}
				JOptionPane.showMessageDialog(null, "Pubblicazione dei servizi terminata", "Ok", JOptionPane.INFORMATION_MESSAGE);
			}
			catch(Exception err){
				JOptionPane.showMessageDialog(null, "Problemi durante la pubblicazione dei servizi", "Errore!", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	
	private class SearchListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(SON==null){
				JOptionPane.showMessageDialog(null, "Bisogna prima creare una SON", "Rete inesistente!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(checkSearchParameters()==false){
				JOptionPane.showMessageDialog(null, "Alcuni parametri di ricerca sono errati", "Errore", JOptionPane.ERROR_MESSAGE);
				return;
			}
			else{
				String msg = "";
				//String keyword = query.getText();
				try{
					//int id = (int)Integer.parseInt(query.getText());
					//String category = "concept_"+id;
					float similarityThreshold = Float.parseFloat((String)table.getValueAt(11, 1));
					int TTL = Integer.parseInt((String)table.getValueAt(12, 1));
					
					LookupDTO dto = new LookupDTO(similarityThreshold, TTL, 1);
					
					lookupHandler = new LookupHandler(SON, dto);
					
					ArrayList<SemanticBasedResult> results = lookupHandler.executeSemanticBasedLookups();
					SemanticBasedResult result = results.get(0);
					
					LinkedList<Integer> services = result.getTimedNumberOfServices();
					LinkedList<Float> recall = result.getTimedRecall();
					msg+="Categoria di ricerca: "+result.getQueryString()+"\n";
					msg+="Totale servizi trovati: "+services.getLast()+"\n";
					msg+="Recall: "+recall.getLast()+"\n";
					msg+="Ricerca terminata in "+(recall.size()-1)+" hops";
				}
				catch(NumberFormatException nfe){
					JOptionPane.showMessageDialog(null, "deve essere un numero di categoria", "Errore", JOptionPane.ERROR_MESSAGE);
					return;
				}
				catch(ConceptNotFoundException cnf){
					JOptionPane.showMessageDialog(null, "Nessun servizio annotato al concetto", "Sorry", JOptionPane.INFORMATION_MESSAGE);
				}
				catch (Exception err) {
					err.printStackTrace();
				}
				new ResultsFrame(msg);
			}
		}
	}	
	
	//******************************************************************** //
	//*******************  MODELLO DEI DATI  ***************************** //
	//******************************************************************** //
	
	private class ParameterModel extends AbstractTableModel{
		
		private static final long serialVersionUID = 6961303880422294265L;
		String[] headings = {"Parameter Name", "Parameter Value"};
		String[][] data = new String[13][2];
		
		public ParameterModel(){
			data[0][0] = "Bit";
			data[1][0] = "Numero di Peer";
			data[2][0] = "Soglia creazione link";
			data[3][0] = "Numero di categorie";
			data[4][0] = "Numero di domini";
			data[5][0] = "Profondità categorie";
			data[6][0] = "Profondità domini";
			data[7][0] = "Numero di servizi";
			data[8][0] = "Categorie per servizio";
			data[9][0] = "Input per servizio";
			data[10][0] = "Output per servizio";
			data[11][0] = "Similarità di inoltro";
			data[12][0] = "Time To Live";
			
			data[0][1] = "16";
			data[1][1] = "500";
			data[2][1] = "2";
			data[3][1] = "500";
			data[4][1] = "80";
			data[5][1] = "12";
			data[6][1] = "6";
			data[7][1] = "2000";
			data[8][1] = "4";
			data[9][1] = "4";
			data[10][1] = "4";
			data[11][1] = "0.7";
			data[12][1] = "6";
		}

		public int getColumnCount() {
			return data[0].length;
		}

		public int getRowCount() {
			return data.length; 		
		}
		
		public boolean isCellEditable(int row, int col){
			if(col==1)
				return true;
			else
				return false;
		}

		public Object getValueAt(int row, int col) {
			return (Object)data[row][col];
		}
		
		public void setValueAt(Object o, int row, int col){
			data[row][col] = (String)o;
			fireTableDataChanged();
		}
		
		public Class<? extends String> getColumnClass(int col){
			return data[0][col].getClass();
		}
		
		public String getColumnName(int col){
			return headings[col];
		}
	}
	
	

	private boolean checkSearchParameters(){
		return true;
	}
	
	private boolean checkCreationParameters(){
		return true;
	}
	
	
	
	public static void main(String[] args){
		SimulatorGUI s = new SimulatorGUI();
	}
}
