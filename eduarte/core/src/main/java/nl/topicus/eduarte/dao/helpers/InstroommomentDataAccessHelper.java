package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.hogeronderwijs.Instroommoment;
import nl.topicus.eduarte.zoekfilters.InstroommomentZoekFilter;

public interface InstroommomentDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Instroommoment, InstroommomentZoekFilter>
{
	public Instroommoment get(String code);
}
