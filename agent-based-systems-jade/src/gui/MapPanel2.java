package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import basic.Map;
import basic.Position;


public class MapPanel2 extends JPanel implements Observer{

	Map map;
	static final Color BACKGROUND = new Color(147, 153, 90);
	
	public MapPanel2(Map map){
		super();
		this.map = map;
		setBackground(BACKGROUND);
	}
	
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		
		float width = getWidth()/100;
		float height = getHeight()/100;
		
		setBackground(BACKGROUND);		
		
		HashSet<Position>[] checked = map.getCheckedSites();
		int numAgents = map.getNumAgents();
		LinkedList<Position> unchecked = map.getUncheckedSites();
		
		g2d.setColor(Color.BLACK);
		for(int i=0; i<unchecked.size(); i++){
			Position currentSite = unchecked.get(i); 
			int x = Math.round(width*currentSite.col);	System.out.println(x);
			int y = Math.round(height*(100 - currentSite.row));	System.out.println(y);
			g2d.fillRect(x, y, 5, 5);
		}
		
		for(int j=0; j<numAgents; j++){
			g2d.setColor(map.getRobotColor(j));
			
			HashSet<Position> robotChecked = checked[j];
			for(Position pos: robotChecked){ 
				int x = Math.round(width*pos.col);	System.out.println(x);
				int y = Math.round(height*(100 - pos.row));	System.out.println(y);
				g2d.fillRect(x, y, 5, 5);			
			}
			
			Position robot_pos = map.getRobotPosition(j);
			if(robot_pos!=null){
				int robot_x = Math.round(width*robot_pos.col);
				int robot_y = Math.round(height*(100 -robot_pos.row));
				g2d.drawOval(robot_x, robot_y, 10, 10);
				g2d.setFont(new Font("Arial", Font.BOLD, 13));
				g2d.drawString("Agent "+(j+1), robot_x, robot_y);
			}
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable arg0, Object arg) {
		this.repaint();
	}
	
}
