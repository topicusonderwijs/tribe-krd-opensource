package nl.topicus.eduarte.dao.participatie.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.eduarte.entities.participatie.DeelnemerMedewerkerGroep;
import nl.topicus.eduarte.zoekfilters.DeelnemerMedewerkerGroepZoekFilter;

public interface DeelnemerMedewerkerGroepDataAccesHelper
		extends
		BatchZoekFilterDataAccessHelper<DeelnemerMedewerkerGroep, DeelnemerMedewerkerGroepZoekFilter>
{
	public List<DeelnemerMedewerkerGroep> list();

	public DeelnemerMedewerkerGroep getByFullID(String id);

	public DeelnemerMedewerkerGroep get(IdObject object);
}
