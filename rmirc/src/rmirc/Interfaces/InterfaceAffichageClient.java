package rmirc.Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceAffichageClient extends Remote {

	public void affiche( String message ) throws RemoteException;
	
	public String get_identifiant() throws RemoteException;
	
}
