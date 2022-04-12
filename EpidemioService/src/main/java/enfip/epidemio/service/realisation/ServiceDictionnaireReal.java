package enfip.epidemio.service.realisation;

import java.util.LinkedList;
import java.util.List;

import comp.transaction.api.TransactionWrapper;

import enfip.epidemio.domaine.PathologieDAO;
import enfip.epidemio.domaine.RegionDAO;
import enfip.epidemio.domaine.types.Pathologie;
import enfip.epidemio.domaine.types.Region;
import enfip.epidemio.service.contrat.ServiceDictionnaire;
import enfip.epidemio.service.contrat.types.PathologieVO;
import enfip.epidemio.service.contrat.types.RegionVO;

public class ServiceDictionnaireReal extends TransactionWrapper implements ServiceDictionnaire
{

    @Override
    public Boolean ajouterPathologie(PathologieVO p) throws Exception
    {
        return PathologieDAO.creer(this, p.getId(), p.getNom());
    }

    @Override
    public Boolean ajouterRegion(RegionVO r) throws Exception
    {
        return RegionDAO.creer(this, r.getId(), r.getNom());
    }

    @Override
    public List<PathologieVO> lirePathologie() throws Exception
    {
        List<PathologieVO> reponses = new LinkedList<PathologieVO>();

        List<Pathologie> resRecherche = PathologieDAO.lire(this);
        if (resRecherche != null)
            for (Pathologie p : resRecherche)
                reponses.add(new PathologieVO(p.getId(), p.getNom()));
        return reponses;
    }

    @Override
    public List<RegionVO> lireRegion() throws Exception
    {
        List<RegionVO> reponses = new LinkedList<RegionVO>();

        List<Region> resRecherche = RegionDAO.lire(this);
        if (resRecherche != null)
            for (Region r : resRecherche)
                reponses.add(new RegionVO(r.getId(), r.getNom()));
        return reponses;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("ServiceEcho [");
        if (getClass() != null)
        {
            builder.append("getClass()=");
            builder.append(getClass());
        }
        builder.append("]");
        return builder.toString();
    }

}
