package rmirc.Serveur;

import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import rmirc.Interfaces.InterfaceServeurForum;
import rmirc.Interfaces.InterfaceSujetDiscussion;

public class ServeurForum extends UnicastRemoteObject implements InterfaceServeurForum {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2644046911738009139L;
	

	private final String[] DEFAULTS_SUJETS = { "musique", "cinema", "pr0n", "poneys", "ornithorynques" };
	
	private Map<String,InterfaceSujetDiscussion> _sujets;
	
	public ServeurForum() throws RemoteException {
		super();
		_sujets = new HashMap<String,InterfaceSujetDiscussion>();
		
		/* // Decommentez pour activer des cannaux par defaut sur le serveur principal 
		for ( int i=0; i<DEFAULTS_SUJETS.length; i++ ) {
			
			String titre = DEFAULTS_SUJETS[i];
			_sujets.put(titre, new SujetDiscussion(titre));
		}
		*/
		
	}
	

	@Override
	public InterfaceSujetDiscussion obtientSujet(String titre_sujet)
			throws RemoteException {
		
		/* Default subjects */
		if ( _sujets.containsKey(titre_sujet) ) {
			
			InterfaceSujetDiscussion sujet = _sujets.get(titre_sujet);
			
			if ( this.verifierPresenceSujet(sujet) ) {
				return sujet;
			}

		}
		
		return null;
	}
	
	@Override
	public boolean verifierPresenceSujet( InterfaceSujetDiscussion sujet ) throws RemoteException {
		
		
		if ( sujet != null && _sujets.containsValue(sujet) ) {
			
			/* Le le PING echoue, le Sujet n'est pas renvoye */
			try {

				if ( sujet.ping() == true ) {
					System.out.println("Found.");

					return true;
				}
			}
			catch ( RemoteException e ) {
				
				System.out.println("Connection lost with a provider");
		
			}
			
			this.supprimeSujet(sujet);
		
		}
		
		return false;
		
	}
	

	@Override
	public boolean enregistreSujet( InterfaceSujetDiscussion sujet )
			throws RemoteException {
		
		/* Default subjects */
		if ( !_sujets.containsValue(sujet) ) {
			_sujets.put(sujet.get_titre(), sujet);
			return true;
		}
			
		
		return false;
	}
	
	
	@Override
	public boolean supprimeSujet(InterfaceSujetDiscussion sujet)
			throws RemoteException {
		
	
		if ( _sujets.containsValue(sujet) ) {
			sujet.diffuse("Sujet \""+sujet.get_titre()+"\" ferme !");
			_sujets.remove(sujet.get_titre());
			return true;
		}
		
	
		return false;
	}
	
	
    public static void main(String[] args) {
    	
        if (System.getSecurityManager() == null) {
  
           System.setSecurityManager(new SecurityManager());
            
        }
        try {
        	
            String name = "TohuBohu";
            
            String url = "rmi://" + InetAddress.getLocalHost().getHostAddress() + "/"+name;
            System.out.println("Binding to url : " + url);
            
            InterfaceServeurForum forum = new ServeurForum();
            
            /*
            InterfaceServeurForum stub =
                (InterfaceServeurForum) UnicastRemoteObject.exportObject(forum, 0);
                */
            Registry registry = LocateRegistry.getRegistry(0);
            Naming.rebind(url, forum);
            
            System.out.println("Serveur TohuBohu j'ecoute !");
            
        } catch (Exception e) {
        	
            System.err.println("MyOwn exception : ");
            e.printStackTrace();
            
        }
        
    }






}
