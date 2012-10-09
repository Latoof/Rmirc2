package rmirc.Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

import rmirc.Client.AffichageClient;


public interface InterfaceSujetDiscussion extends Remote {

	public void inscription( InterfaceAffichageClient iface_client ) throws RemoteException;
	
	public void desInscription( InterfaceAffichageClient iface_client ) throws RemoteException;
	
	public void diffuse( String message ) throws RemoteException;
	
	public void message( InterfaceAffichageClient client, String message ) throws RemoteException;
	
	public String get_titre() throws RemoteException;
	
	public boolean ping() throws RemoteException;
	
	public Set<InterfaceAffichageClient> get_connected_clients() throws RemoteException; 
	
}
