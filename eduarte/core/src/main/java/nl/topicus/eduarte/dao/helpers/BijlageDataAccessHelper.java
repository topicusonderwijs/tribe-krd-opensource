package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.bijlage.Bijlage;
import nl.topicus.eduarte.entities.personen.DeelnemerBijlage;
import nl.topicus.eduarte.zoekfilters.BijlageZoekFilter;

public interface BijlageDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Bijlage, BijlageZoekFilter>
{
	/**
	 * Deze methode is puur voor een update task om de db te fixen
	 */
	public List<DeelnemerBijlage> getDeelnemerBijlagesZonderDeelnemer();
}
