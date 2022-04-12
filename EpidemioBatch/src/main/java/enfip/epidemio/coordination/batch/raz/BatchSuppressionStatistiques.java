package enfip.epidemio.coordination.batch.raz;

import comp.batch.api.ContexteTraitement;
import comp.batch.api.ReturnCode;
import comp.batch.api.UniteTraitementException;
import comp.batch.api.base.ChaineBatch;
import comp.module.appelservice.AppelService;
import comp.serveur.NanoServeur;

public class BatchSuppressionStatistiques extends ChaineBatch
{
    /** Constant : Clef pour accéder au module d'appel de service : {@value #PARAM_MODULE_APPELSERVICES} */
    static final String PARAM_MODULE_APPELSERVICES = "client.appel";

    public BatchSuppressionStatistiques(ContexteTraitement ctx, String id, String description)
    {
        super(ctx, id, description);
    }

    @Override
    public ReturnCode preTraiter(ContexteTraitement ctx) throws UniteTraitementException
    {
        AppelService localisateur =
            (AppelService) NanoServeur.obtenir().rechercherModule(BatchSuppressionStatistiques.PARAM_MODULE_APPELSERVICES);

        ctx.putVariable(PARAM_MODULE_APPELSERVICES, localisateur);

        return ReturnCode.OK;
    }

}
