package rmirc.Fournisseur;

import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.List;

import rmirc.Interfaces.InterfaceAffichageClient;
import rmirc.Interfaces.InterfaceServeurForum;
import rmirc.Interfaces.InterfaceSujetDiscussion;
import rmirc.Serveur.SujetDiscussion;

public class FournisseurDeSujet {
	
	private List<InterfaceSujetDiscussion> _sujets_fournis;
	private Thread _thread_fermeture;
	
	private InterfaceServeurForum _serveur;
	
	public FournisseurDeSujet() {
		_sujets_fournis = new LinkedList<InterfaceSujetDiscussion>();
		_thread_fermeture = new ThreadFermeture(this);
		_serveur = null;
	}

	public List<InterfaceSujetDiscussion> get_sujets_fournis() {
		
		return _sujets_fournis;
		
	}
	
	public InterfaceServeurForum get_serveur() {
		return _serveur;
	}
	
	public void creer_sujets( String[] titres ) {
		
        if (System.getSecurityManager() == null) {
        	  
            System.setSecurityManager(new SecurityManager());
             
         }
         try {
         	
         	
         	String server_name = "TohuBohu";   	
             Registry registry = LocateRegistry.getRegistry(0);

             //String url = "rmi://" + InetAddress.getLocalHost().getHostAddress() + "/"+server_name+"_channel_"+titre;
             _serveur = (InterfaceServeurForum) registry.lookup(server_name);
             
             
             for ( int i=0; i<titres.length; i++ ) {
             	
             	String titre = titres[i];
 	            InterfaceSujetDiscussion channel = new SujetDiscussion( titre );
 	            
 	            
 	            if ( _serveur.enregistreSujet(channel) ) {
 	            	
 	                System.out.println("Sujet " + titre + " cree");
 	               _sujets_fournis.add(channel);
              	                
 	                
 	            }
 	            else {
 	                System.out.println("Impossible de creer " + titre + ". Sujet deja existant ?");
 	
 	            }
 	            
             }
             
             /* Prevoir fermeture des sujets en cas de fermeture */
             Runtime.getRuntime().addShutdownHook( _thread_fermeture );

              
         } catch (Exception e) {
         	
        	 _thread_fermeture.run();
             System.err.println("TohuBohu exception : ");
             
             e.printStackTrace();
             
         }
		
	}
	
    public static void main(String[] args) {
    	
    	String[] liste_nouveaux_sujets = null;
    	
    	if ( args.length > 0 ) {
    		liste_nouveaux_sujets = args;
    	}
    	

    	if ( liste_nouveaux_sujets == null ) {
    		System.out.println("Usage : "+ "FounisseurDeSujet" + " <new_channel_name> [<new_channel_name>] ...");
    		System.exit(1);
    	}
    	
    	
    	FournisseurDeSujet fds = new FournisseurDeSujet();
    	fds.creer_sujets( liste_nouveaux_sujets );
        
    }


}
