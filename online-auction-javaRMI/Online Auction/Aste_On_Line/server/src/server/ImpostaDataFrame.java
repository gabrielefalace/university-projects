package server;

import common.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ImpostaDataFrame extends JFrame{

	private Articolo articolo;
	private JTextField giorno, mese, ore, minuti;
	private JButton conferma;

	public ImpostaDataFrame(Articolo a){
		articolo = a;
		setFrame();
		initComponenti();
		disponiComponenti();
		attaccaAscoltatori();
		setVisible(true);
	}

	private void setFrame(){
		setTitle(articolo.getArticoloID()+" : data");
		setSize(250, 300);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	private void initComponenti(){
		giorno = new JTextField(20);
		mese = new JTextField(20);
		ore = new JTextField(20);
		minuti = new JTextField(20);
		conferma = new JButton("conferma");
	}

	private void disponiComponenti(){
		getContentPane().setLayout(new FlowLayout());
		add(new JLabel("giorno"));
		add(giorno);
		add(new JLabel("mese"));
		add(mese);
		add(new JLabel("ore"));
		add(ore);
		add(new JLabel("minuti"));
		add(minuti);
		add(conferma);

	}

	private void attaccaAscoltatori(){
		conferma.addActionListener(new AscoltaConferma());
	}

	private class AscoltaConferma implements ActionListener{
		public void actionPerformed(ActionEvent ev){
			try{
				int day = Integer.parseInt(giorno.getText());
				int month = Integer.parseInt(mese.getText());
				int hour = Integer.parseInt(ore.getText());
				int minutes = Integer.parseInt(minuti.getText());
				if(day < 1 || day > 31 || month < 1 || month > 12 || hour > 24
				   || hour < 0 || minutes > 60 || minutes < 0)
				   return;
				Data d = new Data(day, month, hour, minutes);
				articolo.setDataChiusura(d);
				articolo.setDisponibile(true);
				dispose();
			}
			catch(Exception err){
				err.printStackTrace();
			}
		}
	}

}