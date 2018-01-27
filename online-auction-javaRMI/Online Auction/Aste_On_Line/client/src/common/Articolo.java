package common;

import java.io.*;
import java.util.Date;

public class Articolo implements Serializable{

	private String articoloID;
	private String nomeIndicativo;
	private String descrizione;
	private String proprietarioID;
	private double prezzo;
	private boolean disponibile;
	private Data dataOraChiusura;
	static final long serialVersionUID = 1;

	/**
	  * Crea un Articolo con il campo disponibile a false ed il
	  * campo dataOraChiusura a null di default.
	  *
	  */
	public Articolo(String id, String nome, String desc, String prop, double p){
		articoloID = id;
		nomeIndicativo = nome;
		descrizione = desc;
		proprietarioID = prop;
		prezzo = p;
		disponibile = false;
		dataOraChiusura = null;
	}

	public String getNomeIndicativo(){
		return nomeIndicativo;
	}

	public String getDescrizione(){
		return descrizione;
	}

	public synchronized double getPrezzo(){
		return prezzo;
	}

	public String getArticoloID(){
		return articoloID;
	}

	public synchronized String getProprietarioID(){
		return proprietarioID;
	}

	public synchronized boolean getDisponibile(){
		return disponibile;
	}

	public synchronized void setDisponibile(boolean b){
		disponibile = b;
	}

	/**
	  * Consente di effettuare una offerta sull'articolo in modo
	  * atomico, per non avere problemi con l'eventuale aggiornamento
	  * del proprietario che avviene quando l'offerta va a buon fine.
	  * @param userID di tipo String che è l'ID dell'utente
	  * @param p di tipo double che è il prezzo proposto
	  * @return boolean che è true se l'offerta è andata a buon fine, false altrimenti
	  */
	public synchronized boolean eseguiOfferta(String userID, double p){
		if(p > prezzo && disponibile==true){
			proprietarioID = userID;
			prezzo = p;
			return true;
		}
		return false;
	}

	public void setDataChiusura(Data d){
		dataOraChiusura = d;
	}

	public Data getDataChiusura(){
		return dataOraChiusura;
	}

}
