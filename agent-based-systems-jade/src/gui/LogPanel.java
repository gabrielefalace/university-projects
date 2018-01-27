package gui;

import javax.swing.*;

import basic.Log;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class LogPanel extends JPanel implements Observer{
	
	private JTabbedPane tab;
	private JTextArea[] areas;
	private JScrollPane[] scroll;
	private int numAgents = 4;			//default
	private Log log;
	
	public LogPanel(int numAgents, Log log){
		super();
		this.numAgents = numAgents;
		this.log = log;
		initComponents();
		setComponents();
	}

	private void initComponents(){
		tab = new JTabbedPane();
		areas = new JTextArea[numAgents];
		scroll = new JScrollPane[numAgents];
		for(int i=0; i<numAgents; i++){
			areas[i] = new JTextArea(30, 70);
			areas[i].setText("");
			scroll[i] = new JScrollPane(areas[i]);
		}
	}
	
	private void setComponents(){
		add(tab);
		for(int i=0; i<numAgents; i++)
			tab.addTab("Agent "+(i+1), scroll[i]);
	}
	
	public void setNumAgents(int numAgents){
		this.numAgents = numAgents;
		tab.removeAll();
		// Come initComponents(), ma senza istanziare il TabbedPane 
		areas = new JTextArea[numAgents];
		scroll = new JScrollPane[numAgents];
		for(int i=0; i<numAgents; i++){
			areas[i] = new JTextArea(30, 70);
			areas[i].setText("");
			scroll[i] = new JScrollPane(areas[i]);
		}
		for(int i=0; i<numAgents; i++)
			tab.addTab("Agent "+(i+1), scroll[i]);
		
		log.reset();					// così causo l'update
	}
	
	public void update(Observable o, Object arg){
		String msg = (String)arg;
		if(msg.equalsIgnoreCase("LOG_RESET")){
			for(int id=0; id<numAgents; id++)
				areas[id].setText("");
		}
		else{
			int robotID = Integer.parseInt(msg.split(":")[1]);
			areas[robotID].append(log.getLastMessage(robotID)+"\n");
		}
	}
	
	
	Log getLog(){
		return log;
	}
}
