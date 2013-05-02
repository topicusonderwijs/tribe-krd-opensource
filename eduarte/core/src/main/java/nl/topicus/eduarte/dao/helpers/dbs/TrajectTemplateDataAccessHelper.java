package nl.topicus.eduarte.dao.helpers.dbs;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.trajecten.templates.TrajectTemplate;
import nl.topicus.eduarte.zoekfilters.dbs.TrajectTemplateZoekFilter;

public interface TrajectTemplateDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<TrajectTemplate, TrajectTemplateZoekFilter>
{
}
