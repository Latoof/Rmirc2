package rmirc.Fournisseur;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

import rmirc.Interfaces.InterfaceServeurForum;
import rmirc.Interfaces.InterfaceSujetDiscussion;

public class ThreadFermeture extends Thread {

	private FournisseurDeSujet _fournisseur;
	
	public ThreadFermeture(FournisseurDeSujet fournisseurDeSujet) {
		
		_fournisseur = fournisseurDeSujet;
		
	}
	
	@Override
	public void run() {
		
		InterfaceServeurForum srv = _fournisseur.get_serveur();
		
		if ( srv != null ) {
		
			 List<InterfaceSujetDiscussion> lsf = _fournisseur.get_sujets_fournis();
			 Iterator<InterfaceSujetDiscussion> iter = lsf.iterator();
			 
			 while ( iter.hasNext() ) {
				 
				 try {
					srv.supprimeSujet( iter.next() );
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				 
			 }
		 
		}
			
	}
	
}
