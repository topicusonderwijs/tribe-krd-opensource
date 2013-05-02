/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.CompetentieMatrix;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Meeteenheid;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.MeeteenheidKoppel;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.MeeteenheidKoppelType;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.MeeteenheidWaarde;
import nl.topicus.eduarte.zoekfilters.MeeteenheidZoekFilter;

/**
 * @author vandenbrink
 */
public interface MeeteenheidDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Meeteenheid, MeeteenheidZoekFilter>
{

	/**
	 * De meeteenheid voor de opgegeven opleiding bij het opgegeven cohort. Als deze niet
	 * gedefinieerd is, wordt de meeteenheid van de organisatie-eenheid van de opleiding
	 * of een bovenliggende teruggegeven.
	 * 
	 * @param opleiding
	 * @param type
	 * @return meeteenheid
	 */
	public Meeteenheid getMeeteenheid(Opleiding opleiding, Cohort cohort, MeeteenheidKoppelType type);

	/**
	 * De meeteenheid voor de opgegeven organisatie-eenheid bij het opgegeven cohort. Als
	 * er geen gedefinieerd is, wordt de meeteenheid van een bovenliggende
	 * organisatie-eenheid teruggegeven (op zijn minst moet er een meeteenheid zijn voor
	 * de instellingen, d.w.z. de root organisatie-eenheid.)
	 * 
	 * @param organisatieEenheid
	 * @param type
	 * @param cohort
	 * @return meeteenheid
	 */
	public Meeteenheid getMeeteenheid(OrganisatieEenheid organisatieEenheid,
			MeeteenheidKoppelType type, Cohort cohort);

	/**
	 * De meeteenheid die direct gekoppeld is aan de organisatieeenheid. Als deze er niet
	 * is, wordt null teruggegeven. Er wordt dus niet gekeken naar meeteenheiden geerfd
	 * uit een hogere organisatieeenheid.
	 * 
	 * @param organisatieEenheid
	 * @param type
	 * @return meeteenheid
	 */
	public Meeteenheid getDirecteMeeteenheid(OrganisatieEenheid organisatieEenheid, Cohort cohort,
			MeeteenheidKoppelType type);

	/**
	 * De meeteenheid die direct gekoppeld is aan de opleiding. Als deze er niet is, wordt
	 * null teruggegeven. Er wordt dus niet gekeken naar meeteenheden geeerfd uit de
	 * organisatie-eenheid hierarchie van deze opleiding.
	 * 
	 * @param opleiding
	 * @param type
	 * @return meeteenheid
	 */
	public Meeteenheid getDirecteMeeteenheid(Opleiding opleiding, MeeteenheidKoppelType type);

	/**
	 * Lookup functie, vind het tekstuele label behorend bij deze numerieke waarde. Geeft
	 * null als deze waarde niet bestaat voor de meeteenheid.
	 * 
	 * @param meeteenheid
	 * @param waarde
	 * @return tekstuele label, of null
	 */
	public String getLabelVoorWaarde(Meeteenheid meeteenheid, int waarde);

	/**
	 * Lookup functie, vind de numerieke waarde voor het gegeven tekstuele label voor deze
	 * Meeteenheid. Geeft null als dit label niet bestaat voor de meeteenheid.
	 * 
	 * @param meeteenheid
	 * @param label
	 * @return integer waarde van het label, of null
	 */
	public Integer getWaardeVoorLabel(Meeteenheid meeteenheid, String label);

	/**
	 * De minimum {@link MeeteenheidWaarde} bij deze {@link Meeteenheid}.
	 * 
	 * @param meeteenheid
	 * @return minimum
	 */
	public MeeteenheidWaarde getMinimumWaarde(Meeteenheid meeteenheid);

	/**
	 * Het maximum {@link MeeteenheidWaarde} bij deze {@link Meeteenheid}.
	 * 
	 * @param meeteenheid
	 * @return maximum
	 */
	public MeeteenheidWaarde getMaximumWaarde(Meeteenheid meeteenheid);

	/**
	 * Vindt de {@link MeeteenheidWaarde} met het gegeven label bij de gegeven
	 * {@link Meeteenheid}
	 * 
	 * @param meeteenheid
	 * @param label
	 * @return de {@link MeeteenheidWaarde}
	 */
	public MeeteenheidWaarde getMeeteenheidWaarde(Meeteenheid meeteenheid, String label);

	/**
	 * * Vindt de {@link MeeteenheidWaarde} met de gegeven waarde bij de gegeven
	 * {@link Meeteenheid}
	 * 
	 * @param meeteenheid
	 * @param waarde
	 * @return de {@link MeeteenheidWaarde}
	 */
	public MeeteenheidWaarde getMeeteenheidWaarde(Meeteenheid meeteenheid, Integer waarde);

	public boolean isMeeteenheidInGebruik(Meeteenheid meeteenheid);

	public MeeteenheidKoppel getMeeteenheidKoppeling(OrganisatieEenheid organisatieEenheid,
			Cohort cohort, MeeteenheidKoppelType type);

	public MeeteenheidKoppel getMeeteenheidKoppeling(Opleiding opleiding, Cohort cohort,
			MeeteenheidKoppelType type);

	public Meeteenheid getMeeteenheid(Verbintenis verbintenis, CompetentieMatrix matrix,
			Opleiding opleiding, Cohort cohort);

	public Meeteenheid getMeeteenheid(CompetentieMatrix matrix, Opleiding opleiding, Cohort cohort);

	public Meeteenheid getTaalMeeteenheid(Verbintenis verbintenis, Opleiding opleiding,
			Cohort cohort);
}
