package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

import basic.*;

public class ControlPanel2 extends JPanel implements Observer{
	
	JLabel title, exec, agents, sites;
	JLabel[] labels;
	JProgressBar[] bars;
	SpringLayout disp;
	JButton reset, start;
	JComboBox agentCombo;
	JComboBox siteCombo;
	Color defaultColor;
	
	int state;
	LogPanel logPanel;
	Map map;
	
	public static final int IDLE = 0, RUNNING = 1;
	
	AgentManager manager;
	
	public ControlPanel2(AgentManager am, LogPanel logPanel){
		manager = am;
		this.logPanel = logPanel;
		
		state = IDLE;
		initComponents();
		setComponents();
		organizeLayout();
		setListeners();
		
		update(null, manager.getMap());
	}
	
	
	private void initComponents(){
		title = new JLabel("Agents battery Levels");
		bars = new JProgressBar[8];
		labels = new JLabel[8];
		reset = new JButton("Reset");
		start = new JButton("Start");
		exec = new JLabel("Execution Control");
		agents = new JLabel("Number of Agents");
		sites = new JLabel("Number of sites");
		siteCombo = new JComboBox();
		
		for(int u=50; u<300; u++)
			siteCombo.addItem(u+"");
		
		agentCombo = new JComboBox(new String[]{"8", "7", "6", "5", "4", "3", "2"});
		agentCombo.setSelectedItem("4");
		siteCombo.setSelectedItem(250+"");
			
		for(int j=0; j<bars.length; j++){
			bars[j] = new JProgressBar();
			labels[j] = new JLabel("Robot "+(j+1));
		}
		
		defaultColor = bars[0].getForeground();
		
		disp = new SpringLayout();
		setLayout(disp);
	}
	

	private void setComponents(){
		add(title);
		add(reset);
		add(start);
		add(exec);
		add(agentCombo);
		add(agents);
		add(sites);
		add(siteCombo);
		for(int i=0; i<bars.length; i++){
			bars[i].setMinimum(0);
			bars[i].setMaximum(100);
			bars[i].setValue(100);
			bars[i].setStringPainted(true);
			add(labels[i]);
			add(bars[i]);
		}
		
		title.setIcon(new ImageIcon("./icons/energy.png"));
		exec.setIcon(new ImageIcon("./icons/execution.png"));
		sites.setIcon(new ImageIcon("./icons/numSites.png"));
		agents.setIcon(new ImageIcon("./icons/agents.png"));
	}
	
	private void setListeners(){
		// START
		start.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				if(state==IDLE){
					state = RUNNING;
					manager.startAll();
				}
			}
		});
		
		// RESET
		reset.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				System.out.println("Unchecked sites vuota? "+manager.getMap().getUncheckedSites().isEmpty());
				System.out.println("Are all finished? "+manager.getMap().areAllFinished());
				if(manager.getMap().getUncheckedSites().isEmpty() || manager.getMap().areAllFinished()){
					state = IDLE;
					System.out.println("Ora chiamo manager.resetAll() ...");
					manager.resetAll();
				}
			}
		});
				
		siteCombo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				if(state==RUNNING)
					return;
				int numSites = Integer.parseInt((String)siteCombo.getSelectedItem());
				manager.setNumSites(numSites);
			}
		});
		
		agentCombo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				if(state==RUNNING)
					return;
				int numAgents = Integer.parseInt((String)agentCombo.getSelectedItem());
				manager.setNumAgents(numAgents);
				logPanel.setNumAgents(numAgents);
			}
		});
	}
	
	private void organizeLayout(){
		SpringLayout.Constraints[] vincoloLabel = new SpringLayout.Constraints[8];
		SpringLayout.Constraints[] vincoloBarra = new SpringLayout.Constraints[8];
		for(int i=0; i<bars.length; i++){
			vincoloLabel[i] = disp.getConstraints(labels[i]);
			vincoloBarra[i] = disp.getConstraints(bars[i]);
		}
		SpringLayout.Constraints vincoloTitolo = disp.getConstraints(title);
		Spring x = Spring.constant(20);
		Spring y = Spring.constant(35);
		vincoloTitolo.setX(x);
		vincoloTitolo.setY(y);
		
		vincoloLabel[0].setX(x);
		vincoloLabel[0].setY(Spring.sum(vincoloTitolo.getHeight(), Spring.sum(vincoloTitolo.getY(), Spring.constant(18))));
		vincoloBarra[0].setY(vincoloLabel[0].getY());
		vincoloBarra[0].setX(Spring.sum(vincoloLabel[0].getWidth(), Spring.sum(vincoloLabel[0].getX(), Spring.constant(10))));
		
		
		for(int k=1; k<bars.length; k++){
			vincoloLabel[k].setX(x);
			vincoloLabel[k].setY(Spring.sum(vincoloBarra[k-1].getHeight(), Spring.sum(vincoloBarra[k-1].getY(), Spring.constant(15))));
			vincoloBarra[k].setX(vincoloBarra[0].getX());
			vincoloBarra[k].setY(vincoloLabel[k].getY());
		}
		
		SpringLayout.Constraints vincoloExec = disp.getConstraints(exec);
		vincoloExec.setX(x);
		vincoloExec.setY(Spring.sum(Spring.sum(Spring.constant(30), vincoloBarra[bars.length-1].getY()), vincoloBarra[bars.length-1].getHeight()));
		
		SpringLayout.Constraints vincoloPause = disp.getConstraints(reset);
		vincoloPause.setX(x);
		vincoloPause.setY(Spring.sum(Spring.sum(Spring.constant(10), vincoloExec.getY()), vincoloExec.getHeight()));
		
		SpringLayout.Constraints vincoloStop = disp.getConstraints(start);
		vincoloStop.setY(vincoloPause.getY());
		vincoloStop.setX(Spring.sum(Spring.sum(Spring.constant(10), vincoloPause.getX()), vincoloPause.getWidth()));
		
		SpringLayout.Constraints vincoloAgents = disp.getConstraints(agents);
		vincoloAgents.setX(x);
		vincoloAgents.setY(Spring.sum(Spring.sum(Spring.constant(35), vincoloStop.getY()), vincoloStop.getHeight()));		
		
		SpringLayout.Constraints vincoloCombo = disp.getConstraints(agentCombo);
		vincoloCombo.setX(Spring.sum(Spring.sum(vincoloAgents.getX(), vincoloAgents.getWidth()), Spring.constant(10)));
		vincoloCombo.setY(vincoloAgents.getY());
		
		SpringLayout.Constraints vincoloSites = disp.getConstraints(sites);
		vincoloSites.setX(x);
		vincoloSites.setY(Spring.sum(Spring.sum(Spring.constant(35), vincoloCombo.getY()), vincoloCombo.getHeight()));		
		
		SpringLayout.Constraints vincoloSiteCombo = disp.getConstraints(siteCombo);
		vincoloSiteCombo.setX(vincoloCombo.getX());
		vincoloSiteCombo.setY(vincoloSites.getY());
		
	}
	
	public void update(Observable o, Object arg){
			if(arg!=null){
				map = (Map)arg;
				repaint();
			}
			else{
				/*
				for(int i=0; i<bars.length; i++){
					bars[i].setValue(100);
					bars[i].setString("100%");
					bars[i].setForeground(defaultColor);
				}
				*/
				repaint();
			}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		for(int i=0; i<bars.length; i++){
			if(i < manager.getNumAgents()){
				bars[i].setValue(map.getBatteryLevel(i));
				bars[i].setString(bars[i].getValue()+"%");
				if(bars[i].getValue()<25)
					bars[i].setForeground(Color.RED);
				else if(bars[i].getValue()<55)
					bars[i].setForeground(Color.ORANGE);
				else
					bars[i].setForeground(Color.GREEN);
			}
			else{
				bars[i].setForeground(Color.GRAY);
			}
		}
	}
}
