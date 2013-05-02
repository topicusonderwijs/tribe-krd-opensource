package nl.topicus.eduarte.dao.helpers.dbs;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.bijzonderheden.Bijzonderheid;
import nl.topicus.eduarte.zoekfilters.dbs.BijzonderheidZoekFilter;

public interface BijzonderheidDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Bijzonderheid, BijzonderheidZoekFilter>
{
}
