package enfip.epidemio.service.contrat;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServiceModificationRemote extends Remote, ServiceModification
{
    List<Resultat> ajouterNombreDeCas(List<SequenceDonneesEpidemiomlogique> requete) throws RemoteException;

    List<Resultat> ajouterTauxIncidence(List<SequenceDonneesEpidemiomlogique> requete) throws RemoteException;
    
    Resultat supprimerNombreDeCas(LigneEpidemiologique requete) throws RemoteException;
    
    Resultat supprimerTaux(LigneEpidemiologique requete) throws RemoteException;
    
    Integer supprimerNombreDeCas() throws RemoteException;
    
    Integer supprimerTaux() throws RemoteException;

}
