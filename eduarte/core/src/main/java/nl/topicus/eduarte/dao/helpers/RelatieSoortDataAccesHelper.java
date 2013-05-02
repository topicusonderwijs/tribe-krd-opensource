package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Instelling;
import nl.topicus.eduarte.entities.personen.RelatieSoort;
import nl.topicus.eduarte.zoekfilters.RelatieSoortZoekFilter;

/**
 * 
 * 
 * @author vanharen
 */
public interface RelatieSoortDataAccesHelper extends
		BatchZoekFilterDataAccessHelper<RelatieSoort, RelatieSoortZoekFilter>
{
	public RelatieSoort get(Instelling organisatie, String code);

	public List<RelatieSoort> list(boolean persoonOpname, boolean organisatieOpname);
}
