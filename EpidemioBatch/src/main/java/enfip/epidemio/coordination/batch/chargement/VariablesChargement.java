/*
 * Copyright (c) 2016 DGFiP - Tous droits réservés
 */
package enfip.epidemio.coordination.batch.chargement;

public interface VariablesChargement
{
    /** Constant : Clef pour accéder au module d'appel de service : {@value #PARAM_MODULE_APPELSERVICES} */
    static final String PARAM_MODULE_APPELSERVICES = "client.appel";

    /** Constant : Clef de la variable de batch qui contient le référentiel des pathologies. */
    static final String VAR_PATHOLOGIES = "pathologies";

    /** Constant : Clef de la valeur du paramètre qui contient le nom de la pathologie : {@value #PARAM_NOM_PATHOLOGIE} */
    static final String PARAM_NOM_PATHOLOGIE = "patho";

    /** Constant : Clef de la variable de batch qui contient le référentiel des régions */
    static final String VAR_REGIONS = "regions";

    /** Constant : Clef de la variable de batch qui les données à charger calculées à partir du fichier */
    static final String VAR_STATS = "stats";

    /**
     * Constant : Clef du paramètre de lancement pour le type de statistiques. <br/>
     * Valeurs possibles :<br/>
     * <ul>
     * <li>{@link enfip.epidemio.coordination.batch.chargement.ValeurParametres#TAUX}</li>
     * <li>{@link enfip.epidemio.coordination.batch.chargement.ValeurParametres#CAS}</li>
     * </ul>
     */
    static final String PARAM_TYPE_STATS = "stype";

    /**
     * Constant : Clef du paramètres de lancement pour le nom complet du fichier d'entrées :
     * {@value #PARAM_NOM_FIC_ENTREES}
     */
    static final String PARAM_NOM_FIC_ENTREES = "fichier";

    /** Constant : VAR_BILAN. */
    static final String VAR_BILAN = "bilan";

    /**
     * Constant : PARAM_REPBILAN. * {@value #PARAM_REPBILAN}
     */
    static final String PARAM_REPBILAN = "repbilan";

}
