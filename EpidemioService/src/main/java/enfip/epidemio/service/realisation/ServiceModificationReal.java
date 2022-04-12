package enfip.epidemio.service.realisation;

import java.util.ArrayList;
import java.util.List;

import comp.service.protocole.api.Erreur;
import comp.transaction.api.TransactionWrapper;
import enfip.epidemio.domaine.NombreDeCasDAO;
import enfip.epidemio.domaine.TauxDAO;
import enfip.epidemio.service.contrat.LigneEpidemiologique;
import enfip.epidemio.service.contrat.Resultat;
import enfip.epidemio.service.contrat.SequenceDonneesEpidemiomlogique;
import enfip.epidemio.service.contrat.ServiceModification;

public class ServiceModificationReal extends TransactionWrapper implements ServiceModification
{

	@Override
	public List<Resultat> ajouterTauxIncidence(List<SequenceDonneesEpidemiomlogique> requetes) throws Exception
	{
		List<Resultat> resultats = new ArrayList<>();
		List<SequenceDonneesEpidemiomlogique> requetesOK = new ArrayList<>();
		for(SequenceDonneesEpidemiomlogique requete : requetes)
		{
			Resultat res = new Resultat();
			if ((!ControleurRequete.verifier(requete, res) && notExistsTaux(res, requete)) == false)
			{
				requetesOK.add(requete);
			}
				
			resultats.add(res);
		}
		
		TauxDAO.ajouter(this, requetesOK);
		return resultats;
	}

	@Override
	public List<Resultat> ajouterNombreDeCas(List<SequenceDonneesEpidemiomlogique> requetes) throws Exception
	{
		List<Resultat> resultats = new ArrayList<>();
		List<SequenceDonneesEpidemiomlogique> requetesOK = new ArrayList<>();
		for(SequenceDonneesEpidemiomlogique requete : requetes)
		{
			Resultat res = new Resultat();
			if ((!ControleurRequete.verifier(requete, res) && notExistsCas(res, requete)) == false)
			{
				requetesOK.add(requete);
			}				
			resultats.add(res);
		}
		NombreDeCasDAO.ajouter(this, requetesOK);
		return resultats;
	}

	private boolean notExistsCas(Resultat res, SequenceDonneesEpidemiomlogique requete) throws Exception
	{
		if (NombreDeCasDAO.exist(this, requete.getAnnee(), requete.getSemaine(), requete.getIdentifiantPathologie()))
		{
			Erreur e = new Erreur(LigneEpidemiologique.STAT_EXISTANTE, requete.toString());
			res.getListeErreur().add(e);
			return true;
		}
		return false;
	}

	private boolean notExistsTaux(Resultat res, SequenceDonneesEpidemiomlogique requete) throws Exception
	{
		if (TauxDAO.exist(this, requete.getAnnee(), requete.getSemaine(), requete.getIdentifiantPathologie()))
		{
			Erreur e = new Erreur(LigneEpidemiologique.STAT_EXISTANTE, requete.toString());
			res.getListeErreur().add(e);
			return true;
		}
		return false;
	}

	@Override
	public Resultat supprimerTaux(LigneEpidemiologique requete) throws Exception
	{
		Resultat res = new Resultat();
		if (ControleurRequete.verifier(requete, res))
			TauxDAO.supprimer(this, requete.getAnnee(), requete.getSemaine(), requete.getIdentifiantPathologie());
		return res;
	}

	@Override
	public Resultat supprimerNombreDeCas(LigneEpidemiologique requete) throws Exception
	{
		Resultat res = new Resultat();
		if (ControleurRequete.verifier(requete, res))
			NombreDeCasDAO.supprimer(this, requete.getAnnee(), requete.getSemaine(), requete.getIdentifiantPathologie());
		return res;
	}

	@Override
	public Integer supprimerTousNombreDeCas() throws Exception
	{
		return NombreDeCasDAO.supprimer(this);
	}

	@Override
	public Integer supprimerTousTaux() throws Exception
	{
		return TauxDAO.supprimer(this);
	}

}
