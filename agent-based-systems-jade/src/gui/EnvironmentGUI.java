package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import basic.*;

public class EnvironmentGUI extends JFrame{

	AgentManager manager;
	
	JSplitPane splitPane;
	JTabbedPane tabPane;
	MapPanel2 mp;
	ControlPanel2 cp;
	StatPanel sp;
	LogPanel lp;
	JMenuBar barra;
	
	JMenu rob_menu;
	JCheckBox naiveBox, clusterBox, intelligentBox;
	
	Log log;
	
	public EnvironmentGUI(Log log){	
		this.log = log;
		manager = new AgentManager(log);		// devo far avere il log agli agenti per scriverlo!
		setFrame();
		initComponents();
		setComponents();
		setObservers();
		setListeners();
		setVisible(true);
	}
	
	
	private void setFrame(){
		setTitle("Environment");
		setSize(1200,720);
		Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		
		float x = screenDim.width/2;
		float y = screenDim.height/2;
		x = x - getWidth()/2;
		y = y - getHeight()/2;
		
		int centerX = Math.round(x);
		int centerY = Math.round(y);
		
		setLocation(centerX, centerY);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
	}	
	
	
	private void initComponents(){
		mp = new MapPanel2(manager.getMap());
		lp = new LogPanel(manager.getNumAgents(), log);
		cp = new ControlPanel2(manager, lp);
		
		splitPane = new JSplitPane();
		tabPane = new JTabbedPane();
		sp = new StatPanel(manager.getMap());
		
		barra = new JMenuBar();
		rob_menu = new JMenu("Agents");
		
		naiveBox = new JCheckBox("Simple");
		clusterBox = new JCheckBox("Clustering");
		intelligentBox = new JCheckBox("Intelligent");
	}

	private void setObservers(){
		manager.getMap().addObserver(mp);
		manager.getMap().addObserver(cp);
		log.addObserver(lp);
	}
	
	private void setComponents(){
		setJMenuBar(barra);
		barra.add(rob_menu);
		rob_menu.add(naiveBox);
		rob_menu.add(clusterBox);
		rob_menu.add(intelligentBox);
		naiveBox.setSelected(true);
		clusterBox.setSelected(false);
		intelligentBox.setSelected(false);
		
		splitPane.setDividerSize(2);
		
		tabPane.addTab("Map", mp);
		tabPane.addTab("Log", lp);
		tabPane.addTab("Statistics", sp);
		tabPane.setIconAt(2, new ImageIcon("./icons/stat2.png"));
		tabPane.setIconAt(1, new ImageIcon("./icons/log.png"));
		tabPane.setIconAt(0, new ImageIcon("./icons/map.png"));
		
		add(splitPane);
		splitPane.setDividerLocation(340);
		splitPane.setDividerSize(0);
		splitPane.setRightComponent(tabPane);
		splitPane.setLeftComponent(cp);
		
		tabPane.setComponentAt(1, lp);
	}

	
	private void setListeners(){
		naiveBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				naiveBox.setSelected(true);
				clusterBox.setSelected(false);
				intelligentBox.setSelected(false);
				manager.setAgentType("NAIVE");
			}
		});
		clusterBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				naiveBox.setSelected(false);
				clusterBox.setSelected(true);
				intelligentBox.setSelected(false);
				manager.setAgentType("CLUSTERING");
			}
		});
		intelligentBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				naiveBox.setSelected(false);
				clusterBox.setSelected(false);
				intelligentBox.setSelected(true);
				manager.setAgentType("INTELLIGENT");
			}
		});
	}
	
}
