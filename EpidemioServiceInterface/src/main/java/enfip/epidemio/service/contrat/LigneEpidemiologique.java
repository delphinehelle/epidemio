package enfip.epidemio.service.contrat;

import java.io.Serializable;

import comp.service.protocole.api.OVRequete;

public class LigneEpidemiologique extends OVRequete implements Serializable
{

    /**
     * serialVersionUID - long, DOCUMENTEZ_MOI
     */
    private static final long serialVersionUID = 6672404991899798089L;

    public static final String LIGNE_NULLE = "ERR-1001";

    public static final String ANNEE_NULLE = "ERR-1002";

    public static final String SEMAINE_NULLE = "ERR-1003";

    public static final String IDPATHO_NULLE = "ERR-1004";
    
    public static final String STAT_EXISTANTE = "ERR-2000";

    
    protected Integer annee;
    protected Integer semaine;
    protected Integer identifiantPathologie;

    public LigneEpidemiologique(Integer annee, Integer semaine, Integer identifiantPathologie)
    {
        super();
        this.annee = annee;
        this.semaine = semaine;
        this.identifiantPathologie = identifiantPathologie;
    }

    public LigneEpidemiologique()
    {
        super();
    }

    public final Integer getAnnee()
    {
        return annee;
    }

    public final Integer getSemaine()
    {
        return semaine;
    }

    public final Integer getIdentifiantPathologie()
    {
        return identifiantPathologie;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("LigneEpidemiologique [");
        if (annee != null)
        {
            builder.append("annee=");
            builder.append(annee);
            builder.append(", ");
        }
        if (semaine != null)
        {
            builder.append("semaine=");
            builder.append(semaine);
            builder.append(", ");
        }
        if (identifiantPathologie != null)
        {
            builder.append("identifiantPathologie=");
            builder.append(identifiantPathologie);
        }
        builder.append("]");
        return builder.toString();
    }

}