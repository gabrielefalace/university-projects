package server;

import common.*;
import java.util.*;
import javax.swing.table.*;

public class ModelloUtenti extends AbstractTableModel{

	private LinkedList<AccountUtente> lista;
	private String[] titoli = {"user ID", "pasword", "stato", "URL attuale"};
	private String[][] entries ;

	public ModelloUtenti(LinkedList<AccountUtente> l){
		lista = l;
		entries = new String[l.size()][4];
		int i=0;
		for(AccountUtente iter: lista){
			entries[i][0] = iter.getUserID();
			entries[i][1] = iter.getPassword();
			entries[i][2] = (iter.getDisattivato())?"disattivato":"attivato";
			entries[i][3] = (iter.getURL()==null)?"null":iter.getURL();
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


