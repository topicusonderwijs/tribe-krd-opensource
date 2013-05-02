package nl.topicus.eduarte.dao.participatie.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.LesdagIndeling;
import nl.topicus.eduarte.entities.participatie.LesweekIndeling;
import nl.topicus.eduarte.participatie.zoekfilters.LesdagindelingZoekFilter;

public interface LesdagindelingDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<LesdagIndeling, LesdagindelingZoekFilter>
{

	/**
	 * Geeft de lesweekindelingen bij de gegeven instelling.
	 * 
	 * @return de lesweekindelingen
	 */
	public List<LesdagIndeling> list();

	public LesdagIndeling getlesdagIndeling(LesdagindelingZoekFilter filter);

	public List<LesdagIndeling> getLesdagindeling(LesweekIndeling lesweekIndeling);

	public LesdagIndeling getLesdagOpDag(LesweekIndeling lesweekIndeling, String weekDag);
}
