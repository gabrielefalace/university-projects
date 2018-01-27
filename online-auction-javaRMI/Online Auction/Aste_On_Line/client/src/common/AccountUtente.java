package common;
import java.io.*;
import java.util.*;

public class AccountUtente implements Serializable{

	static final long serialVersionUID = 2;
	private String userID;
	private String password;
	private String URL;
	private LinkedList<String> articoliInteresse;
	private boolean utenteDisattivato;

	public AccountUtente(String u, String p, LinkedList<String> articoli){
		userID = u;
		password = p;
		articoliInteresse = articoli;
		utenteDisattivato = false;
		URL = null;
	}

	public String getUserID(){
		return userID;
	}

	public String getPassword(){
		return password;
	}

	public boolean getDisattivato(){
		return utenteDisattivato;
	}

	public void setDisattivato(boolean b){
		utenteDisattivato = b;
	}

	public void setURL(String newURL){
		URL = newURL;
	}

	public String getURL(){
		return URL;
	}

	public LinkedList<String> getArticoliInteresse(){
		return articoliInteresse;
	}

}
