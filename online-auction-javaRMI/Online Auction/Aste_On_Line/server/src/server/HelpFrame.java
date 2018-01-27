package server;

import common.*;
import javax.swing.*;
import java.awt.*;

public class HelpFrame extends JFrame{

	private JTextArea testo;
	private JScrollPane scroll;

	public HelpFrame(){
		testo = new JTextArea();
		Font f = new Font("Arial", Font.BOLD, 14);
		testo.setFont(f);

		String s = "PER ORGANIZZARE UN ASTA \n\n 1. premere su -Aggiorna Articoli- per rilevare \n gli articoli che gli utenti hanno inviato per \n la messa all asta \n\n 2. selezionare un articolo \n\n 3. cliccare su -Attiva- e , settare la data di \n chiusura dell asta come richiesto dalla finestra \n che compare \n\n 4. se si vuole visualizzare il nuovo stato dell articolo \n premere su -Aggiorna Articoli- nuovamente \n\n 5. CLICCA SU -NOTIFICA- PER AVVISARE GLI UTENTI";
		testo.setEditable(false);
		testo.setText("");
		testo.append(s);
		scroll = new JScrollPane(testo);
		add(scroll);
		setTitle("Help");
		setSize(400, 400);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}


}