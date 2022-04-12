package enfip.epidemio.domaine.types;


public class Pathologie 
{

    private Integer id;
    
    private String nom;

    public Pathologie(Integer id, String nom)
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
