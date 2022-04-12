package enfip.epidemio.service.contrat.types;

import java.io.Serializable;

public class PathologieVO implements Serializable
{
    /**
     * serialVersionUID - long, DOCUMENTEZ_MOI
     */
    private static final long serialVersionUID = -2096909396561712726L;

    private Integer id;
    
    private String nom;

    public PathologieVO(Integer id, String nom)
    {
        super();
        this.id = id;
        this.nom = nom;
    }

    public final Integer getId()
    {
        return id;
    }

    public final String getNom()
    {
        return nom;
    }
    
}
