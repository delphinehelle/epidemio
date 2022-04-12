package enfip.epidemio.coordination.batch.raz;

import comp.batch.api.ContexteTraitement;
import comp.batch.api.Niveau;
import comp.batch.api.ReturnCode;
import comp.batch.api.UniteTraitementException;
import comp.batch.api.base.Etape;
import comp.module.appelservice.AppelService;

import enfip.epidemio.service.contrat.ServiceModification;

public class EtapeSuppressionTaux extends Etape
{

    public EtapeSuppressionTaux(String id, String description)
    {
        super(id, description);

    }

    @Override
    public ReturnCode traiter(ContexteTraitement ctx) throws UniteTraitementException
    {
        try
        {
            AppelService localisateur = (AppelService) ctx.getVariable(BatchSuppressionStatistiques.PARAM_MODULE_APPELSERVICES);

            ServiceModification service = (ServiceModification) localisateur.obtenirService(ServiceModification.class);
            ctx.journaliser(Niveau.INFO, "Appel de la suppression des statistiques en taux");
            Integer nbsuppression = service.supprimerTousTaux();
            ctx.journaliser(Niveau.INFO, "Nomre de taux supprimes : " + nbsuppression);
        }
        catch (Exception e)
        {
            ctx.suspendre("Impossible d'executer les suppressions", e);
        }
        return ReturnCode.OK;
    }

}
