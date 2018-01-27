package common;


import java.rmi.*;

public interface ClientManager extends Remote{

	public AccountUtente autentica(String userID, String password, String URL)throws RemoteException;
	public boolean registra(AccountUtente acc)throws RemoteException;
	public void disconnetti(String userID) throws RemoteException;

}
