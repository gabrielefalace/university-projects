package client;

import common.*;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.rmi.*;
import javax.swing.*;

public class AccessoFrame extends JFrame{

	private ArticoliManager am;
	private ClientManager cm;
	private JTextField userF;
	private JPasswordField passwordF;
	private JButton accedi, registra;
	private int registryPort;

	public AccessoFrame(ClientManager cm, ArticoliManager am, int registryPort){
		this.cm = cm;
		this.am = am;
		this.registryPort = registryPort;
		setFrame();
		disponiComponenti();
		attaccaAscoltatori();
		setVisible(true);
	}

	private void setFrame(){
		setTitle("Autentica");
		setSize(250, 150);
		setResizable(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void disponiComponenti(){
		userF = new JTextField(20);
		passwordF = new JPasswordField(20);
		accedi = new JButton("ACCEDI");
		registra = new JButton("REGISTRA");
		getContentPane().setLayout(new FlowLayout());
		add(new JLabel("Inserisci userID e password"));
		add(userF);
		add(passwordF);
		add(accedi);
		add(registra);
	}

	private void attaccaAscoltatori(){
		accedi.addActionListener(new AscoltaAccedi());
		registra.addActionListener(new AscoltaRegistra());
	}

	private class AscoltaRegistra implements ActionListener{
		public void actionPerformed(ActionEvent e){
			dispose();
			new RegistraFrame(am, cm, registryPort);
		}
	}

	private class AscoltaAccedi implements ActionListener{
		public void actionPerformed(ActionEvent e){
			try{
				String thisURL = "rmi://";
				InetAddress ind = InetAddress.getLocalHost();
				String user = userF.getText();
				thisURL = thisURL+ind.getHostAddress()+":"+registryPort+"/"+user;
				String pass = new String(passwordF.getPassword());
				AccountUtente acc = cm.autentica(user, pass, thisURL);
				if(acc != null){
					ListaArticoliWrapper lw = new ListaArticoliWrapper();
					ListaArticoliClient_Impl listaArticoliClient = new ListaArticoliClient_Impl(lw);
					Naming.rebind(thisURL, listaArticoliClient);
					dispose();
					new PrincipaleFrame(am, cm, lw, acc);
				}
			}
			catch(Exception err0){
				JOptionPane.showMessageDialog(null, "Problemi con il server remoto","Attenzione", JOptionPane.WARNING_MESSAGE);
			}
		}
	}

}
