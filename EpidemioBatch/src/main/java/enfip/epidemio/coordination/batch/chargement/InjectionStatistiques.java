package enfip.epidemio.coordination.batch.chargement;

import static enfip.epidemio.coordination.batch.chargement.VariablesChargement.PARAM_MODULE_APPELSERVICES;
import static enfip.epidemio.coordination.batch.chargement.VariablesChargement.PARAM_TYPE_STATS;
import static enfip.epidemio.coordination.batch.chargement.VariablesChargement.VAR_BILAN;
import static enfip.epidemio.coordination.batch.chargement.VariablesChargement.VAR_STATS;

import java.util.Iterator;
import java.util.List;

import comp.batch.api.ContexteTraitement;
import comp.batch.api.Niveau;
import comp.batch.api.ReturnCode;
import comp.batch.api.UniteTraitementException;
import comp.batch.api.base.Etape;
import comp.module.appelservice.AppelService;
import enfip.epidemio.service.contrat.Resultat;
import enfip.epidemio.service.contrat.SequenceDonneesEpidemiomlogique;
import enfip.epidemio.service.contrat.ServiceModification;

public class InjectionStatistiques extends Etape
{

    public InjectionStatistiques(String id, String description)
    {
        super(id, description);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ReturnCode traiter(ContexteTraitement ctx) throws UniteTraitementException
    {
        ctx.journaliser(Niveau.INFO, "Initialisation du bilan");
        DonneesBilan bilan = new DonneesBilan();
        ctx.putVariable(VAR_BILAN, bilan);

        //SequenceDonneesEpidemiomlogique current = null;
        try
        {
            AppelService localisateur = (AppelService) ctx.getVariable(PARAM_MODULE_APPELSERVICES);

            ServiceModification service = (ServiceModification) localisateur.obtenirService(ServiceModification.class);

            ctx.journaliser(Niveau.INFO, "Debut de l'injection des statistiques");
            List<SequenceDonneesEpidemiomlogique> datas = (List<SequenceDonneesEpidemiomlogique>) ctx.getVariable(VAR_STATS);
            String typeStats = ctx.getParametres(PARAM_TYPE_STATS);

            List<Resultat> listeRes = null;
            if (typeStats.compareToIgnoreCase(ValeurParametres.CAS) == 0)
            {
            	listeRes = service.ajouterNombreDeCas(datas);
            }
            else if (typeStats.compareToIgnoreCase(ValeurParametres.TAUX) == 0)
            {
            	listeRes = service.ajouterTauxIncidence(datas);            	       	
            }              
            majBilan(bilan, listeRes, datas);
            ctx.journaliser(Niveau.INFO, "Fin de l'injection des statistiques");
        }
        catch (Exception e)
        {
            //ctx.journaliser(Niveau.ERROR, "erreur de traitement sur : " + (current == null ? null : current.toString()));
        	ctx.journaliser(Niveau.ERROR, "erreur de traitement sur : injection.java");
            throw new UniteTraitementException(e);
        	
        }
        return ReturnCode.OK;
    }

	private void majBilan(DonneesBilan bilan, List<Resultat> listeRes,
			List<SequenceDonneesEpidemiomlogique> datas) {
		Iterator<SequenceDonneesEpidemiomlogique> iterDatas = datas.iterator(); 
		for (Resultat res : listeRes)
		{
		    SequenceDonneesEpidemiomlogique data = iterDatas.next(); 
		    if(res.getListeErreur().isEmpty())
		        bilan.incrementerSucces();
		    else
		        bilan.ajouterAnomalie(data,res.getListeErreur());
		  
		}
	}

}
