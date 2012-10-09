package rmirc.Client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.Set;

import rmirc.Interfaces.InterfaceAffichageClient;
import rmirc.Interfaces.InterfaceServeurForum;
import rmirc.Interfaces.InterfaceSujetDiscussion;


public class AffichageClient extends UnicastRemoteObject implements InterfaceAffichageClient {


    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1582318348755837843L;
	
	public final String DEFAULT_NAME = "Anonymous";
	
	private String _pseudo;
	

	public AffichageClient() throws RemoteException {
    	_pseudo = DEFAULT_NAME;
    }
	
	public AffichageClient( String pseudo ) throws RemoteException {
		_pseudo = pseudo;
	}

	@Override
	public void affiche(String message) throws RemoteException {
		
		System.out.println("Affiche : " + message);
		
	}    
	
    @Override
    public String get_identifiant()  throws RemoteException {
    	
    	return _pseudo;
    	
    }
	
	public static String wait_for_cmd() {
		
	      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	      String cmd = "";

	      try {
	    	  cmd = br.readLine();
	      } catch (IOException ioe) {
	         System.out.println("IO error trying to read argument!");
	         System.exit(1);
	      }
	      
	      return cmd;
	      
	}
	
    public static void main( String args[] ) {
    	
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "TohuBohu";
            Registry registry = LocateRegistry.getRegistry(0);
                       
            InterfaceServeurForum forum = (InterfaceServeurForum) registry.lookup(name);
 
            AffichageClient ac = null;
            

            if ( args.length > 0 ) {
            	ac = new AffichageClient(args[0]);
            }
            else {
            	ac = new AffichageClient();
            }
            
            String cmd = "";
            while ( ( cmd = wait_for_cmd() ) != "exit" ) {
            	
            	if ( cmd.equals("/help") || cmd.equals("help") ) {
            		System.out.println("Available commands : ");
            		System.out.println("	/join <subject>");
            		System.out.println("	/part <subject>");
            		System.out.println("	/msg <subject> <message>");
            		System.out.println("	/list <subject>");

            	}
            	else {
            		
            		String[] spl = cmd.split(" ");
            		
            		if ( spl.length > 1 ) {
            			
            			InterfaceSujetDiscussion suj = forum.obtientSujet(spl[1]);
            			
             			if ( spl[0].equals("/join") ) {
            				
             				
             				if ( suj == null ) {
                    			//InterfaceSujetDiscussion suj = forum.obtientSujet(spl[1]);

             					//forum.enregistreSujet(spl[1]);
             				}
             				else {
             				
	            				try {
	            					suj.inscription(ac);
	            				}
	            				catch ( ConnectException e ) {
	            					System.out.println("Connexion refusee. Le sujet n'existe pas");
	            					
	            				}
            				
             				}
            				
            			}
             			
             			else if ( suj != null ) {
	            			
	       
	            			if ( spl[0].equals("/part") ) {
	            				suj.desInscription(ac);

	            			}
	            			else if ( spl[0].equals("/msg") && spl.length > 2 ) {
	            				
	            				// On peut tester si le Serveur est bien toujours la 
	            				try {
	            					
	            					suj.message(ac,spl[2]);
	            				}
	            				catch ( ConnectException e ) {
	            					System.out.println("Erreur : Connexion avec le canal perdue. Tentez de rejoindre le canal.");
	            				}
	            				
	            			}
	            			else if ( spl[0].equals("/list") ) {
	            				
	            				// On peut tester si le Serveur est bien toujours la 
	            				try {
	            					
	            					Set<InterfaceAffichageClient> clients = suj.get_connected_clients();
	            					Iterator<InterfaceAffichageClient> iter = clients.iterator();
	            					
	            					System.out.println("Connected clients :");
	            					while ( iter.hasNext() ) {
	            						System.out.println("\t- "+iter.next().get_identifiant());
	            					}
	            					
	            				}
	            				catch ( ConnectException e ) {
	            					System.out.println("Erreur : Connexion avec le canal perdue. Tentez de rejoindre le canal.");
	            				}
	            				
	            			}
	            			
            			}
            			else {
            				System.out.println(spl[1]+" : Subject not found !");
            			}
            			
            			
            		}
            		
            		
            	}
        
            	
            }
            
            
        } catch (Exception e) {
            System.err.println("Client exception:");
            e.printStackTrace();
        }
    }
    

    
}
