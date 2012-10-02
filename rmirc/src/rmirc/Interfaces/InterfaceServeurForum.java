package rmirc.Interfaces;
import java.rmi.Remote;
import java.rmi.RemoteException;


public interface InterfaceServeurForum extends Remote {

	public InterfaceSujetDiscussion obtientSujet( String titre_sujet ) throws RemoteException;
	
	/* Quand un fournisseur fournit un sujet */
	public boolean enregistreSujet( InterfaceSujetDiscussion sujet ) throws RemoteException;
	
	/* Quand un fournisseur detruit un sujet (ou s'il se coupe) */
	public boolean supprimeSujet( InterfaceSujetDiscussion sujet ) throws RemoteException;
	
	public boolean verifierPresenceSujet( InterfaceSujetDiscussion sujet ) throws RemoteException;
	
}
