package client;

import common.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

public class PrincipaleFrame extends JFrame{

	private ArticoliManager am;
	private ClientManager cm;
	private ListaArticoliWrapper listaLocale;
	private JTable tabella;
	private JScrollPane tabellaScroll;
	private ModelloTabellaClient modello;
	private JButton partecipa, richiesta;
	private JLabel intestazione;
	private volatile boolean continua;
	private TableUpdater tableUpdater;
	private AccountUtente mySelf;

	public PrincipaleFrame(ArticoliManager am, ClientManager cm, ListaArticoliWrapper l, AccountUtente u){
		this.am = am;
		this.cm = cm;
		listaLocale = l;
		mySelf = u;
		continua = true;
		setFrame();
		initComponenti();
		disponiComponenti();
		attaccaAscoltatori();
		setVisible(true);

		// Recupero iniziale della lista degli articoli

		try{
			LinkedList<String> preferenze = mySelf.getArticoliInteresse();
			LinkedList<Articolo> lista = new LinkedList<Articolo>();
			LinkedList<Articolo> listaTot = am.getArticoli("TUTTO");
			for(Articolo iter: listaTot){
				if((preferenze.contains(iter.getNomeIndicativo()) && iter.getDisponibile()==true )||
				(iter.getDataChiusura()!=null && iter.getProprietarioID().equals(mySelf.getUserID()) && iter.getDisponibile()==false))
					lista.add(iter);
			}
			listaLocale.aggiornaLista(lista);
		}
		catch(Exception err){
			err.printStackTrace();
		}

		tableUpdater = new TableUpdater();
		tableUpdater.start();
	}

	public void setFrame(){
		setTitle("Asta On Line - "+mySelf.getUserID());
		setSize(700, 700);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	public void initComponenti(){
		modello = new ModelloTabellaClient(new LinkedList<Articolo>());
		tabella = new JTable(modello);
		tabella.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabellaScroll = new JScrollPane(tabella);
		intestazione = new JLabel("Articoli all' asta");
		partecipa = new JButton("Partecipa");
		richiesta = new JButton("Metti all'asta");
	}


	public void disponiComponenti(){
		add(intestazione);
		add(tabellaScroll);
		add(partecipa);
		add(richiesta);
		SpringLayout disp = new SpringLayout();
		getContentPane().setLayout(disp);
		SpringLayout.Constraints vincoloInt = disp.getConstraints(intestazione);
		SpringLayout.Constraints vincoloTabella = disp.getConstraints(tabellaScroll);
		SpringLayout.Constraints vincoloBottone = disp.getConstraints(partecipa);
		SpringLayout.Constraints vincoloRichiesta = disp.getConstraints(richiesta);
		vincoloInt.setX(Spring.constant(10));
		vincoloInt.setY(Spring.constant(10));
		vincoloInt.setWidth(Spring.constant(300));
		vincoloInt.setHeight(Spring.constant(30));
		vincoloTabella.setX(vincoloInt.getX());
		vincoloTabella.setY(Spring.sum(vincoloInt.getY(), vincoloInt.getHeight()));
		vincoloTabella.setWidth(Spring.constant(500));
		vincoloTabella.setHeight(Spring.constant(500));
		vincoloBottone.setX(vincoloTabella.getX());
		vincoloBottone.setY(Spring.sum(Spring.sum(vincoloTabella.getY(), vincoloTabella.getHeight()), Spring.constant(10)));
		vincoloBottone.setHeight(Spring.constant(50));
		vincoloBottone.setWidth(Spring.constant(100));
		vincoloRichiesta.setX(Spring.sum(vincoloBottone.getX(), vincoloBottone.getWidth()));
		vincoloRichiesta.setY(vincoloBottone.getY());
		vincoloRichiesta.setWidth(Spring.constant(160));
		vincoloRichiesta.setHeight(vincoloBottone.getHeight());
	}

	public void attaccaAscoltatori(){
		partecipa.addActionListener(new AscoltaPartecipa());
		richiesta.addActionListener(new AscoltaRichiesta());
		addWindowListener(new AscoltaChiusura());
	}

	private class AscoltaPartecipa implements ActionListener{
		public void actionPerformed(ActionEvent e){
			 int riga = tabella.getSelectedRow();
			 if(riga < 0)
			 	return;
			 String articoloID = (String)modello.getValueAt(riga, 0);
			 String nome = (String)modello.getValueAt(riga, 1);
			 new PartecipaFrame(am, articoloID, nome, mySelf.getUserID());
		}
	}


	private class TableUpdater extends Thread{
		public void run(){
			while(continua == true){
				try{
					LinkedList<String> preferenze = mySelf.getArticoliInteresse();
					LinkedList<Articolo> lista = listaLocale.getArticoli();
					SwingUtilities.invokeLater(new Modifica(lista));
					sleep(10000);
				}
				catch(Exception err){}
			}
		}
	}

	private class Modifica implements Runnable{
		private LinkedList<Articolo> lista;
		public Modifica(LinkedList<Articolo> l){lista=l;}
		public void run(){
				modello = new ModelloTabellaClient(lista);
				tabella = new JTable(modello);
				tabellaScroll.setViewportView(tabella);
		}
	}

	private class AscoltaChiusura extends WindowAdapter{
		public void windowClosing(WindowEvent e){
			continua = false;
			try{
				cm.disconnetti(mySelf.getUserID());
			}
			catch(Exception err){}
		}
	}

	private class AscoltaRichiesta implements ActionListener{
		public void actionPerformed(ActionEvent e){
			new RichiestaFrame(am, mySelf.getUserID());
		}
	}



}
