package enfip.epidemio.service.contrat;

import java.io.Serializable;

public class SequenceDonneesEpidemiomlogique extends LigneEpidemiologique implements Serializable
{

    private static final long serialVersionUID = 227L;

    private Integer[] valeurs;

    public SequenceDonneesEpidemiomlogique(Integer annee, Integer semaine, Integer identifiantPathologie)
    {
        super(annee, semaine, identifiantPathologie);
    }

    public SequenceDonneesEpidemiomlogique()
    {
        super();
    }

    public final Integer[] getValeurs()
    {
        return valeurs;
    }

    public final void setValeurs(Integer[] valeurs)
    {
        this.valeurs = valeurs;
    }

}