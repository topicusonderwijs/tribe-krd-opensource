package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.ZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.CompetentieMatrix;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.LokaalCompetentieMaximum;
import nl.topicus.eduarte.zoekfilters.LokaalCompetentieMaximumZoekFilter;

public interface LokaalCompetentieMaximumDataAccessHelper extends
		ZoekFilterDataAccessHelper<LokaalCompetentieMaximum, LokaalCompetentieMaximumZoekFilter>
{
	public LokaalCompetentieMaximum getLokaalCompetentieMaximum(Opleiding opleiding,
			CompetentieMatrix matrix, Cohort cohort);

	public LokaalCompetentieMaximum getLokaalCompetentieMaximum(Opleiding opleiding, Cohort cohort);
}
