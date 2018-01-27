package server;

import common.*;
import java.util.*;
import javax.swing.table.*;

public class ModelloArticoli extends AbstractTableModel{

	private LinkedList<Articolo> lista;
	private String[] titoli = {"Articolo ID", "Nome Indicativo", "Descrizione", "Proprietario", "prezzo", "disponibilità", "chiusura"};
	private String[][] entries ;

	public ModelloArticoli(LinkedList<Articolo> l){
		lista = l;
		entries = new String[l.size()][7];
		int i=0;
		for(Articolo iter: lista){
			entries[i][0] = iter.getArticoloID();
			entries[i][1] = iter.getNomeIndicativo();
			entries[i][2] = iter.getDescrizione();
			entries[i][3] = iter.getProprietarioID();
			entries[i][4] = iter.getPrezzo()+"";
			entries[i][5] = (iter.getDisponibile())?"si":"no";
			entries[i][6] = (iter.getDataChiusura()==null)?"attivabile":iter.getDataChiusura().toString();
			i++;
		}
	}


	public int getRowCount() {
		return lista.size();
	}

	public int getColumnCount() {
		return titoli.length;
	}

	public Object getValueAt(int arg0, int arg1) {
		return entries[arg0][arg1];
	}

	public Class getColumnClass(int c){
		return entries[0][c].getClass();
	}

	public String getColumnName(int c){
		return titoli[c];
	}

}

