package common;

import java.io.*;

public class Data implements Serializable{

	private int mese;
	private int giorno;
	private int ore;
	private int minuti;
	private static final long serialVersionUID = 33;


	public Data(int giorno, int mese, int ore, int minuti){
		this.mese = mese;
		this.giorno = giorno;
		this.ore = ore;
		this.minuti = minuti;
	}

	public int getMese(){return mese;}

	public int getGiorno(){return giorno;}

	public int getOre(){return ore;}

	public int getMinuti(){return minuti;}

	/**
	  * Rappresenta la data come String.
	  * @return String che è la rappresentazione di una Data
	  */
	public String toString(){
		return giorno+"/"+mese+" "+ore+":"+minuti;
	}

	/**
	  * Consente di verificare se due date sono uguali, cioè se hanno stesso
	  * valore in tutti gli attributi.
	  * @param o di tipo Object che deve essere l'altra data da confrontare
	  * @return boolean che è true se le date sono uguali, false altrimenti
	  */
	public boolean equals(Object o){
		if(!(o instanceof Data))
			return false;
		Data d = (Data)o;
		if(this.giorno!=d.giorno || this.mese!=d.mese || this.ore!=d.ore || this.minuti!=d.minuti)
			return false;
		return true;
	}

	/**
	  * Consente il confronto tra due Data.
	  * @param d di tipo Data che è la data confrontata con this
	  * @return true se la Data this precede l'argomento d
	  */
	public boolean precede(Data d){
		if(this.mese < d.mese)
			return true;
		if(this.mese==d.mese && this.giorno < d.giorno)
			return true;
		if(this.mese==d.mese && this.giorno==d.giorno && this.ore < d.ore)
			return true;
		if(this.mese==d.mese && this.giorno==d.giorno && this.ore==d.ore && this.minuti < d.minuti)
			return true;
		return false;
	}

}