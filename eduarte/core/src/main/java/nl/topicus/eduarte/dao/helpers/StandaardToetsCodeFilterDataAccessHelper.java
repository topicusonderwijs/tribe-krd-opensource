package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.resultaatstructuur.StandaardToetsCodeFilter;
import nl.topicus.eduarte.zoekfilters.StandaardToetsCodeFilterZoekFilter;

public interface StandaardToetsCodeFilterDataAccessHelper
		extends
		BatchZoekFilterDataAccessHelper<StandaardToetsCodeFilter, StandaardToetsCodeFilterZoekFilter>
{
	public StandaardToetsCodeFilter getStandaardFilter(Opleiding opleiding, Cohort cohort);
}
