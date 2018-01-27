package client;

import common.*;
import java.rmi.*;

public class Client{

	public static void main(String [] args){

		try{
			ClientManager cm = (ClientManager)Naming.lookup("rmi://localhost/CLIENT_MANAGER");
			ArticoliManager am = (ArticoliManager)Naming.lookup("rmi://localhost/ARTICOLI_MANAGER");
			new AccessoFrame(cm, am, 2000);
		}
		catch(Exception err){
			err.printStackTrace();
		}

	}

}