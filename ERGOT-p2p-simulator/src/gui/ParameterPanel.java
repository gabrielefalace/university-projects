package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;

public class ParameterPanel extends JPanel{

	private JTable table;
	private JScrollPane scroll;
	private ParameterModel model;
	
	public ParameterPanel(){
		initComponents();
		setComponents();
		locateComponents();
		setListeners();
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
	}
	
	private void setComponents(){
		add(scroll);
	}
	
	private void locateComponents(){
		setLayout(new FlowLayout());
	}
	
	private void setListeners(){
	}
	
	
	//******************************************************************** //
	//************************* ASCOLTATORI ****************************** //
	//******************************************************************** //
	
	private class SONListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			
		}
	}
	
	private class DHTListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			
		}
	}	
	
	//******************************************************************** //
	//*******************  MODELLO DEI DATI  ***************************** //
	//******************************************************************** //
	
	private class ParameterModel extends AbstractTableModel{
		
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
	
	
	public static void main(String[] args){
		JFrame f = new JFrame();
		f.add(new ParameterPanel());
		f.setSize(400, 400);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
