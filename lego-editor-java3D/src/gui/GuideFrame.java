package gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class GuideFrame extends JFrame{

	public GuideFrame(){
		super("Guida per l'utente");
		setSize(1247, 755);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//		getContentPane().setLayout(new BorderLayout());
		getContentPane().setBackground(Color.WHITE);
		
		JLabel guideLabel = new JLabel();
		guideLabel.setBackground(Color.WHITE);
		guideLabel.setIcon(new ImageIcon("./img/guida_utente.png"));
		add(guideLabel);
		
		setVisible(true);
	}
	
	public static void main(String[] args){
		new GuideFrame();
	}
	
}
