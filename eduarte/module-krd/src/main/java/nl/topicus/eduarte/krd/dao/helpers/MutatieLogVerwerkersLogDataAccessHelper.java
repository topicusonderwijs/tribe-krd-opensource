package nl.topicus.eduarte.krd.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.krd.entities.mutatielog.MutatieLogVerwerkersLog;
import nl.topicus.eduarte.krd.zoekfilters.MutatieLogVerwerkersLogZoekFilter;

public interface MutatieLogVerwerkersLogDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<MutatieLogVerwerkersLog, MutatieLogVerwerkersLogZoekFilter>
{
	public int getQueueCount(MutatieLogVerwerkersLog mutatieLogVerwerkersLog);

	public long getLaatstTeVerwerkenMutatieId(MutatieLogVerwerkersLog mutatieLogVerwerkersLog);
}
