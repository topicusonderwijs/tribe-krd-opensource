/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.zoekfilters.DeelnemerZoekFilter;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.hibernate.criterion.Criterion;

/**
 * @author loite
 */
public interface DeelnemerDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Deelnemer, DeelnemerZoekFilter>
{

	/**
	 * Voegt restricties toe aan het gegeven criteriaobject op basis van de restricties
	 * die in het zoekfilter zijn opgegeven. Verwacht dat je de alias al hebt gemaakt.
	 * 
	 * @param builder
	 *            De criteria waaraan de restricties toegevoegd moeten worden
	 * @param filter
	 *            Het filter die de restricties definieert
	 * @param deelnemerAlias
	 *            De alias van Deelnemer bij de criteria
	 * @param persoonAlias
	 *            De alias van Persoon bij de criteria
	 */
	public void addCriteria(CriteriaBuilder builder, DeelnemerZoekFilter filter,
			String deelnemerAlias, String persoonAlias);

	/**
	 * Zoek deelnemer met id.
	 * 
	 * @param id
	 * @return deelnemer of null als deze niet bestaat
	 */
	public Deelnemer get(Long id);

	/**
	 * Geeft een lijst van id's terug van deelnemers die aan het zoekfilter voldoen
	 * 
	 * @param filter
	 * @return Een lijst van deelnemerid's
	 */
	public List<Long> getIds(VerbintenisZoekFilter filter);

	/**
	 * Geeft de deelnemer van de instelling terug met het bijbehorende id
	 * 
	 * @param id
	 * @return De deelnemer
	 */
	public Deelnemer getById(Long id);

	/**
	 * @param deelnemernummer
	 * @return De deelnemer
	 */
	public Deelnemer getByDeelnemernummer(Integer deelnemernummer);

	public Deelnemer getByStudielinknummer(Integer studielinknummer);

	/**
	 * @param bsn
	 * @return De deelnemer
	 */
	public List<Deelnemer> getByBSN(Long bsn);

	/**
	 * @param onderwijsnummer
	 * @return De deelnemer met het gegeven onderwijsnummer
	 */
	public List<Deelnemer> getByOnderwijsnummer(Long onderwijsnummer);

	/**
	 * @param persoon
	 * @return De deelnemer die gekoppeld is aan de gegeven persoon
	 */
	public Deelnemer getByPersoon(Persoon persoon);

	public List<Deelnemer> getLeerplichtige(VerbintenisZoekFilter filter);

	public Deelnemer getDeelnemerByIdInOudpakket(Long id);

	public void addMentorDocentVerantwoordelijkeUitvoerende(CriteriaBuilder builder,
			List<Criterion> mentorDocent, List<Criterion> verantwoordelijkeUitvoerende);

	/**
	 * Bepaalt de deelnemers die gisteren op de een of andere manier inactief geworden
	 * zijn. Inactief betekent dat de deelnemer geen actieve verbintenissen meer heeft.
	 * Dit kan dus gebeuren doordat de einddatum van de laatste actieve verbintenis op
	 * gisteren staat, of dat een gebruiker gisteren de verbintenis beeindigd heeft op een
	 * datum in het verleden.
	 */
	public List<Deelnemer> getDeelnemersDieGisterenInactiefGewordenZijn();

	public List<Long> getDeelnemerIdsMetResultaten();
}
