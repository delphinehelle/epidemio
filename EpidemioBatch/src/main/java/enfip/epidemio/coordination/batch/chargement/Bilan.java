package enfip.epidemio.coordination.batch.chargement;

import static enfip.epidemio.coordination.batch.chargement.VariablesChargement.PARAM_NOM_FIC_ENTREES;
import static enfip.epidemio.coordination.batch.chargement.VariablesChargement.PARAM_REPBILAN;
import static enfip.epidemio.coordination.batch.chargement.VariablesChargement.VAR_BILAN;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map.Entry;

import comp.batch.api.ContexteTraitement;
import comp.batch.api.Niveau;
import comp.batch.api.ReturnCode;
import comp.batch.api.UniteTraitementException;
import comp.batch.api.base.Etape;
import comp.service.protocole.api.Erreur;

import enfip.epidemio.service.contrat.SequenceDonneesEpidemiomlogique;

public class Bilan extends Etape
{

    public Bilan(String id, String description)
    {
        super(id, description); // DOCUMENTEZ_MOI Raccord de constructeur auto-généré
    }

    @Override
    public ReturnCode postTraiter(ContexteTraitement ctx) throws UniteTraitementException
    {
        String repbilan = ctx.getParametres(PARAM_REPBILAN);
        String nomFicIn = ctx.getParametres(PARAM_NOM_FIC_ENTREES);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss");
        String ficBilan = repbilan + "BIC-" + format(nomFicIn) + "-" + df.format(Calendar.getInstance().getTime()) + ".txt";
        DonneesBilan bilan = (DonneesBilan) ctx.getVariable(VAR_BILAN);
        ctx.journaliser(Niveau.INFO, "Debut ecriture du bilan dans : " + ficBilan);

        try (FileWriter fw = new FileWriter(ficBilan, false))
        {
            fw.write("BILAN DU CHARGEMENT\n");
            fw.write("Nombre de données chargées : " + bilan.getNbSucces() + "\n");
            fw.write("ANOMALIES\n");

            for (Entry<SequenceDonneesEpidemiomlogique, List<Erreur>> entree : bilan.getAnomalies().entrySet())
            {
                SequenceDonneesEpidemiomlogique ligne = entree.getKey();
                fw.write("Ligne[" + ligne.getAnnee() + "," + ligne.getSemaine() + "," + ligne.getIdentifiantPathologie() + "]\n");
                fw.write("[données soumises = {");
                Integer[] tab = ligne.getValeurs();
                for (int i = 0; i < tab.length - 1; i++)
                    fw.write(tab[i] + ",");
                fw.write(tab[tab.length - 1] + "}]\n");
                fw.write("[Debut Anomalies]\n");
                for (Erreur e : entree.getValue())
                    fw.write("code=" + e.getCode() + "\n");
                fw.write("[Fin Anomalies]\n");
            }
        }
        catch (Exception e)
        {
            ctx.suspendre("Production du bilan " + ficBilan, e);
        }

        ctx.journaliser(Niveau.INFO, "Fin ecriture du bilan");

        return ReturnCode.OK;
    }

    private String format(String nomFicIn)
    {
        int rpath = nomFicIn.lastIndexOf("/");
        return nomFicIn.substring(rpath == -1 ? 0 : rpath + 1, nomFicIn.length());
    }

}
