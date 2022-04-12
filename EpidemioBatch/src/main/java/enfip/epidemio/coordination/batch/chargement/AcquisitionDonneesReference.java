package enfip.epidemio.coordination.batch.chargement;

import static enfip.epidemio.coordination.batch.chargement.VariablesChargement.PARAM_MODULE_APPELSERVICES;
import static enfip.epidemio.coordination.batch.chargement.VariablesChargement.VAR_PATHOLOGIES;
import static enfip.epidemio.coordination.batch.chargement.VariablesChargement.VAR_REGIONS;

import comp.batch.api.ContexteTraitement;
import comp.batch.api.Niveau;
import comp.batch.api.ReturnCode;
import comp.batch.api.base.Etape;
import comp.module.appelservice.AppelService;
import enfip.epidemio.service.contrat.ServiceDictionnaire;

public class AcquisitionDonneesReference extends Etape
{

    public AcquisitionDonneesReference(String id, String description)
    {
        super(id, description);
    }

    @Override
    public ReturnCode traiter(ContexteTraitement ctx)
    {
        try
        {
            AppelService localisateur = (AppelService) ctx.getVariable(PARAM_MODULE_APPELSERVICES);

            ServiceDictionnaire service = (ServiceDictionnaire) localisateur.obtenirService(ServiceDictionnaire.class);
            ctx.journaliser(Niveau.INFO, "Acquisition des pathologies");
            ctx.putVariable(VAR_PATHOLOGIES, service.lirePathologie());
            ctx.journaliser(Niveau.INFO, "Acquisition des regions");
            ctx.putVariable(VAR_REGIONS, service.lireRegion());
        }
        catch (Exception e)
        {
            ctx.suspendre("Impossible d'executer le service", e);
        }
        return ReturnCode.OK;
    }

}
