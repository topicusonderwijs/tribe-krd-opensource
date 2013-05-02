package nl.topicus.eduarte.dao.helpers.dbs;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.gedrag.Incident;
import nl.topicus.eduarte.zoekfilters.dbs.IncidentZoekFilter;

public interface IncidentDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Incident, IncidentZoekFilter>
{
}
