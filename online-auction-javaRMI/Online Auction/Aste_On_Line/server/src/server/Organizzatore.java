package server;

import common.*;
import java.rmi.*;
import java.io.*;

public class Organizzatore {

	public static void main(String[] args){
		try{
			String directory = "c:/aste_on_line_FILE/";
			String fileUtentiURL = "c:/aste_on_line_FILE/utenti.dat";
			String fileArticoliURL = "c:/aste_on_line_FILE/articoli.dat";
			directory = directory.replace('/', File.separatorChar);
			fileUtentiURL = fileUtentiURL.replace('/', File.separatorChar);
			fileArticoliURL = fileArticoliURL.replace('/', File.separatorChar);
			File folder = new File(directory);
			File fileUtenti = new File(fileUtentiURL);
			File fileArticoli = new File(fileArticoliURL);
			ListaUtenti listaUtenti;
			ListaArticoli listaArticoli;
			if(!(fileArticoli.exists() && fileUtenti.exists())){
				folder.mkdirs();
				fileUtenti.createNewFile();
				fileArticoli.createNewFile();
				listaUtenti = new ListaUtenti();
				listaArticoli = new ListaArticoli();
			}
			else{
				ObjectInputStream inU = new ObjectInputStream(new FileInputStream(fileUtenti));
				ObjectInputStream inA = new ObjectInputStream(new FileInputStream(fileArticoli));
				Object a = inA.readObject();
				Object u = inU.readObject();
				listaArticoli = (ListaArticoli)a;
				listaUtenti = (ListaUtenti)u;
			}
			OrganizzatoreGUI oGUI = new OrganizzatoreGUI(listaUtenti, listaArticoli, fileUtenti, fileArticoli);
			if(System.getSecurityManager()==null)
				System.setSecurityManager(new RMISecurityManager());
			ClientManager_Impl cm = new ClientManager_Impl(listaUtenti);
			ArticoliManager_Impl am = new ArticoliManager_Impl(listaArticoli);
			Naming.rebind("rmi://127.0.0.1:1099/CLIENT_MANAGER", cm);
			Naming.rebind("rmi://127.0.0.1:1099/ARTICOLI_MANAGER", am);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}
