package enfip.epidemio.coordination.batch.chargement;

import comp.batch.api.ContexteTraitement;
import comp.batch.api.ReturnCode;
import comp.batch.api.UniteTraitementException;
import comp.batch.api.base.ChaineBatch;
import comp.module.appelservice.AppelService;
import comp.serveur.NanoServeur;

import static enfip.epidemio.coordination.batch.chargement.VariablesChargement.PARAM_MODULE_APPELSERVICES;

public class BatchChargementStatistiques extends ChaineBatch
{
  
    public BatchChargementStatistiques(ContexteTraitement ctx, String id, String description)
    {
        super(ctx, id, description);
    }

    @Override
    public ReturnCode preTraiter(ContexteTraitement ctx) throws UniteTraitementException
    {
        AppelService localisateur =
            (AppelService) NanoServeur.obtenir().rechercherModule(PARAM_MODULE_APPELSERVICES);

        ctx.putVariable(PARAM_MODULE_APPELSERVICES, localisateur);

        return ReturnCode.OK;
    }

}
