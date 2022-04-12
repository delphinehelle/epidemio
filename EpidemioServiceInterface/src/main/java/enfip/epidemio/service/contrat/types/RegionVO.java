package enfip.epidemio.service.contrat.types;

import java.io.Serializable;

public class RegionVO implements Serializable
{
    /**
     * serialVersionUID - long, DOCUMENTEZ_MOI
     */
    private static final long serialVersionUID = -7028390418469552418L;

    private Integer id;

    private String nom;
  
    public RegionVO(Integer id, String nom)
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
