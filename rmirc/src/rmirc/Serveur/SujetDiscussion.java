package rmirc.Serveur;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import rmirc.Client.AffichageClient;
import rmirc.Interfaces.InterfaceAffichageClient;
import rmirc.Interfaces.InterfaceSujetDiscussion;

public class SujetDiscussion extends UnicastRemoteObject implements InterfaceSujetDiscussion {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6673626884454566833L;
	
	
	private final String _titre;
	private Set<InterfaceAffichageClient> _clients;
	
	public SujetDiscussion( String titre_sujet ) throws RemoteException {
		_titre = titre_sujet;
		_clients = new HashSet<InterfaceAffichageClient>();
	}
	
	@Override
	public synchronized void inscription(InterfaceAffichageClient client)
			throws RemoteException {

		if ( client != null ) {
			
		}
		
		if ( !_clients.contains(client) ) {
			this.diffuse("Channel '"+this._titre+"' --> "+client.get_identifiant()+" has joined the channel");
			_clients.add(client);
			client.affiche("Vous etes inscrit au sujet "+_titre+" ! :)");
		}
		
	}

	@Override
	public synchronized void desInscription(InterfaceAffichageClient client)
			throws RemoteException {

		if ( _clients.contains(client) ) {
			_clients.remove(client);
			client.affiche("Vous etes maintenant desinscrit du sujet "+_titre);
			this.diffuse("Channel '"+this._titre+"' --> "+client.get_identifiant() + " has left the channel");
		}
		else {
			client.affiche("[WTF] Vous n'etes pas inscrit au sujet "+_titre);
		}
		
	}

	@Override
	public synchronized void diffuse( String message ) throws RemoteException {

		try  {
		
			Iterator<InterfaceAffichageClient> iterator = _clients.iterator();
				
			while ( iterator.hasNext() ) {
				iterator.next().affiche( message );
			}
			
		}
		catch ( RemoteException e ) {
			System.out.println("Client inacessible");
		}
	
	
	}

	
	@Override
	public synchronized void message( InterfaceAffichageClient client, String message) throws RemoteException {

		if ( client == null ) {
			return;
		}
		
		if ( _clients.contains(client) ) {
			
			this.diffuse(client.get_identifiant()+"> "+message);
		
		}
		else {
			
			client.affiche("Channel '"+this._titre+"' --> You are not following this channel");
			
		}
		
	}
	
	@Override
	public Set<InterfaceAffichageClient> get_connected_clients() throws RemoteException {
		return _clients;
	}


	@Override
	public String get_titre() throws RemoteException {

		return _titre;
	}

	@Override
	public boolean ping() throws RemoteException {

		return true;
	}




	
	
}
