package server;

import common.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

public class OrganizzatoreGUI extends JFrame{


	private ListaArticoli articoli;
	private ListaUtenti utenti;
	private ModelloArticoli modelloArticoli;
	private ModelloUtenti modelloUtenti;
	private JTable tabellaArticoli, tabellaUtenti;
	private JScrollPane scrollArticoli, scrollUtenti;
	private JButton aggiornaArticoli, rimuoviUtente, attiva, aggiornaUtenti, notifica, help;
	private JLabel articoliLabel, utentiLabel;
	private File fileUtenti, fileArticoli;
	private volatile boolean continua;

	public OrganizzatoreGUI(ListaUtenti cli, ListaArticoli art, File fileUtenti, File fileArticoli){
		articoli = art;
		utenti = cli;
		this.fileUtenti = fileUtenti;
		this.fileArticoli = fileArticoli;
		setFrame();
		initComponenti();
		disponiComponenti();
		attaccaAscoltatori();
		setVisible(true);
		continua = true;
		new AsteCloser().start();
	}

	private void setFrame(){
		setSize(950, 700);
		setResizable(false);
		setTitle("Organizzatore");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void initComponenti(){
		articoliLabel = new JLabel("Lista Articoli");
		add(articoliLabel);
		utentiLabel = new JLabel("Lista Utenti");
		add(utentiLabel);
		modelloArticoli = new ModelloArticoli(articoli.getArticoli("TUTTO"));
		modelloUtenti = new ModelloUtenti(utenti.getUtenti());
		tabellaArticoli = new JTable(modelloArticoli);
		tabellaUtenti = new JTable(modelloUtenti);
		tabellaArticoli.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabellaUtenti.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollArticoli = new JScrollPane(tabellaArticoli);
		add(scrollArticoli);
		scrollUtenti = new JScrollPane(tabellaUtenti);
		add(scrollUtenti);
		aggiornaArticoli = new JButton("Aggiorna Articoli");
		add(aggiornaArticoli);
		rimuoviUtente = new JButton("Disattiva Utente");
		add(rimuoviUtente);
		attiva = new JButton("Attiva Articolo");
		add(attiva);
		aggiornaUtenti = new JButton("Aggiorna Utenti");
		add(aggiornaUtenti);
		notifica = new JButton("Notifica Utenti");
		add(notifica);
		help = new JButton("Help");
		add(help);
	}

	private void disponiComponenti(){
		SpringLayout disp = new SpringLayout();
		getContentPane().setLayout(disp);
		SpringLayout.Constraints vincoloArticoliLabel = disp.getConstraints(articoliLabel);
		SpringLayout.Constraints vincoloUtentiLabel = disp.getConstraints(utentiLabel);
		SpringLayout.Constraints vincoloScrollArticoli = disp.getConstraints(scrollArticoli);
		SpringLayout.Constraints vincoloScrollUtenti = disp.getConstraints(scrollUtenti);
		SpringLayout.Constraints vincoloAggiornaArticoli = disp.getConstraints(aggiornaArticoli);
		SpringLayout.Constraints vincoloRimuoviUtente = disp.getConstraints(rimuoviUtente);
		SpringLayout.Constraints vincoloAttiva = disp.getConstraints(attiva);
		SpringLayout.Constraints vincoloHelp = disp.getConstraints(help);
		SpringLayout.Constraints vincoloAggiornaUtenti = disp.getConstraints(aggiornaUtenti);
		SpringLayout.Constraints vincoloNotifica = disp.getConstraints(notifica);
		vincoloArticoliLabel.setX(Spring.constant(10));
		vincoloArticoliLabel.setY(Spring.constant(10));
		vincoloArticoliLabel.setWidth(Spring.constant(100));
		vincoloArticoliLabel.setHeight(Spring.constant(20));
		vincoloScrollArticoli.setX(Spring.constant(10));
		vincoloScrollArticoli.setY(Spring.sum(vincoloArticoliLabel.getHeight(), Spring.sum(vincoloArticoliLabel.getY(), Spring.constant(10))));
		vincoloScrollArticoli.setWidth(Spring.constant(560));
		vincoloScrollArticoli.setHeight(Spring.constant(500));
		vincoloScrollUtenti.setX(Spring.sum(vincoloScrollArticoli.getX(), Spring.sum(vincoloScrollArticoli.getWidth(), Spring.constant(20))));
		vincoloScrollUtenti.setY(vincoloScrollArticoli.getY());
		vincoloScrollUtenti.setWidth(Spring.constant(340));
		vincoloScrollUtenti.setHeight(Spring.constant(500));
		Spring yBottoni = Spring.sum(vincoloScrollArticoli.getY(), Spring.sum(vincoloScrollArticoli.getHeight(), Spring.constant(20)));
		Spring w = Spring.constant(160);
		Spring h = Spring.constant(40);
		vincoloAggiornaArticoli.setX(Spring.constant(10));
		vincoloAggiornaArticoli.setY(yBottoni);
		vincoloAggiornaArticoli.setWidth(w);
		vincoloAggiornaArticoli.setHeight(h);
		vincoloAttiva.setX(Spring.sum(vincoloAggiornaArticoli.getX(), vincoloAggiornaArticoli.getWidth()));
		vincoloAttiva.setY(yBottoni);
		vincoloAttiva.setWidth(w);
		vincoloAttiva.setHeight(h);
		vincoloAggiornaUtenti.setX(vincoloScrollUtenti.getX());
		vincoloAggiornaUtenti.setY(yBottoni);
		vincoloAggiornaUtenti.setWidth(w);
		vincoloAggiornaUtenti.setHeight(h);
		vincoloRimuoviUtente.setX(Spring.sum(vincoloAggiornaUtenti.getX(), vincoloAggiornaUtenti.getWidth()));
		vincoloRimuoviUtente.setY(yBottoni);
		vincoloRimuoviUtente.setWidth(w);
		vincoloRimuoviUtente.setHeight(h);
		vincoloUtentiLabel.setX(vincoloScrollUtenti.getX());
		vincoloUtentiLabel.setY(vincoloArticoliLabel.getY());
		vincoloUtentiLabel.setWidth(vincoloArticoliLabel.getWidth());
		vincoloUtentiLabel.setHeight(vincoloArticoliLabel.getHeight());
		vincoloNotifica.setY(Spring.sum(vincoloRimuoviUtente.getY(), vincoloRimuoviUtente.getHeight()));
		vincoloNotifica.setX(vincoloRimuoviUtente.getX());
		vincoloNotifica.setWidth(w);
		vincoloNotifica.setHeight(h);
		vincoloHelp.setX(Spring.sum(Spring.sum(Spring.constant(15), vincoloAttiva.getX()), vincoloAttiva.getWidth()));
		vincoloHelp.setY(vincoloAttiva.getY());
		vincoloHelp.setWidth(vincoloAttiva.getWidth());
		vincoloHelp.setHeight(vincoloAttiva.getHeight());
	}

	private void attaccaAscoltatori(){
		aggiornaArticoli.addActionListener(new AscoltaAggiornaArticoli());
		aggiornaUtenti.addActionListener(new AscoltaAggiornaUtenti());
		attiva.addActionListener(new AscoltaAttivaArticolo());
		rimuoviUtente.addActionListener(new AscoltaEliminaUtente());
		notifica.addActionListener(new AscoltaNotifica());
		addWindowListener(new AscoltaChiusura());
		help.addActionListener(new AscoltaHelp());
	}

	private class AscoltaEliminaUtente implements ActionListener{
		public void actionPerformed(ActionEvent e){
			int riga = tabellaUtenti.getSelectedRow();
			if(riga < 0) return;
			utenti.rimuoviUtente((String)modelloUtenti.getValueAt(riga, 0));
			SwingUtilities.invokeLater(new AggiornaTabellaUtenti());
		}
	}

	private class AscoltaAttivaArticolo implements ActionListener{
		public void actionPerformed(ActionEvent e){
			int riga = tabellaArticoli.getSelectedRow();
			if(riga < 0) return;
			String aId =(String)modelloArticoli.getValueAt(riga, 0);
			Articolo art = articoli.getArticolo(aId);
			Calendar now = Calendar.getInstance();
			Date d = new Date();
			now.setTime(d);
			int mese = now.get(Calendar.MONTH) + 1; // i mesi sono numerati da 0 a 11
			int giorno = now.get(Calendar.DAY_OF_MONTH);
			int ora = now.get(Calendar.HOUR_OF_DAY);
			int minuti = now.get(Calendar.MINUTE);
			Data adesso = new Data(giorno, mese, ora, minuti);
			if(art.getDataChiusura()==null)
				new ImpostaDataFrame(art);
			else if(art.getDataChiusura().precede(adesso)){
				JOptionPane.showMessageDialog(null, "Questa asta è già conclusa", "Attenzione", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		}
	}


	private class AscoltaAggiornaUtenti implements ActionListener{
		public void actionPerformed(ActionEvent e){
				SwingUtilities.invokeLater(new AggiornaTabellaUtenti());
		}
	}

	private class AscoltaAggiornaArticoli implements ActionListener{
		public void actionPerformed(ActionEvent e){
				SwingUtilities.invokeLater(new AggiornaTabellaArticoli());
		}
	}

	private class AggiornaTabellaArticoli implements Runnable{
		public void run(){
			modelloArticoli = new ModelloArticoli(articoli.getArticoli("TUTTO"));
			tabellaArticoli = new JTable(modelloArticoli);
			scrollArticoli.setViewportView(tabellaArticoli);
		}
	}

	private class AggiornaTabellaUtenti implements Runnable{
		public void run(){
			modelloUtenti = new ModelloUtenti(utenti.getUtenti());
			tabellaUtenti = new JTable(modelloUtenti);
			scrollUtenti.setViewportView(tabellaUtenti);
		}
	}

	private class AscoltaNotifica implements ActionListener{

		public void actionPerformed(ActionEvent e){
			for(AccountUtente iter: utenti.getUtenti()){
				if(iter.getURL() != null){
					new ClientUpdater(iter, articoli).start();
				}
			}
		}
	}

	public class AscoltaChiusura extends WindowAdapter{
		public void windowClosing(WindowEvent e){
			try{
				continua = false;
				for(AccountUtente u: utenti.getUtenti())
					u.setURL(null);
				ObjectOutputStream outA = new ObjectOutputStream(new FileOutputStream(fileArticoli));
				ObjectOutputStream outU = new ObjectOutputStream(new FileOutputStream(fileUtenti));
				outA.writeObject(articoli);
				outU.writeObject(utenti);
			}
			catch(Exception err){
				err.printStackTrace();
			}
		}
	}


	private class AscoltaHelp implements ActionListener{
		public void actionPerformed(ActionEvent ev){
			new HelpFrame();
		}
	}

	private class ClientUpdater extends Thread{
		private AccountUtente utente;
		private ListaArticoli articoli;
		public ClientUpdater(AccountUtente u, ListaArticoli a){utente=u; articoli=a;}
		public void run(){
			LinkedList<Articolo> listaDaInviare = new LinkedList<Articolo>();
			try{
				ListaArticoliClient lista = (ListaArticoliClient)Naming.lookup(utente.getURL());
				LinkedList<String> preferenze = utente.getArticoliInteresse();
				LinkedList<Articolo> articles = articoli.getArticoli("TUTTO");
				for(Articolo art: articles){
					if(preferenze.contains(art.getNomeIndicativo()) && art.getDisponibile())
						listaDaInviare.add(art);
				}
				LinkedList<Articolo> vinte = articoli.getArticoliProprietà(utente.getUserID());
				for(Articolo i: vinte)
					if(i.getDataChiusura() != null && i.getDisponibile()==false)
						listaDaInviare.add(i);
				lista.aggiornaLista(listaDaInviare);
			}
			catch(Exception err){}
		}
	}

	private class AsteCloser extends Thread{
		public void run(){
			while(continua==true){
				try{
					sleep(20000);
					Calendar now = Calendar.getInstance();
					Date d = new Date();
					now.setTime(d);
					int mese = now.get(Calendar.MONTH) + 1; // i mesi sono numerati da 0 a 11
					int giorno = now.get(Calendar.DAY_OF_MONTH);
					int ora = now.get(Calendar.HOUR_OF_DAY);
					int minuti = now.get(Calendar.MINUTE);
					Data adesso = new Data(giorno, mese, ora, minuti);
					LinkedList<Articolo> articoliPresenti = articoli.getArticoli("TUTTO");
					if(articoliPresenti.isEmpty())
						continue;
					for(Articolo iter: articoliPresenti){
						if(iter.getDataChiusura().precede(adesso))
							iter.setDisponibile(false);
							/*
							 *  Aggiornamento dei Client a fine asta !
							 */
							for(AccountUtente u: utenti.getUtenti()){
								if(u.getURL() != null){
										new ClientUpdater(u, articoli).start();
								}
							}
					}
				}
				catch(Exception err){
					err.printStackTrace();
				}
			}
		}
	}

}
