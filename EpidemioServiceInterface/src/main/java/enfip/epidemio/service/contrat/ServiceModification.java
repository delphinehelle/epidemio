package enfip.epidemio.service.contrat;

import java.util.List;

public interface ServiceModification
{
    List<Resultat> ajouterNombreDeCas(List<SequenceDonneesEpidemiomlogique> requete) throws Exception;

    List<Resultat> ajouterTauxIncidence(List<SequenceDonneesEpidemiomlogique> requete) throws Exception;
    
    Resultat supprimerNombreDeCas(LigneEpidemiologique requete) throws Exception;
    
    Resultat supprimerTaux(LigneEpidemiologique requete) throws Exception;

    Integer supprimerTousNombreDeCas() throws Exception;
    
    Integer supprimerTousTaux() throws Exception;
    
}
