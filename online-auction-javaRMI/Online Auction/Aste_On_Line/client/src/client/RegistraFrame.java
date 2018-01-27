package client;

import common.*;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;

public class RegistraFrame extends JFrame{

	private int port;
	private ArticoliManager am;
	private ClientManager cm;
	private JTextField userID;
	private JPasswordField password;
	private JButton bottone;
	private JTextArea preferenze;


	public RegistraFrame(ArticoliManager am, ClientManager cm, int port){
		this.cm = cm;
		this.am = am;
		this.port = port;
		setFrame();
		initComponenti();
		disponiComponenti();
		attaccaAscoltatori();
		setVisible(true);
	}

	private void setFrame(){
		setTitle("Registrazione");
		setSize(320, 500);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	private void initComponenti(){
		userID = new JTextField(20);
		password = new JPasswordField(20);
		bottone = new JButton(" Registra ");
		preferenze = new JTextArea(20, 20);
	}

	private void disponiComponenti(){
		getContentPane().setLayout(new FlowLayout());
		add(new JLabel("inserisci userID e password"));
		add(userID);
		add(password);
		add(new JLabel("Inserisci Nomi di articoli di interesse"));
		add(new JLabel("separati da \" ; \" " ));
		add(preferenze);
		add(bottone);

	}

	private void attaccaAscoltatori(){
		bottone.addActionListener(new AscoltaRegistrazione());
	}

	private class AscoltaRegistrazione implements ActionListener{
		public void actionPerformed(ActionEvent e){
			try{
				LinkedList<String> preferences = getPreferenze();
				boolean soloVuote = true;
				for(int i=0; i<preferences.size(); i++){
					if(!(preferences.get(i).trim().equals(""))){
						soloVuote = false;
						break;
					}
				}
				if(preferences==null || preferences.isEmpty() || soloVuote==true){
					JOptionPane.showMessageDialog(null, "Devi inserire le preferenze","Attenzione", JOptionPane.WARNING_MESSAGE);
					return;
				}
				if(userPassValidi() == false){
					JOptionPane.showMessageDialog(null, "Devi inserire userID e password","Attenzione", JOptionPane.WARNING_MESSAGE);
					return;
				}
				AccountUtente account = new AccountUtente(userID.getText(),
														new String(password.getPassword()),
														preferences);
				boolean esito = cm.registra(account);
				if(esito == true){
					dispose();
					new AccessoFrame(cm, am, port);
				}
				else{
					JOptionPane.showMessageDialog(null,"Problemi con i dati digitati", "Attenzione",JOptionPane.WARNING_MESSAGE);
				}
			 }
			 catch(Exception err){
				 JOptionPane.showMessageDialog(null,"Impossibile connettersi","Problemi con la rete", JOptionPane.WARNING_MESSAGE);
			 }
		}

		private LinkedList<String> getPreferenze(){
			String[] pref = preferenze.getText().split("\\s*;\\s*");
			LinkedList<String> l = new LinkedList<String>();
			for(int i=0; i<pref.length; i++){
				pref[i].toLowerCase();
				l.add(pref[i]);
			}
			return l;
		}

		private boolean userPassValidi(){
			String user = userID.getText();
			String pass = new String(password.getPassword());
			if(user==null || pass==null)
				return false;
			else if(user.trim().equals("") || pass.trim().equals(""))
				return false;
			else
				return true;
		}
	}
}
