package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.criteriumbank.Criterium;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.zoekfilters.CriteriumZoekFilter;

/**
 * @author loite
 */
public interface CriteriumDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Criterium, CriteriumZoekFilter>
{
	public List<Criterium> getCriteria(Opleiding opleiding);

	public Criterium getCriterium(Opleiding opleiding, Cohort cohort, String naam);
}
