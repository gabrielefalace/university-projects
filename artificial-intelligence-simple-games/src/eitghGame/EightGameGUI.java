package eitghGame;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class EightGameGUI extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1325385972528424848L;
	private JTextArea[] boxes;
	private JButton startB;
	
	private Table table;
	private Table solutionTable;
	private Resolver resolver;
	
	private Color backgroundColor;
	
	public EightGameGUI(Table t){
		table = t;
		solutionTable = (Table) table.clone();
		resolver = new Resolver();
		boxes = new JTextArea[Table.DIMENSION*Table.DIMENSION];
		startB = new JButton("start");
		backgroundColor = new Color(57, 153, 0);
		setFrame();
		setComponent();
		setVisible(true);
	}

	private void setFrame() {
		setTitle("Eight Game");
		setSize(350, 400);
		setResizable(false);
		setLayout(new FlowLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);		
	}

	private void setComponent() {
		for(int i=0; i<boxes.length; i++){
			boxes[i] = new JTextArea(1, 1);
			boxes[i].setEditable(false);
			boxes[i].setForeground(Color.WHITE);
			boxes[i].setBackground(backgroundColor);
			boxes[i].setFont(new Font("Verdana", Font.BOLD, 85));
			add(boxes[i]);
		}
		add(startB);
		
		
		startB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				resolver.resolve(table);
				
				resolver.printSolution();
				
				new Displayer().start();
				
				System.out.println("La soluzione finale è: ");
				System.out.println(solutionTable.toString());
			}
		});
	}
	
	
	class Displayer extends Thread {
		
	  public void run(){
		  
		startB.setEnabled(false);  
		
		for(int h=resolver.path.size()-1; h>=0; h--){
			
			solutionTable = solutionTable.move(resolver.path.get(h));
			
			// display questa solutionTable
			int k = 0;
			for(int i=0; i<Table.DIMENSION; i++){
				for(int j=0; j<Table.DIMENSION; j++){
					int elem = solutionTable.table[i][j]; 
					if(elem!=9){
						boxes[k].setBackground(backgroundColor);
						boxes[k].setText("");
						boxes[k].setText(""+elem);
					}
					else {
						boxes[k].setText("");
						boxes[k].setBackground(Color.WHITE);
					}
					k++;
				}
			}
			try{
				Thread.sleep(500);
			}
			catch(Exception err){err.printStackTrace();}
			System.out.println("aggiornamento eseguito");
		}
		
		startB.setEnabled(true);
	  }
	}
	
}
