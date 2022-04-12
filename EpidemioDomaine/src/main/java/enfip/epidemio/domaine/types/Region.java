package enfip.epidemio.domaine.types;

public class Region
{

    private Integer id;

    private String nom;

    public Region(Integer id, String nom)
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
