package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaal;
import nl.topicus.eduarte.zoekfilters.NaamActiefZoekFilter;

public interface SchaalDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Schaal, NaamActiefZoekFilter<Schaal>>
{
	public Schaal get(String naam);
}
