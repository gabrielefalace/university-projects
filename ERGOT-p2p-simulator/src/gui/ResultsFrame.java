package gui;

import java.awt.FlowLayout;

import javax.swing.*;

public class ResultsFrame extends JFrame{

	private JLabel l = new JLabel("Risultati Ricerca");
	private JTextArea area = new JTextArea(4, 40);
	
	
	public ResultsFrame(String msg){
		setTitle("Results");
		setSize(500, 170);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		add(l);
		l.setIcon(new ImageIcon("./icons/results.png"));
		add(new JScrollPane(area));
		area.setEditable(false);
		area.setText(msg);
		setLayout(new FlowLayout());
		setVisible(true);
	}
}
