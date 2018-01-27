
package common;

import java.rmi.*;
import java.util.*;

public interface ListaArticoliClient extends Remote{

	public void aggiornaLista(LinkedList<Articolo> lista) throws RemoteException;

}
