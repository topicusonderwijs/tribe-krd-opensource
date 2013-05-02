package nl.topicus.eduarte.dao.participatie.helpers;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.PersoonlijkeGroep;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.participatie.zoekfilters.PersoonlijkeGroepZoekFilter;
import nl.topicus.eduarte.providers.PersoonProvider;

public interface PersoonlijkeGroepDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<PersoonlijkeGroep, PersoonlijkeGroepZoekFilter>
{
	/**
	 * Lijst van alle persoonlijkegroepen
	 */
	public List<PersoonlijkeGroep> list();

	/**
	 * @param deelnemer
	 * @return De persoonlijke groepen waarin de gegeven deelnemer zit
	 */
	public List<PersoonlijkeGroep> list(Deelnemer deelnemer);

	/**
	 * Geeft de deelnemers die ingedeeld zijn in de gegeven groep op de gegeven peildatum.
	 * 
	 * @param groep
	 * @param peilDatum
	 * @return de deelnemers
	 */
	public List<Deelnemer> getDeelnemers(PersoonlijkeGroep groep, Date peilDatum);

	public boolean isConflicterend(PersoonlijkeGroep groep, String code, boolean gedeeld,
			PersoonProvider eigenaar);
}
