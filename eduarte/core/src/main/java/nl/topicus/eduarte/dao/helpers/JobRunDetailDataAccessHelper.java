package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.jobs.logging.JobRunDetail;
import nl.topicus.eduarte.zoekfilters.JobRunDetailZoekFilter;

public interface JobRunDetailDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<JobRunDetail, JobRunDetailZoekFilter<JobRunDetail>>
{
}
