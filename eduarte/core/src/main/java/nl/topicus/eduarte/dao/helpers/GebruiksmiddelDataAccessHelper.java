package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.Gebruiksmiddel;
import nl.topicus.eduarte.zoekfilters.GebruiksmiddelZoekFilter;

public interface GebruiksmiddelDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Gebruiksmiddel, GebruiksmiddelZoekFilter>
{
	public List<Gebruiksmiddel> list();
}
