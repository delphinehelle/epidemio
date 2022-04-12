package enfip.epidemio.coordination.batch.chargement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import comp.service.protocole.api.Erreur;

import enfip.epidemio.service.contrat.SequenceDonneesEpidemiomlogique;

public class DonneesBilan
{

    private int nbSucces = 0;

    private Map<SequenceDonneesEpidemiomlogique, List<Erreur>> anomalies = new HashMap<SequenceDonneesEpidemiomlogique, List<Erreur>>();

    public void incrementerSucces()
    {
        nbSucces++;
    }

    public void ajouterAnomalie(SequenceDonneesEpidemiomlogique ligne, List<Erreur> listeErreur)
    {
        this.anomalies.put(ligne, listeErreur);
    }

    public final int getNbSucces()
    {
        return nbSucces;
    }

    public final Map<SequenceDonneesEpidemiomlogique, List<Erreur>> getAnomalies()
    {
        return anomalies;
    }

}
