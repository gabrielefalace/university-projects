package gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class AboutFrame extends JFrame{

	public AboutFrame(){
		super("About this software");
		setSize(350, 200);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().setBackground(Color.WHITE);
		
		JLabel aboutLabel = new JLabel();
		aboutLabel.setIcon(new ImageIcon("./img/about.png"));
		add(aboutLabel);
		
		setVisible(true);
	}
	
	public static void main(String[] args){
		new AboutFrame();
	}
	
}
