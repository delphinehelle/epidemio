package enfip.epidemio.service.contrat;

import java.util.List;

import enfip.epidemio.service.contrat.types.PathologieVO;
import enfip.epidemio.service.contrat.types.RegionVO;

public interface ServiceDictionnaire
{
    
    List<RegionVO> lireRegion() throws Exception;
    
    List<PathologieVO> lirePathologie() throws Exception;
    
    Boolean ajouterRegion(RegionVO r) throws Exception;
    
    Boolean ajouterPathologie(PathologieVO p) throws Exception;

    
}
