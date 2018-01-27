package client;

import common.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class RichiestaFrame extends JFrame{

	private ArticoliManager am;
	private JTextField idField, nomeField, descrizioneField, prezzoField;
	private JLabel idLabel, nomeLabel, descrizioneLabel, prezzoLabel;
	private JButton invia;
	private String userID;

	public RichiestaFrame(ArticoliManager am, String userID){
		this.am = am;
		this.userID = userID;
		setFrame();
		initComponenti();
		disponiComponenti();
		attaccaAscoltatori();
		setVisible(true);
	}

	private void setFrame(){
		setTitle("Richiesta");
		setSize(300, 300);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	private void initComponenti(){
		idField = new JTextField(25);
		nomeField = new JTextField(25);
		descrizioneField = new JTextField(25);
		prezzoField = new JTextField(25);
		idLabel = new JLabel("ID articolo");;
		nomeLabel = new JLabel("nome indicativo");
		descrizioneLabel = new JLabel("descrizione");
		prezzoLabel = new JLabel("prezzo base");
		invia = new JButton("invia");
	}

	private void disponiComponenti(){
		getContentPane().setLayout(new FlowLayout());
		add(idLabel);
		add(idField);
		add(nomeLabel);
		add(nomeField);
		add(descrizioneLabel);
		add(descrizioneField);
		add(prezzoLabel);
		add(prezzoField);
		add(invia);
		prezzoField.setToolTipText("euro e cents separati da PUNTO");
	}

	private void attaccaAscoltatori(){
			invia.addActionListener(new AscoltaInvio());
	}

	private boolean datiValidi(){
		return true;
	}

	private class AscoltaInvio implements ActionListener{
		public void actionPerformed(ActionEvent ev){
			try{
				String id = idField.getText();
				String nome = nomeField.getText();
				nome.toLowerCase();
				String desc = descrizioneField.getText();
				String pr = prezzoField.getText();
				double prezzo = Double.parseDouble(pr);
				String proprietario = userID;
				Articolo a = new Articolo(id, nome, desc, proprietario, prezzo);
				boolean esito = am.inviaArticolo(a);
				if(esito == false)
					throw new Exception();
				else
					dispose();
			}
			catch(Exception err){
				JOptionPane.showMessageDialog(null,"Dati Non Validi","Controlla i dati ed eventualmente cambia ID", JOptionPane.WARNING_MESSAGE);
			}
		}
	}


}
