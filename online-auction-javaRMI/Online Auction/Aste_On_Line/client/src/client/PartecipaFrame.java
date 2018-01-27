package client;

import common.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

public class PartecipaFrame extends JFrame{

	private ArticoliManager am;
	private String articoloID, descrizione, userID;
	private JScrollPane descScroll;
	private JTextArea area;
	private JLabel titolo, prezzoCor, virgola, euro;
	private JTextField prezzo, offertaEuro, offertaCents;
	private JButton tasto;
	private PriceUpdater priceUpdater;
	private volatile boolean continua;

	public PartecipaFrame(ArticoliManager am, String id, String descriz, String userID){
		this.am = am;
		this.userID = userID;
		continua = true;
		articoloID = id;
		descrizione = descriz;
		try{
			if(am.getArticolo(articoloID).getDisponibile()==false){
				JOptionPane.showMessageDialog(null, "è un articolo di tua proprietà", "Non Disponibile", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		}
		catch(Exception err){
			err.printStackTrace();
		}
		setFrame();
		initComponenti();
		disponiComponenti();
		attaccaAscoltatori();
		setVisible(true);
		priceUpdater = new PriceUpdater(articoloID);
		priceUpdater.start();
	}

	private void setFrame(){
		setTitle("Finestra Partecipazione");
		setSize(320, 300);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	private void initComponenti(){
		titolo = new JLabel(articoloID);
		area= new JTextArea();
		area.setEditable(false);
		area.setText(descrizione);
		descScroll = new JScrollPane(area);
		prezzoCor = new JLabel("Prezzo attuale € ");
		virgola = new JLabel(",");
		euro = new JLabel("€");
		prezzo = new JTextField(10);
		prezzo.setEditable(false);
		offertaEuro = new JTextField(10);
		offertaCents = new JTextField(10);
		tasto = new JButton("OFFRI");
	}



	public void attaccaAscoltatori(){
		addWindowListener(new AscoltaChiusura());
		tasto.addActionListener(new AscoltaOffri());
	}



	private class PriceUpdater extends Thread{
		private String articoloID;
		public PriceUpdater(String id){
			articoloID = id;
		}
		public void run(){
			while(continua == true){
				try{
					sleep(1200);
					Articolo a = am.getArticolo(articoloID);
					Modifica m = new Modifica(a.getPrezzo());
					SwingUtilities.invokeLater(m);
				}
				catch(Exception err){
					err.printStackTrace();
				}
			}
		}
	}

	private class Modifica implements Runnable{
		private double price;
		public Modifica(double pr){
			price = pr;
		}
		public void run(){
			prezzo.setText(price+" ");
		}
	}


	private class AscoltaOffri implements ActionListener{
		public void actionPerformed(ActionEvent e){
			try{
				double po = prezzoOfferta();
				boolean b = am.faiOfferta(articoloID, userID, po);
			}
			catch(Exception err){
				err.printStackTrace();
			}
		}

		private double prezzoOfferta(){
			double value = 0.00;
			try{
				int euro = Integer.parseInt(offertaEuro.getText());
				String centesimi = offertaCents.getText();
				centesimi = centesimi.trim();
				if(centesimi == null || centesimi.equals(""))
					centesimi = "00";
				int cents = Integer.parseInt(centesimi);
				String valore = ""+euro+"."+cents;
				value = Double.parseDouble(valore);
			}
			catch(NumberFormatException err){
				JOptionPane.showMessageDialog(null, "", "", JOptionPane.WARNING_MESSAGE);
			}
			return value;
		}
	}

	private class AscoltaChiusura extends WindowAdapter{
		public void windowClosing(WindowEvent e){
			continua = false;
		}
	}

	private void disponiComponenti(){
		add(titolo);
		add(descScroll);
		add(prezzoCor);
		add(prezzo);
		add(offertaEuro);
		add(virgola);
		add(offertaCents);
		add(euro);
		add(tasto);
		SpringLayout disp = new SpringLayout();
		getContentPane().setLayout(disp);
		Spring xInit = Spring.constant(10);
		Spring yInit = Spring.constant(10);
		SpringLayout.Constraints vincoloTitolo = disp.getConstraints(titolo);
		SpringLayout.Constraints vincoloDesc = disp.getConstraints(descScroll);
		SpringLayout.Constraints vincoloPrezzoCor = disp.getConstraints(prezzoCor);
		SpringLayout.Constraints vincoloPrezzo = disp.getConstraints(prezzo);
		SpringLayout.Constraints vincoloOffertaE = disp.getConstraints(offertaEuro);
		SpringLayout.Constraints vincoloOffertaC = disp.getConstraints(offertaCents);
		SpringLayout.Constraints vincoloVirgola = disp.getConstraints(virgola);
		SpringLayout.Constraints vincoloTasto = disp.getConstraints(tasto);
		SpringLayout.Constraints vincoloEuro = disp.getConstraints(euro);
		vincoloTitolo.setX(xInit);
		vincoloTitolo.setY(yInit);
		vincoloTitolo.setWidth(Spring.constant(100));
		vincoloTitolo.setHeight(Spring.constant(20));
		vincoloDesc.setX(xInit);
		vincoloDesc.setY(Spring.sum(vincoloTitolo.getHeight(), yInit));
		vincoloDesc.setHeight(Spring.constant(150));
		vincoloDesc.setWidth(Spring.constant(200));
		vincoloPrezzoCor.setX(xInit);
		vincoloPrezzoCor.setY(Spring.sum(Spring.sum(vincoloDesc.getHeight(), vincoloDesc.getY()), Spring.constant(10)));
		vincoloPrezzoCor.setWidth(Spring.constant(100));
		vincoloPrezzoCor.setHeight(Spring.constant(20));
		vincoloPrezzo.setX(Spring.sum(vincoloPrezzoCor.getX(), vincoloPrezzoCor.getWidth()));
		vincoloPrezzo.setY(vincoloPrezzoCor.getY());
		vincoloPrezzo.setWidth(Spring.constant(50));
		vincoloPrezzo.setHeight(Spring.constant(20));
		vincoloOffertaE.setX(xInit);
		vincoloOffertaE.setY(Spring.sum(Spring.sum(vincoloPrezzoCor.getY(), vincoloPrezzoCor.getHeight()), Spring.constant(10)));
		vincoloOffertaE.setHeight(Spring.constant(20));
		vincoloOffertaE.setWidth(Spring.constant(30));
		vincoloVirgola.setX(Spring.sum(vincoloOffertaE.getX(), vincoloOffertaE.getWidth()));
		vincoloVirgola.setY(vincoloOffertaE.getY());
		vincoloVirgola.setWidth(Spring.constant(5));
		vincoloVirgola.setHeight(Spring.constant(20));
		vincoloOffertaC.setX(Spring.sum(vincoloVirgola.getX(), vincoloVirgola.getWidth()));
		vincoloOffertaC.setY(vincoloVirgola.getY());
		vincoloOffertaC.setHeight(Spring.constant(20));
		vincoloOffertaC.setWidth(Spring.constant(30));
		vincoloEuro.setX(Spring.sum(vincoloOffertaC.getX(), vincoloOffertaC.getWidth()));
		vincoloEuro.setY(vincoloOffertaC.getY());
		vincoloEuro.setWidth(Spring.constant(50));
		vincoloEuro.setHeight(Spring.constant(20));
		vincoloTasto.setX(Spring.sum(vincoloEuro.getX(), vincoloEuro.getWidth()));
		vincoloTasto.setY(vincoloEuro.getY());
		vincoloTasto.setWidth(Spring.constant(100));
		vincoloTasto.setHeight(Spring.constant(20));
	}
}