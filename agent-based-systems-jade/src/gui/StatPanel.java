package gui;

import java.awt.Graphics;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import basic.Map;
import basic.Position;


public class StatPanel extends JPanel implements Observer{
	
	JTextArea area;
	Map map;
	
	public StatPanel(Map theMap){
		map = theMap;
		area = new JTextArea();
		add(area);
		organizeComponents();
	}
	
	
	private void organizeComponents(){
		SpringLayout disp = new SpringLayout();
		setLayout(disp);
		SpringLayout.Constraints areaC = disp.getConstraints(area);
		areaC.setX(Spring.constant(10));
		areaC.setY(Spring.constant(10));
		areaC.setWidth(Spring.constant(600));
		areaC.setHeight(Spring.constant(500));
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if(map.getShowReport()==false && map.getBatteryLevel(0)==100)
			area.setText("");
		else if(map.getShowReport()==false)
			return;
		else if(map.getUncheckedSites().isEmpty() && map.getBatteryLevel(0)<100){
			area.append("Missione Compiuta!\n");
			HashSet<Position>[] checked = map.getCheckedSites();
			for(int i=0; i<checked.length; i++){
				area.append("robot "+(i+1)+" : visitato "+checked[i].size()+" siti. Dispersione dalla posizione iniziale: "+map.getDistance(i)+" \n");
			}
			map.setShowReport(false);
		}
		else if(map.areAllFinished() && map.getBatteryLevel(0)<100){
			area.append("Missione Fallita!\n");
			HashSet<Position>[] checked = map.getCheckedSites();
			for(int i=0; i<checked.length; i++){
				area.append("robot "+(i+1)+" : visitato "+checked[i].size()+" siti. Dispersione dalla posizione iniziale: "+map.getDistance(i)+" \n");
			}
			map.setShowReport(false);
		}
	}
	
	public void update(Observable o, Object arg){
		if(map.getShowReport()==true)
			repaint();
	}
	
	public void reset(){
		area.setText("");
	}
	
}
