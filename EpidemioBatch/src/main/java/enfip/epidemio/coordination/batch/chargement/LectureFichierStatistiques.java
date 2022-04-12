package enfip.epidemio.coordination.batch.chargement;

import static enfip.epidemio.coordination.batch.chargement.VariablesChargement.PARAM_NOM_FIC_ENTREES;
import static enfip.epidemio.coordination.batch.chargement.VariablesChargement.PARAM_NOM_PATHOLOGIE;
import static enfip.epidemio.coordination.batch.chargement.VariablesChargement.PARAM_TYPE_STATS;
import static enfip.epidemio.coordination.batch.chargement.VariablesChargement.VAR_PATHOLOGIES;
import static enfip.epidemio.coordination.batch.chargement.VariablesChargement.VAR_REGIONS;
import static enfip.epidemio.coordination.batch.chargement.VariablesChargement.VAR_STATS;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import comp.batch.api.ContexteTraitement;
import comp.batch.api.Niveau;
import comp.batch.api.ReturnCode;
import comp.batch.api.UniteTraitementException;
import comp.batch.api.base.Etape;
import enfip.epidemio.service.contrat.SequenceDonneesEpidemiomlogique;
import enfip.epidemio.service.contrat.types.PathologieVO;
import enfip.epidemio.service.contrat.types.RegionVO;

public class LectureFichierStatistiques extends Etape
{
	// Identifiant de la pahologie
	private Integer idPatho;

	// On va calculer puis stocker en mémoire une table qui établit la coorespondance entre :
	// - le rang du libellé de la région dans l'entête du fichier
	// - et la donnée du référentiel des régions

	private Map<Integer, RegionVO> mapEnteteRegion = new HashMap<Integer, RegionVO>();

	// Nom du fichier en entree
	private String nomFicIn;

	// Nom de la pathologie
	private String nomPatho;

	// Type de chronologie (en année ou en année/semaine)
	private String typeChrono;

	private Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

	public LectureFichierStatistiques(String id, String description)
	{
		super(id, description);

	}

	@SuppressWarnings("unchecked")
	@Override
	public ReturnCode preTraiter(ContexteTraitement ctx) throws UniteTraitementException
	{
		this.nomFicIn = ctx.getParametres(PARAM_NOM_FIC_ENTREES);
		ctx.journaliser(Niveau.INFO, "Fichier a traiter : " + this.nomFicIn);

		// Verification de l'existence du fichier
		File ficin = new File(this.nomFicIn);
		if (Files.exists(ficin.toPath(), LinkOption.NOFOLLOW_LINKS) == false)
			ctx.suspendre("Le fichier " + ficin + " n'existe pas");

		// Validation du type de statistiques
		String typeStats = ctx.getParametres(PARAM_TYPE_STATS);
		ctx.journaliser(Niveau.INFO, "Type de statistiques : " + typeStats);
		if (ValeurParametres.CAS.compareToIgnoreCase(typeStats) != 0 && ValeurParametres.TAUX.compareToIgnoreCase(typeStats) != 0)
			ctx.suspendre("Le type de statistique " + typeStats + " n'existe pas");

		this.nomPatho = ctx.getParametres(PARAM_NOM_PATHOLOGIE);
		ctx.journaliser(Niveau.INFO, "Type de statistiques : " + this.nomPatho);

		// Vérification de l'existence de la pathologie
		ctx.journaliser(Niveau.INFO, "Verification de l'existence de la pathologie : " + this.nomPatho);
		List<PathologieVO> listepatho = (List<PathologieVO>) ctx.getVariable(VAR_PATHOLOGIES);
		boolean existence = false;
		for (int index = 0; index < listepatho.size() && existence == false; index++)
			if (listepatho.get(index).getNom().compareToIgnoreCase(this.nomPatho) == 0)
			{
				existence = true;
				// La pathologie existe alors on place son identifiant pour utilisation ultérieure
				this.idPatho = listepatho.get(index).getId();
			}
		if (existence == false)
			ctx.suspendre("La pathologie " + this.nomPatho + " n'existe pas");

		List<String> entete = null;
		try
		{
			entete = decoderPremiereLigne(ficin);
		}
		catch (Exception e)
		{
			ctx.suspendre(e.getMessage(), e);
		}

		validerEtTraiterEntete(ctx, entete);

		return ReturnCode.OK;
	}

	@Override
	public ReturnCode traiter(ContexteTraitement ctx) throws UniteTraitementException
	{
		List<SequenceDonneesEpidemiomlogique> dataACharger = new LinkedList<SequenceDonneesEpidemiomlogique>();

		try
		{            
			dataACharger = Files.lines(Paths.get(this.nomFicIn), StandardCharsets.UTF_8).collect(Collectors.toList())
					.stream()
					.skip(1) //ne pas traiter la ligne d'entête
					.parallel()                    
					.map(ligne -> construireSequence(ligne, ctx))
					.collect(Collectors.toList());
			ctx.putVariable(VAR_STATS, dataACharger);
		}
		catch (Exception e)
		{
			ctx.suspendre(e.getMessage(), e);
		}

		return ReturnCode.OK;
	}

	private SequenceDonneesEpidemiomlogique construireSequence(String ligne, ContexteTraitement ctx)
	{
		SequenceDonneesEpidemiomlogique seq = null;

		String[] tab = ligne.split(",");

		Integer annee;
		Integer semaine;

		if (this.typeChrono.compareToIgnoreCase(ValeurParametres.EN_ANNEE) == 0)
		{
			semaine = 0;
			annee = Integer.decode(tab[0]);
		}
		else
		{
			int nb = Integer.decode(tab[0]);
			semaine = nb % 100;
			annee = (nb - semaine) / 100;
		}
		seq = new SequenceDonneesEpidemiomlogique(annee, semaine, this.idPatho);

		seq.setValeurs(Arrays.asList(tab)
				.stream()
				.skip(1)
				.map(s -> traitementDonneeNumeric(s, ctx))
				.toArray(Integer[]::new)
				);
		return seq;
	}

	private Integer traitementDonneeNumeric(String s, ContexteTraitement ctx) {

		Integer val = null;
		if(isNumeric(s))
		{
			val = new Integer(s);
		}
		else if(s != null && !s.equals("-"))
		{
			ctx.suspendre("fichier corrompu");
		}
		return val;
	}

	private List<String> decoderPremiereLigne(File ficin) throws Exception
	{
		List<String> res = new ArrayList<String>();

		try (Scanner scan = new Scanner(new FileInputStream(ficin)))
		{
			if (scan.hasNextLine())
			{
				String ligne = scan.nextLine();
				try (Scanner lignescan = new Scanner(ligne))
				{
					lignescan.useDelimiter(",");
					while (lignescan.hasNext())
						res.add(lignescan.next());
				}
			}
		}

		return res;
	}

	@SuppressWarnings("unchecked")
	private void validerEtTraiterEntete(ContexteTraitement ctx, List<String> entete)
	{
		ctx.journaliser(Niveau.INFO, "Verification et traitement de l'entete du fichier");

		String modeSerieChronologique = entete.get(0);

		if (modeSerieChronologique.compareTo(ValeurParametres.EN_ANNEE) != 0
				&& modeSerieChronologique.compareTo(ValeurParametres.EN_SEMAINE) != 0)
			ctx.suspendre("Code entete du mode de serie chronologique inconne : " + modeSerieChronologique);
		else
			this.typeChrono = entete.get(0);

		List<String> libellesRegion = entete.subList(1, entete.size());

		List<RegionVO> regions = (List<RegionVO>) ctx.getVariable(VAR_REGIONS);

		if (regions.size() != libellesRegion.size())
			ctx.suspendre("Nombre de regions incoherent entre le fichier et le referentiel");

		boolean demandeInterruption = false;

		List<String> libellesRegionReferentiel = new ArrayList<String>();

		for (RegionVO r : regions)
			libellesRegionReferentiel.add(r.getNom());

		for (int i = 0; i < libellesRegion.size(); i++)
		{
			int rangRegion = libellesRegionReferentiel.indexOf(libellesRegion.get(i));
			if (rangRegion == -1)
			{
				ctx.journaliser(Niveau.ERROR, libellesRegion.get(i) + " absente du referentiel");
				demandeInterruption = true;
			}
			else
			{
				// On a trouvé donc on alimente la table de correspondance
				this.mapEnteteRegion.put(i, regions.get(rangRegion));
			}
		}

		if (demandeInterruption)
			ctx.suspendre("Ecart entre les regions de l'entete et le referentiel");

	}

	private boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false; 
		}
		return pattern.matcher(strNum).matches();
	}
}
