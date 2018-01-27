package gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import basic.Map;
import basic.Position;


public class MapPanel extends JPanel implements Observer{

	Map map;
	JPanel[][] mapArray;
	static final Color BACKGROUND = new Color(147, 153, 90);
	
	public MapPanel(Map map){
		super();
		this.map = map;
		mapArray = new JPanel[map.dim][map.dim];
		
		setComponents();
	}
	
	
	private void setComponents(){
		setLayout(new GridLayout(100,100));
		for(int i=0; i<mapArray.length; i++){
			for(int j=0; j<mapArray[0].length; j++){
				mapArray[i][j] = new JPanel();
				add(mapArray[i][j], i, j);
				mapArray[i][j].setBackground(BACKGROUND);
			}
		}
	}
	
	
	public void update(Observable arg0, Object arg) {
		if(arg==null){
			for(int i=0; i<mapArray.length; i++)
				for(int j=0; j<mapArray[0].length; j++)
					mapArray[i][j].setBackground(BACKGROUND);
		}
		else{	
			map = (Map)arg;
			LinkedList<Position> unchecked = map.getUncheckedSites(); 
			for(Position p:unchecked)
				mapArray[p.row][p.col].setBackground(Color.BLACK);
		
			HashSet<Position>[] checked = map.getCheckedSites();
		
			for(int i=0; i<map.getNumAgents(); i++){
				Position curr = map.getRobotPosition(i);
				if(curr!=null)	
					mapArray[curr.row][curr.col].setBackground(map.getRobotColor(i));
				
				HashSet<Position> set = checked[i];
				for(Position p:set){
					int r = p.row;
					int c = p.col;
					Color col = map.getRobotColor(i);
					System.out.println("Agente "+i+" : colore "+col.toString());
					mapArray[r][c].setBackground(col);
				}
			}
		}
	}
	
}
