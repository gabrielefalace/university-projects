package common;

import java.rmi.*;
import java.util.*;

public interface ArticoliManager extends Remote{

	public LinkedList<Articolo> getArticoli(String categoria) throws RemoteException;
	public Articolo getArticolo(String articoloID) throws RemoteException;
	public boolean inviaArticolo(Articolo a) throws RemoteException;
	public boolean faiOfferta(String articoloID, String userID, double prezzo) throws RemoteException;

}
