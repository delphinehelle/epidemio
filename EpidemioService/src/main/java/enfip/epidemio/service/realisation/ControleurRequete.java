package enfip.epidemio.service.realisation;

import comp.service.protocole.api.Erreur;

import enfip.epidemio.service.contrat.LigneEpidemiologique;
import enfip.epidemio.service.contrat.Resultat;

public class ControleurRequete
{

    public static boolean verifier(LigneEpidemiologique requete, Resultat r)
    {
        boolean res = true;
        
        if (requete == null)
            res &= !(r.getListeErreur().add(new Erreur(LigneEpidemiologique.LIGNE_NULLE)));
        
        if (requete != null && requete.getAnnee() == null)
            res &= !(r.getListeErreur().add(new Erreur(LigneEpidemiologique.ANNEE_NULLE)));
        
        if (requete != null && requete.getSemaine() == null)
            res &= !(r.getListeErreur().add(new Erreur(LigneEpidemiologique.SEMAINE_NULLE)));
        
        if (requete != null && requete.getIdentifiantPathologie() == null)
            res &= !(r.getListeErreur().add(new Erreur(LigneEpidemiologique.IDPATHO_NULLE)));
        
        return res; 
    }
}
