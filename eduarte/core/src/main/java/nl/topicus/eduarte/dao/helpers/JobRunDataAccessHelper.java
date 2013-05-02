/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import java.util.Date;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.zoekfilters.JobRunZoekFilter;

public interface JobRunDataAccessHelper<T extends JobRun, ZF extends JobRunZoekFilter<T>> extends
		BatchZoekFilterDataAccessHelper<T, ZF>
{
	/**
	 * @param jobrunClass
	 * @return De laatste run van de gegeven job
	 */
	public <R extends T> R getLaatsteRun(Class<R> jobrunClass);

	/**
	 * Functie welke voor de {@link JobRun}s het resultaat verwijdert waar de startdatum
	 * ouder is dan de opgegeven datum.
	 * 
	 * @param jobrunClass
	 * @param date
	 */
	public int cleanupOldRuns(Class< ? extends JobRun> jobrunClass, Date date);
}
